package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.TextDao;
import io.javalin.http.Context;
import model.Document;
import util.*;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * This is the fundamental class to handle the requests. Methods includes {@link #handleExists(Context)}, {@link #handleUpload(Context)},
 * {@link #handleDownload(Context)}, {@link #handleCompare(Context)}, {@link #handleList(Context)}, {@link #handleDelete(Context)}.
 * Each method handles a request. Each of these methods are invoked in {@see #Server}.
 *
 * @author Gao Manlin
 * @since JDK1.8
 *
 */
public class TextService {

    /**
     * A {@see TextDao} object to interact with database.
     */
    TextDao dao;

    /**
     * TextService Constructor.
     * @param dao
     * TextDao object.
     */
    public TextService(TextDao dao) {
        this.dao = dao;
    }

    /**
     * Check whether the file user specified exists in the database.
     * If exists, report json format of {@code SuccessResponse}, else return {@code FailureResponse}.
     *
     * @param ctx
     *
     */
    public void handleExists(Context ctx){
        Response response;
        String md5 = ctx.pathParam("md5");
        if(dao.isExist(md5)){
            response = new SuccessResponse();
            response.getResult().put("exists","true");
        }else{
            response=new FailureResponse(FailureCause.FILE_NOT_FOUND);
            response.getResult().put("exists","false");
        }
        ctx.res.setCharacterEncoding("UTF-8");
        ctx.json(response);
    }

    /**
     * Upload the md5 and the content of the file to the database.
     * If md5 and the content don't match, report {@code FailureCause.HASH_NOT_MATCH}.
     * If md5 doesn't exist in the database, report {@code FailureCause.ALREADY_EXIST}.
     * If upload successfully, report {@code SuccessResponse}
     *
     * @param ctx
     *
     */
    public void handleUpload(Context ctx) {
        Response response;
        String md5 = ctx.pathParam("md5");
        String bodyString = ctx.body();
        boolean isUpload=false;
        if(!Utils.calculateMD5(bodyString).equals(md5)){
            response=new FailureResponse(FailureCause.HASH_NOT_MATCH);
        }else if(!dao.isExist(md5)){
            int length=bodyString.length();
            Document document=new Document(md5,bodyString,length);
            isUpload=dao.upload(document);
            response=new SuccessResponse();
        }else{
            response=new FailureResponse(FailureCause.ALREADY_EXIST);
        }
        response.getResult().put("upload",isUpload?"success":"fail");

        ctx.res.setCharacterEncoding("UTF-8");
        ctx.json(response);
    }

    /**
     * Download the file which has the specified md5 to the specified path.
     * If the md5 doesn't exist in the database, report {@cade FailureCause.FILE_NOT_FOUND}.
     * If the specified path has existed, report {@cade FailureCause.FILE_SET_DUPLICATE}.
     * The file will be written in UTF-8.
     *
     * @param ctx
     *
     */
    public void handleDownload(Context ctx) {
        Response response;
        String md5 = ctx.pathParam("md5");
        String path=ctx.header("path");

        File file;
        if (dao.isExist(md5)) {
            if (path!=null) {
                try{
                    path= URLDecoder.decode(path,"UTF-8");
                }catch(Exception e){
                    e.printStackTrace();
                    System.err.println("Unsupported encoding");
                }
                file = new File(path);
                File fileParent = file.getParentFile();
                if (fileParent != null && !fileParent.exists()) {
                    fileParent.mkdirs();
                }
                try{
                    boolean is=file.createNewFile();
                    System.out.println(is);
                    if(!is){
                        response = new FailureResponse(FailureCause.FILE_SET_DUPLICATE);
                        response.getResult().put("download","fail");
                        ctx.json(response);
                        return;
                    }
                }catch(IOException e){
                    e.printStackTrace();
                    System.err.println("File create fails.");
                    return;
                }
            } else {
                file = new File(md5 + ".txt");
                if(file.exists()){
                    response = new FailureResponse(FailureCause.FILE_SET_DUPLICATE);
                    response.getResult().put("download","fail");
                    ctx.json(response);
                    return;
                }
            }
            Document document = dao.fetchDocument(md5);
            try{
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
                bw.write(document.getContent());
                bw.flush();
                bw.close();
            }catch(IOException e){
                e.printStackTrace();
                System.err.println("File asked to write in fails.");
            }
            response = new SuccessResponse();
            response.getResult().put("name",file.getName());
            response.getResult().put("preview", document.getPreview());
            response.getResult().put("path", file.getAbsolutePath());
        } else {
            response = new FailureResponse(FailureCause.FILE_NOT_FOUND);
            response.getResult().put("download","fail");
        }

        ctx.res.setCharacterEncoding("UTF-8");
        ctx.json(response);
    }

    /**
     * Compare two files and calculate the similarity according to specified md5.
     * If md5 doesn't exist in the database, report {@code FailureCause.FILE_NOT_FOUND}.
     *
     *
     * @param ctx
     *
     */
    public void handleCompare(Context ctx){
        Response response;
        String md51 = ctx.pathParam("md51");
        String md52 = ctx.pathParam("md52");
        if(dao.isExist(md51) && dao.isExist(md52)){
            Document doc1=dao.fetchDocument(md51);
            Document doc2=dao.fetchDocument(md52);
            String similarity= Utils.simpleSimilarity(doc1,doc2);
            String Levenshtein_distance=Utils.LevenshteinDistance(doc1,doc2);
            response=new SuccessResponse();
            response.getResult().put("simple_similarity", similarity);
            response.getResult().put("Levenshtein_distance", Levenshtein_distance);
        }else{
            response=new FailureResponse(FailureCause.FILE_NOT_FOUND);
        }

        ctx.res.setCharacterEncoding("UTF-8");
        ctx.json(response);
    }

    /**
     * Get the information of all the files in the database.
     *
     * @param ctx
     *
     */
    public void handleList(Context ctx){
        Response response=new SuccessResponse();
        List<Document> list=dao.getFileList();

        response.getResult().set("files", new ObjectMapper().valueToTree(list));

        ctx.res.setCharacterEncoding("UTF-8");
        ctx.json(response);
    }

    /**
     * Delete the file in the database according to md5 or delete all the files.
     * If the md5 is valid, delete the specified file.
     * If the md5 is "all", delete all the files.
     * If the md5 is invalid and not "all", report {@code FailureCause.FILE_NOT_FOUND}.
     * @param ctx
     *
     */
    public void handleDelete(Context ctx){
        Response response;
        String md5 = ctx.pathParam("md5");
        if(md5.equals("all")){
            dao.deleteAll();
            response=new SuccessResponse();
            response.getResult().put("delete","successfully");
        }else{
            boolean isD=dao.delete(md5);
            if(isD){
                response=new SuccessResponse();
                response.getResult().put("delete","successfully");
            }else{
                response=new FailureResponse(FailureCause.FILE_NOT_FOUND);
            }
        }
        ctx.res.setCharacterEncoding("UTF-8");
        ctx.json(response);
    }

}
