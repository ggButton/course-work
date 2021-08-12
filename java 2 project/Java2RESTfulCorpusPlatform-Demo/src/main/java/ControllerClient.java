import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import org.apache.http.client.fluent.Request;
import org.apache.tika.parser.txt.CharsetDetector;
import org.apache.tika.parser.txt.CharsetMatch;
import util.Utils;
import javafx.scene.Node;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Controller of the Window.fxml. Handle the exists, upload, download, compare, list, delete, help requests.
 *
 * @author Gao Manlin
 * @since 1.8
 *
 */
public class ControllerClient {
    /**
     * Class own Main object. To get connected with Main class.
     */
    private Main main;
    /**
     * Endpoint of the html request.
     */
    static String endpoint = "http://localhost:7001/files";
    /**
     * Star marks the response from the program.
     */
    static String star="★";
    /**
     * Hollow star marks the error message.
     */
    static String er="☆";
    /**
     * A mapper to map the string to specific class.
     */
    static ObjectMapper objectMapper = new ObjectMapper();
    /**
     * A String array to contain the command from users.
     */
    static String[]args;

    @FXML
    Button exist;
    @FXML
    Button upload;
    @FXML
    Button download;
    @FXML
    Button compare;
    @FXML
    Button list;
    @FXML
    Button delete;
    @FXML
    Button help;
    @FXML
    TextArea output;
    @FXML
    TextArea input;
    @FXML
    Label outputLabel;
    @FXML
    Label inputLabel;


    /**
     * Called when exist button is pressed. If text area is empty, print "exists".
     */
    @FXML
    public void existButton(){
        if(!input.getText().equals("exists ")){
            input.setText("exists ");
        }
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(main.getPrimaryStage());
        if(file!=null){
            input.appendText(file.getPath()+" ");
        }
    }

    /**
     * Called when upload button is pressed. If text area is empty, print "upload".
     */
    @FXML
    public void uploadButton(){
        if(!input.getText().equals("upload ")){
            input.setText("upload ");
        }
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(main.getPrimaryStage());
        if(file!=null){
            input.appendText(file.getPath()+" ");
        }
    }

    /**
     * Called when download button is pressed. If text area is empty, print "download".
     */
    @FXML
    public void downloadButton(){
        if(!input.getText().equals("download ")){
            input.setText("download ");
        }
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "Text files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(main.getPrimaryStage());
        if(file!=null){
            input.appendText(file.getAbsolutePath()+" ");
        }
    }

    /**
     * Called when compare button is pressed. If text area is empty, print "compare".
     */
    @FXML
    public void compareButton(){
        if(!input.getText().equals("compare ")){
            input.setText("compare ");
        }
    }

    /**
     * Called when list button is pressed.
     */
    @FXML
    public void listButton(){
        try{
            handleList();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Called when delete button is pressed. If text area is empty, print "delete".
     */
    @FXML
    public void deleteButton(){
        if(!input.getText().equals("delete ")){
            input.setText("delete ");
        }
    }
    /**
     * Called when help button is pressed.
     */
    @FXML
    public void helpButton(){
        handleHelp();
    }

    /**
     * Called hen user press enter key in the input text area.
     *
     */
    @FXML
    public void sendByKey(){
        input.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER)  {
                    // do operation after the enter key is pressed
                    args=input.getText().split(" ");
                    try{
                        switch (args[0]){
                            case "exists":
                                handleExists(args);
                                break;
                            case "upload":
                                handleUpload(args);
                                break;
                            case "download":
                                handleDownload(args);
                                break;
                            case "compare":
                                handleCompare(args);
                                break;
                            case "delete":
                                handleDelete(args);
                                break;
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    // clear text
                    input.setText("");
                }
            }
        });
    }

    /**
     *
     * @param main
     *      The object of Main from the outside to set the main.
     *
     */
    public void setMain(Main main){
        this.main=main;
    }

    private void handleExists(String[] args){//md5 or name
        File file=new File(args[1]);
        byte[] utf8bytes;
        if(file.isDirectory()){
            output.appendText(er+"File provided is a directory.\n");
            return;
        }
        if(file.exists()) {
            if(!Client.isText(file)){
                output.appendText(er+"File provided is a binary file.\n");
                return;
            }
            try{
                utf8bytes = bom(file);
                args[1] = Utils.calculateMD5(utf8bytes);
            }catch(Exception e){
                e.printStackTrace();
                output.appendText(er+"File open error.\n");
            }

        }
        try{
            String responseString = Request.Get(Client.buildURI(args)).execute().returnContent().asString();

            Map<String, Object> Response = (Map<String, Object>)objectMapper.readValue(responseString, Map.class);
            Map<String,String>result=(Map<String,String>)Response.get("result");
            output.appendText(star+"exists: "+result.get("exists")+"\n");
        }catch(Exception e){
            e.printStackTrace();
            output.appendText(er+"Exists message receiving error.\n");
        }
    }

