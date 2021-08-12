import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.fluent.Request;
import org.apache.tika.parser.txt.CharsetDetector;
import org.apache.tika.parser.txt.CharsetMatch;
import util.Utils;
import javax.activation.MimetypesFileTypeMap;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static thredds.catalog.InvService.iso;

/**
 * This class serves as an interface through which users interact with the program.
 *
 * <p>Client is an important class to obtain, send, receive and present messages. Fist, Client gets the user's command through command line.
 * Second, convert the command into the html requests and send it to the {@see #Server}.
 * Third, Receive and handle the response and output the result to user.
 *
 *
 * @author Gao Manlin
 * @since JDK1.8
 *
 *
 */
public class Client{
    /**
     * The endpoint of the html request.
     */
    static String endpoint = "http://localhost:7001/files";
    static String dash="----------------------------------------";

    /**
     * The remark to user which means the following is the result.
     */
    static String star="★";

    /**
     * A mapper to map the received string into preferred class object.
     */
    static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Enumeration of the operations: {@code EXISTS, UPLOAD, DOWNLOAD, COMPARE, LIST, DELETE, HELP}.
     */
    enum Operation{
        UPLOAD, DOWNLOAD, COMPARE, EXISTS, LIST, DELETE, HELP
    }

    /**
     * Static method to convert the user's command into the defined enumeration of operations.
     * If the command is not predefined, return <code>null</code>.
     *
     * @param op
     *        String.
     * @return Operation
     */
    public static Operation parseOperation(String op){
        //TODO: convert String to Operation
        switch(op.toLowerCase()){
            case "upload":
                return Operation.UPLOAD;
            case "download":
                return Operation.DOWNLOAD;
            case "compare":
                return  Operation.COMPARE;
            case "exists":
                return Operation.EXISTS;
            case "list":
                return Operation.LIST;
            case "delete":
                return Operation.DELETE;
            case "help":
                return Operation.HELP;
            default:
                return null;
        }

    }

    /**
     * Receive the user's requests. Handle some exceptions such as nonsense command.
     *
     * @param args
     * User input.
     * @throws IOException
     * If some methods throws Exception.
     *
     */
    public static void main(String[] args) throws IOException {
        /**
         * When the client is connected, wave hello to the users. {@code responseString} will get "Welcome to RESTful Corpus Platform!".
         */
        String responseString = Request.Get("http://localhost:7001/").execute().returnContent().asString();
        System.out.println(responseString);

        while(true) {
            Scanner in = new Scanner(System.in);
            args = in.nextLine().split("\\s+");

            if (args.length < 2 && !args[0].toLowerCase().equals("list") && !args[0].toLowerCase().equals("help")) {
                System.err.println("Simple Client");
                printUsage();
                continue;
            }

            Operation operation = parseOperation(args[0]);
            if (operation == null) {
                System.err.println("Unknown operation");
                printUsage();
                continue;
            }

            switch (operation) {
                case UPLOAD:
                    handleUpload(args);
                    break;
                case DOWNLOAD:
                    handleDownload(args);
                    break;
                case COMPARE:
                    handleCompare(args);
                    break;
                case EXISTS:
                    handleExists(args);
                    break;
                case LIST:
                    handleList();
                    break;
                case DELETE:
                    handleDelete(args);
                    break;
                case HELP:
                    handleHelp();
            }
        }
    }

