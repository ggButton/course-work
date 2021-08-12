package util;

import model.Document;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A class contains useful tools.
 *
 * @author Gao Manlin
 * @since JDK1.8
 *
 */
public class Utils {
    /**
     * Return md5 code for the file.
     *
     * @param bytes
     *          File content bytes.
     * @return The md5 code for the file.
     *
     */
    // source: https://www.baeldung.com/java-md5
    public static String calculateMD5(byte[] bytes){
        //TODO
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
    /**
     * Return md5 code for the file.
     *
     * @param str
     *          File content.
     * @return The md5 code for the file.
     *
     */
    public static String calculateMD5(String str){
        return calculateMD5(str.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Return simple similarity.
     *
     * @param doc1,doc2
     * Two files.
     * @return Simple similarity.
     *
     */
    public static String simpleSimilarity(Document doc1, Document doc2){
        int length=Math.max(doc1.getLength(), doc2.getLength());
        int less=Math.min(doc1.getLength(), doc2.getLength());

        double count=0;
        char[] d1=doc1.getContent().toCharArray();
        char[] d2=doc2.getContent().toCharArray();

        for(int i=0; i<less; i++){
            if(d1[i]==d2[i]){
                count++;
            }
        }
        double similarity=count/length;
        return String.format("%.3f",similarity);
    }

    /**
     * Return Levenshtein Distance.
     *
     * @param doc1,doc2
     *          The model of two files.
     * @return Levenshtein Distance
     *
     */
    public static String LevenshteinDistance(Document doc1, Document doc2){
        int m=doc1.getLength();
        int n=doc2.getLength();
        char[] str1=doc1.getContent().toCharArray();
        char[] str2=doc2.getContent().toCharArray();
        int[][] dis=new int[m][n];
        dis[0][0]=str1[0]==str2[0]? 0:1;
        for(int i=1; i<m; i++){
            dis[i][0]=i+dis[0][0];
        }
        for(int j=1; j<n; j++){
            dis[0][j]=j+dis[0][0];
        }
        int cost;
        for(int i=1; i<m; i++){
            for(int j=1; j<n; j++){
                cost=str1[i]==str2[j]? 0:1;
                dis[i][j]=min(dis[i-1][j]+1,dis[i][j-1]+1,dis[i-1][j-1]+cost);
            }
        }
        return String.valueOf(dis[m-1][n-1]);
    }

    /**
     * Return the minimal number of three.
     *
     * @param a,b,c
     *          Three numbers.
     * @return The minimal number among three.
     *
     */
    private static int min(int a, int b, int c){
        int d=a<b? a:b;
        return c<d? c:d;
    }
}