    private void handleUpload(String[] args) throws IOException {//file name
        File file=new File(args[1]);
        String md5;
        String responseString;
        byte[]utf8bytes;

        if(!file.exists()){ //check if the file exist
            output.appendText(er+"File provided does not exist or can't read.\n");
            return;
        }
        if(file.isDirectory()){
            ArrayList<File> fileList=Client.getList(file);
            if(fileList!=null){
                for(File subFile:fileList){
                    utf8bytes=bom(subFile);
                    md5=Utils.calculateMD5(utf8bytes);
                    args[1]=md5;
                    responseString = Request.Post(Client.buildURI(args)).bodyByteArray(utf8bytes).execute().returnContent().asString();
                    responseString = new String(responseString.getBytes("iso-8859-1"), "utf-8");
                    Map<String, Object> response = (Map<String, Object>)objectMapper.readValue(responseString, Map.class);
                    Map<String,String>result=(Map<String,String>)response.get("result");
                    Integer code = (Integer)response.get("code");
                    if(code==0){
                        output.appendText(star+subFile.getName()+" upload: "+result.get("upload")+"\n");
                    }else{
                        output.appendText(er+response.get("message")+"\n");
                    }
                }
            }else{
                output.appendText(er+"The directory is empty.\n");
            }
        }else{
            if(!Client.isText(file)){ //check if the text file
                output.appendText(er+"File type is a binary file.\n");
                return;
            }
            utf8bytes=bom(file); //drop the bom head
            md5=Utils.calculateMD5(utf8bytes);
            args[1]=md5;
            responseString = Request.Post(Client.buildURI(args)).bodyByteArray(utf8bytes).execute().returnContent().asString();
            Map<String, Object> response = (Map<String, Object>)objectMapper.readValue(responseString, Map.class);
            Map<String,String>result=(Map<String,String>)response.get("result");
            Integer code = (Integer)response.get("code");
            if(code==0){
                output.appendText(star+"upload: "+result.get("upload")+"\n");
            }else{
                output.appendText(er+response.get("message")+"\n");
            }
        }
    }

    private void handleDownload(String[] args) throws IOException {//md5
        String responseString;
        args[1]= URLEncoder.encode(args[1],"UTF-8");

        if(args.length==3){ //args: download (path) md5, args[2] is md5, can't use Client.buildURI().
            responseString = Request.Get(endpoint+"/"+args[2]).setHeader("path",args[1]).execute().returnContent().asString();
        }else{
            responseString = Request.Get(endpoint+"/"+args[1]).execute().returnContent().asString();
        }

        Map<String, Object> response = (Map<String, Object>)objectMapper.readValue(responseString, Map.class);
        Map<String,String>result=(Map<String,String>)response.get("result");
        Integer code = (Integer)response.get("code");
        if(code==0){
            output.appendText(star+"download successfully: \nfile name: "+result.get("name")+"\npreview: "+result.get("preview")+"\npath: "+result.get("path")+"\n");
        }else{
            output.appendText(er+response.get("message")+"\n");
        }
    }

    private void handleCompare(String[] args) throws IOException {//md5
        String responseString = Request.Get(Client.buildURI(args)).execute().returnContent().asString();

        Map<String, Object> response = (Map<String, Object>)objectMapper.readValue(responseString, Map.class);
        Map<String,String>result=(Map<String,String>)response.get("result");
        Integer code = (Integer)response.get("code");
        if(code==0){
            output.appendText(star+"Simple similarity: "+result.get("simple_similarity")+"\n");
            output.appendText(star+"Levenshtein distance: "+result.get("Levenshtein_distance")+"\n");
        }else{
            output.appendText(er+(String)response.get("message")+"\n");
        }
    }

    private void handleList() throws IOException{
        String responseString = Request.Get(endpoint).execute().returnContent().asString();

        Map<String, Object> response = (Map<String, Object>)objectMapper.readValue(responseString, Map.class);
        Map<String,Object> result=(Map<String,Object>)response.get("result");
        List<Map<String,Object>> list=(List<Map<String, Object>>) result.get("files");
        output.appendText(star+"Total files: "+list.size()+"\n");
        for(Map<String,Object> file: list){
            output.appendText("md5: "+file.get("md5")+"\nlength: "+file.get("length").toString()+"\npreview: "+file.get("preview")+"\n");
            output.appendText("\n");
        }
    }

    private void handleDelete(String[] args)throws IOException{
        String responseString=Request.Get(Client.buildURI(args)).execute().returnContent().asString();
        Map<String, Object> response = (Map<String, Object>)objectMapper.readValue(responseString, Map.class);
        Map<String,String>result=(Map<String,String>)response.get("result");
        Integer code = (Integer)response.get("code");
        if(code==0){
            output.appendText(star+"delete: "+result.get("delete")+"\n");
        }else{
            output.appendText(er+response.get("message")+"\n");
        }
    }

    private void handleHelp(){
        output.appendText(star+"Help:\n");
        output.appendText("exists md5\t\t        Check if the file exists in the database.\n" +
                "upload md5\t\t        Upload a txt file to the database.\n" +
                "download md5\t\t    Download a txt file from the database.\n" +
                "compare md51 md52\t\tCompare two files in the database for similarity.\n" +
                "list\t\t\t        List all the files in the database.\n" +
                "help\t\t\t        Show help txt.\n" +
                "delete md5/all\t\t    Delete (all) the file.\n");
    }


    private byte[] bom(File file)throws IOException{//getString(),readAllBytes()
        byte[] bytes= Files.readAllBytes(file.toPath());//get bytes
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
            output.appendText(star+"detect charset: "+match.getName()+"-BOM\n");
        }else{
            output.appendText(star+"detect charset: "+match.getName()+"\n");
        }
        return utf8bytes;
    }

}