    /**
     * Check if a specific file exists in the database.
     *
     * <p> Receive the request with the file identification. If the identification is name, convert to the md5.
     * Use {@code Request.Get(buildURI(args)).execute().returnContent().asString()} to send the request to server and get string result.
     * The return will be in Json like:
     * <pre>{
     *      code	-> 0 or	error code,
     *      message	->{} or error description,
     *      result	->{
     *          exists -> true or false
     *          }
     *     }
     *
     * Then output the result.
     *
     * @param args
     *        {@code args[0]} contains the command, {@code args[1]} contains the file identification or name.
     * @throws IOException
     *         If {@code bom()} throws IOException.
     *         If The URI has error.{@code execute()} can't hold.
     *         If {@code objectMapper} can't map to the class.
     *
     */
    private static void handleExists(String[] args){//md5 or name
        File file=new File(args[1]);
        byte[] utf8bytes;
        if(file.isDirectory()){
            System.err.println("File provided is a directory.");
            return;
        }
        if(file.exists()) {

            if(!isText(file)){
                System.err.println("File provided is a binary file.");
                return;
            }
            try{
                utf8bytes = bom(file);
                args[1] = Utils.calculateMD5(utf8bytes);
            }catch(Exception e){
                e.printStackTrace();
                System.err.println("File open error.");
            }

        }
        try{
            String responseString = Request.Get(buildURI(args)).execute().returnContent().asString();
            responseString = new String(responseString.getBytes("iso-8859-1"), "utf-8");
            Map<String, Object> Response = (Map<String, Object>)objectMapper.readValue(responseString, Map.class);
            Map<String,String>result=(Map<String,String>)Response.get("result");
            System.out.println(star+"exists: "+result.get("exists"));
        }catch(Exception e){
            e.printStackTrace();
            System.err.println("Exists message receiving error.");
        }
    }

    /**
     * Upload a file or a batch of file to the database.
     *
     * <p> Receive the UPLOAD request with the file name or a directory.
     * If receive file directory, upload all the files to the database which are not binary files.
     * If the directory is empty, error message will output.
     * If recive file name, upload the file which has the name.
     * If the file is a binary file, error message will output.
     *
     * <pre> The return will be like:
     * upload: success/fail
     *
     * The return will be in Json like:
     * <pre>{
     *      code	-> 0 or	error code,
     *      message	->{} or error description,
     *      result	->{
     *          exists -> true or false
     *          }
     *     }
     *
     *
     * @param args
     *        {@code args[0]} contains the command, {@code args[1]} contains the file identification or name.
     * @throws IOException
     *         If {@code bom()} throws IOException.
     *         If The URI has error.{@code execute()} can't hold.
     *         If {@code objectMapper} can't map to the class.
     *
     */
    private static void handleUpload(String[] args) throws IOException {//file name
        File file=new File(args[1]);
        String md5;
        String responseString;
        byte[]utf8bytes;

        if(!file.exists()){ //check if the file exist
            System.err.println("File provided does not exist or can't read.");
            return;
        }
        if(file.isDirectory()){ //read in files under the directory
            ArrayList<File> fileList=getList(file);
            if(fileList!=null){
                for(File subFile:fileList){
                    utf8bytes=bom(subFile);
                    md5=Utils.calculateMD5(utf8bytes);
                    args[1]=md5;
                    responseString = Request.Post(buildURI(args)).bodyByteArray(utf8bytes).execute().returnContent().asString();
                    responseString = new String(responseString.getBytes("iso-8859-1"), "utf-8");
                    Map<String, Object> response = (Map<String, Object>)objectMapper.readValue(responseString, Map.class);
                    Map<String,String>result=(Map<String,String>)response.get("result");
                    Integer code = (Integer)response.get("code");
                    if(code==0){
                        System.out.println(star+subFile.getName()+" upload: "+result.get("upload"));
                    }else{
                        System.err.println(star+response.get("message"));
                    }
                }
            }else{
                System.err.println("The directory is empty.");
            }
        }else{
            if(!isText(file)){ //check if the text file
                System.err.println("File type is a binary file");
                return;
            }
            utf8bytes=bom(file); //drop the bom head
            md5=Utils.calculateMD5(utf8bytes);
            args[1]=md5;
            responseString = Request.Post(buildURI(args)).bodyByteArray(utf8bytes).execute().returnContent().asString();
            responseString = new String(responseString.getBytes("iso-8859-1"), "utf-8");
            Map<String, Object> response = (Map<String, Object>)objectMapper.readValue(responseString, Map.class);
            Map<String,String>result=(Map<String,String>)response.get("result");
            Integer code = (Integer)response.get("code");
            if(code==0){
                System.out.println(star+"upload: "+result.get("upload"));
            }else{
                System.err.println(star+response.get("message"));
            }
        }
    }

