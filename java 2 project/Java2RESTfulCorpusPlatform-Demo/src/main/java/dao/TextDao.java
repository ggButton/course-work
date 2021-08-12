package dao;

import model.Document;
import org.apache.poi.ss.formula.functions.T;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

/**
 * This class interact with the database. Basically get information and execute operations using SQLite language.
 *
 * @author Gao Manlin
 * @since JDK1.8
 *
 */
public class TextDao {
    /**
     * Sql2o object.
     */
    Sql2o sql2o;

    /**
     * Constructor.
     */
    public TextDao(Sql2o sql2o){
        this.sql2o=sql2o;
    }

    /**
     * Default Constructor.
     */
    public TextDao(){}

    /**
     * Check whether the file exists.
     *
     * @param md5
     *        File identification.
     * @return <code>true</code> if md5 exists in database. <code>false</code> if md5 doesn't exist in database.
     *
     */
    public boolean isExist(String md5){
        String selectSQL="select md5,content,length,preview from files where md5='"+md5+"'";
        try(Connection con=sql2o.open()){
            List<Document> fileList=null;
            fileList=con.createQuery(selectSQL).executeAndFetch(Document.class);
            return fileList.size()!=0;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Upload file.
     *
     * @param document
     *          Contain the information of the file to upload.
     * @return <code>true</code> if upload successfully. <code>false</code> if fail.
     *
     */
    public boolean upload(Document document){
        String insertSQL="insert into files (md5, content, length, preview) values (:md5, :content, :length, :preview)";
        try(Connection con=sql2o.open()){
            con.createQuery(insertSQL)
                    .addParameter("md5", document.getMd5())
                    .addParameter("content", document.getContent())
                    .addParameter("length", document.getLength())
                    .addParameter("preview", document.getPreview())
                    .executeUpdate();
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Fetch the information of the specified file with the md5.
     *
     * @param md5
     *          File identification.
     * @return Information of the specified file with the md5.
     *
     */
    public Document fetchDocument(String md5){
        String selectSQL="select md5,content,length,preview from files where md5='"+md5+"'";
        try(Connection con=sql2o.open()){
            List<Document> fileList;
            fileList=con.createQuery(selectSQL).executeAndFetch(Document.class);
            return fileList.get(0);
        }
    }

    /**
     * Get information of all the files in the database.
     *
     * @return Information of all the files in the database.
     *
     */
    public List<Document> getFileList(){
        String selectSQL="select md5,length,preview from files";//here the content will be null, because we do not fetch content here
        List<Document> fileList;
        try(Connection con=sql2o.open()){
            fileList=con.createQuery(selectSQL).executeAndFetch(Document.class);
        }
        return fileList;
    }

    /**
     * Delete the specified file with the same md5.
     *
     * @param md5
     *          File identification.
     * @return <code>true</code> if delete successfully. <code>false</code> if fails.
     *
     */
    public boolean delete(String md5){
        if (isExist(md5)) {
            try(Connection con=sql2o.open()){
                con.createQuery("delete from files where md5=:md5")
                        .addParameter("md5", md5)
                        .executeUpdate();
            }
            return true;
        }else{
            return false;
        }
    }

    /**
     * Delete all the files in the database.
     *
     */
    public void deleteAll(){
        try(Connection con=sql2o.open()){
            con.createQuery("delete from files").executeUpdate();
        }
    }

}
