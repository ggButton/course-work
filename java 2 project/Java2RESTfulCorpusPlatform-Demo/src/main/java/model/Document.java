package model;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * A class stores the information of a file, including its md5 code, content of the file, length, preview(first 100words).
 *
 * @author Gao Manlin
 * @since JDK1.8
 *
 */
public class Document {
    /**
     * File's md5 code, content of the file, length of the content, preview(first 100words).
     */
    String md5;
    String content;
    int length;
    String preview;

    /**
     * Default constructor.
     */
    public Document(){}
    /**
     * Constructor.
     * @param md5,length,preview
     * Construct with md5,length,preview.
     */
    public Document(String md5, int length, String preview){
        this.md5=md5;
        this.length=length;
        this.preview=preview;
    }

    /**
     * Constructor.
     * @param md5,content,length
     * Construct with md5,content,length.
     */
    public Document(String md5, String content, int length){
        this.md5=md5;
        this.content=content;
        this.length=length;
        if(length>100){
            preview=content.substring(0,101);
        }else{
            preview=content;
        }
    }

    /**
     * Get md5.
     */
    public String getMd5(){return md5;}
    /**
     * Get content.
     */
    public String getContent(){return content;}
    /**
     * Get length.
     */
    public int getLength(){return length;}
    /**
     * Get preview.
     */
    public String getPreview(){return preview;}

    /**
     * Override the toString method.
     */
    @Override
    public String toString(){
        return "files:{\n" +
                "md5: "+md5+"\n"+
                "length: "+length+"\n"+
                "preview: "+preview+"\n"+
                "}";
    }



}