    /**
     * Return all the files under the directory which are not binary files.
     *
     * @param fileDirectory
     *        File directory the user send in.
     * @return A list of all the files under the directory which are not binary files.
     *
     */
    public static ArrayList<File> getList(File fileDirectory) {
        ArrayList<File> all=new ArrayList<>();
        File[] list=fileDirectory.listFiles();

        if(list==null){
            return all;
        }
        for(File file:list){
            if(file.canRead() && file.isDirectory()){
                getList(file);
            }else{
                if(isText(file)){
                    all.add(file);
                }
            }
        }
        return all;
    }

    /**
     * Download the file which has the md5 the user specified.
     * The user can choose his preferred file path where the file will be downloaded to.
     * If the user doesn't have his preferred file path, the file name will be default name, md5.txt.
     *
     * The result will be like:
     * download successfully:
     * file name:
     * preview:
     * path:
     *
     * @param args
     * @throws IOException
     *          If {@code bom()} throws IOException.
     *          If The URI has error.{@code execute()} can't hold.
     *          If {@code objectMapper} can't map to the class.
     *
     */
    private static void handleDownload(String[] args) throws IOException {//md5
        String responseString;
        Scanner input=new Scanner(System.in);
        System.out.println("Input your preferred file path: (or type \"no\")");
        String path=URLEncoder.encode(input.next(),"UTF-8");

        if(!path.equals("no")){
            responseString = Request.Get(buildURI(args)).setHeader("path",path).execute().returnContent().asString();
        }else{
            responseString = Request.Get(buildURI(args)).execute().returnContent().asString();
        }
        //responseString = new String(responseString.getBytes("iso-8859-1"), "utf-8");
        Map<String, Object> response = (Map<String, Object>)objectMapper.readValue(responseString, Map.class);
        Map<String,String>result=(Map<String,String>)response.get("result");
        Integer code = (Integer)response.get("code");
        if(code==0){
            System.out.println(star+"download successfully: \nfile name: "+result.get("name")+"\npreview: "+result.get("preview")+"\npath: "+result.get("path"));
        }else{
            System.err.println(star+response.get("message"));
        }
    }

    /**
     * Compare the two files the user specified and return the similarity of the files.
     *
     * Return two kinds of similarity. First is the simple distance and the second is Levenshtein distance.
     * <pre>The output will be like:
     * Simple similarity: 0.333
     * Levenshtein distance: 4
     *
     * @param args
     * @throws IOException
     *          If {@code bom()} throws IOException.
     *          If The URI has error.{@code execute()} can't hold.
     *          If {@code objectMapper} can't map to the class.
     */
    private static void handleCompare(String[] args) throws IOException {//md5
        String responseString = Request.Get(buildURI(args)).execute().returnContent().asString();

        Map<String, Object> response = (Map<String, Object>)objectMapper.readValue(responseString, Map.class);
        Map<String,String>result=(Map<String,String>)response.get("result");
        Integer code = (Integer)response.get("code");
        if(code==0){
            System.out.println(star+"Simple similarity: "+result.get("simple_similarity"));
            System.out.println(star+"Levenshtein distance: "+result.get("Levenshtein_distance"));
        }else{
            System.err.println((String)response.get("message"));
        }

    }


    /**
     * Request to present all the files in the database.
     *
     * <pre>The output will be like:
     * Total files:
     * md5:
     * length:
     * preview:
     *
     * @throws IOException
     *          If {@code bom()} throws IOException.
     *          If The URI has error.{@code execute()} can't hold.
     *          If {@code objectMapper} can't map to the class.
     */
    private static void handleList() throws IOException{
        String responseString = Request.Get(endpoint).execute().returnContent().asString();

        Map<String, Object> response = (Map<String, Object>)objectMapper.readValue(responseString, Map.class);
        Map<String,Object> result=(Map<String,Object>)response.get("result");
        List<Map<String,Object>> list=(List<Map<String, Object>>) result.get("files");
        System.out.println(star+"Total files: "+list.size());
        for(Map<String,Object> file: list){
            System.out.println("md5: "+file.get("md5")+"\nlength: "+file.get("length").toString()+"\npreview: "+file.get("preview")+"\n");
        }
    }

    /**
     * Delete the files in the database.
     *
     * The output will be like:
     * delete: successfully
     *
     * @param args
     *          User's input.
     * @throws IOException
     *          If {@code bom()} throws IOException.
     *          If The URI has error.{@code execute()} can't hold.
     *          If {@code objectMapper} can't map to the class.
     */
    private static void handleDelete(String[] args)throws IOException{
        String responseString=Request.Get(buildURI(args)).execute().returnContent().asString();
        Map<String, Object> response = (Map<String, Object>)objectMapper.readValue(responseString, Map.class);
        Map<String,String>result=(Map<String,String>)response.get("result");
        Integer code = (Integer)response.get("code");
        if(code==0){
            System.out.println(star+"delete: "+result.get("delete"));
        }else{
            System.err.println((String)response.get("message"));
        }

    }

    /**
     *
     * Get help text for users.
     *
     */
    private static void handleHelp(){
        System.out.println("exists md5\t\t        Check if the file exists in the database.\n" +
                "upload md5\t\t        Upload a txt file to the database.\n" +
                "download md5\t\t    Download a txt file from the database.\n" +
                "compare md51 md52\t\tCompare two files in the database for similarity.\n" +
                "list\t\t\t        List all the files in the database.\n" +
                "help\t\t\t        Show help txt.\n" +
                "delete md5/all\t\t    Delete (all) the file.\n");
    }

    /**
     * Return the byte array of the file content and drop the BOM head if the file has.
     *
     * @param file
     *          File need to drop BOM head.
     * @return The byte array which contains the content of the file.
     * @throws IOException
     *          If file can't open, byte reading will fail.
     *
     */
    private static byte[] bom(File file)throws IOException{//getString(),readAllBytes()
        byte[] bytes=Files.readAllBytes(file.toPath());//get bytes
        CharsetDetector detector=new CharsetDetector();
        detector.setText(bytes);
        CharsetMatch match=detector.detect();//get the most likely encoding.
        //convert the bytes to string using original encoding and convert to the utf8 bytes using utf8 encoding
        byte[] utf8bytes=match.getString().getBytes(StandardCharsets.UTF_8);

        boolean withBom=false;
        if(utf8bytes.length>=3 && utf8bytes[0]==-17 && utf8bytes[1]==-69 && utf8bytes[2]==-65){
            withBom=true;
            byte[]tmp=new byte[utf8bytes.length-3];
            System.arraycopy(utf8bytes,3,tmp,0,tmp.length);
            utf8bytes=tmp;
        }
        if(withBom){
            System.out.println(star+"detect charset: "+match.getName()+"-BOM");
        }else{
            System.out.println(star+"detect charset: "+match.getName());
        }
        return utf8bytes;
    }

    /**
     * Return URI.
     *
     * @param args
     *          User's input.
     * @return The URI.
     *
     */
    public static String buildURI(String[] args){
        String uri="";
        switch(args[0].toLowerCase()){
            case "exists":///ﬁles/:md5/exists
                uri="/"+args[1]+"/exists";
                break;
            case "upload":///ﬁles/:md5
            case "download":
                uri= "/"+args[1];
                break;
            case "compare":///ﬁles/:md51/compare/:md52
                uri= "/"+args[1]+"/compare/"+args[2];
                break;
            case "delete":
                uri="/"+args[1]+"/delete";
                break;
        }
        return endpoint+uri;
    }

    /**
     * Return <code>true</code> if the file is a text file. <code>false</code> if binary file.
     *
     * @param file
     * @return To check whether the file is text file or binary file.
     *
     */
    public static boolean isText(File file) {
        boolean isText = true;
        try {
            FileInputStream fin = new FileInputStream(file);
            long len = file.length();
            for (int j = 0; j < (int) len; j++) {
                int t = fin.read();
                if (t < 32 && t != 9 && t != 10 && t != 13) {
                    isText = false;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isText;
    }

    /**
     * Return guide for user if user input the nonsense command.
     *
     */
    private static void printUsage() {
        System.out.println("Usage: [op] [params]");
        System.out.println("Available Operation: upload, download, compare, exists, list, delete, help");
    }

    /**
     * Return md5 code for the file.
     *
     * @param bytes
     * File content bytes.
     * @return The md5 code for the file.
     *
     */
    // source: https://www.baeldung.com/java-md5
    public static String calculateMD5(byte[] bytes) {
        String myHash = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            byte[] digest = md.digest();
            myHash = DatatypeConverter
                    .printHexBinary(digest).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return myHash;
    }
}
