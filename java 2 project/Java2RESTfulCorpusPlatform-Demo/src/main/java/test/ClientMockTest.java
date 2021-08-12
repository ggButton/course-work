package test;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.fluent.Request;
import org.apache.tika.parser.txt.CharsetDetector;
import org.apache.tika.parser.txt.CharsetMatch;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import util.Utils;


import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.*;



//Need to delete the database before do the test!!!!!!
//Need to delete the database before do the test!!!!!!
//Need to delete the database before do the test!!!!!!


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClientMockTest {
    static String endpoint = "http://localhost:7001/files";
    static ObjectMapper objectMapper = new ObjectMapper();
    static ArrayList<String> md;
    static ArrayList<byte[]> by;


    @BeforeClass
    public static void init(){
        try{
            md=new ArrayList<>();
            by=new ArrayList<>();
            String responseString = Request.Get("http://localhost:7001/").execute().returnContent().asString();
            System.out.println(responseString);

            File file = new File("test4.txt");
            byte[] utf8bytes = bom(file);
            String md5 = Utils.calculateMD5(utf8bytes);
            md.add(md5);
            by.add(utf8bytes);

            file = new File("co2.txt");
            utf8bytes = bom(file);
            md5 = Utils.calculateMD5(utf8bytes);
            md.add(md5);
            by.add(utf8bytes);

            file = new File("co1.txt");
            utf8bytes = bom(file);
            md5 = Utils.calculateMD5(utf8bytes);
            md.add(md5);
            by.add(utf8bytes);

            /*file = new File("8.txt");
            utf8bytes = bom(file);
            md5 = Utils.calculateMD5(utf8bytes);
            md.add(md5);
            by.add(utf8bytes);*/
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void test01_uploadTest(){
        try{
            //upload success

            String s = Request.Post(endpoint + "/" + md.get(0)).bodyByteArray(by.get(0)).execute().returnContent().asString();
            Map<String, Object> Response = (Map<String, Object>) objectMapper.readValue(s, Map.class);
            Map<String, String> result = (Map<String, String>) Response.get("result");
            assertEquals(0,Response.get("code"));
            assertEquals("",Response.get("message"));
            assertEquals("success", result.get("upload"));


            s = Request.Post(endpoint + "/" + md.get(1)).bodyByteArray(by.get(1)).execute().returnContent().asString();
            Response = (Map<String, Object>) objectMapper.readValue(s, Map.class);
            result = (Map<String, String>) Response.get("result");
            assertEquals("success",result.get("upload"));


            s = Request.Post(endpoint + "/" + md.get(2) ).bodyByteArray(by.get(2)).execute().returnContent().asString();
            Response = (Map<String, Object>) objectMapper.readValue(s, Map.class);
            result = (Map<String, String>) Response.get("result");
            assertEquals("success",result.get("upload"));

            //not success
            s = Request.Post(endpoint + "/" + md.get(2) + "333").bodyByteArray(by.get(2)).execute().returnContent().asString();
            Response = (Map<String, Object>) objectMapper.readValue(s, Map.class);
            result = (Map<String, String>) Response.get("result");
            assertEquals(2,Response.get("code"));
            assertEquals("hash not match",Response.get("message"));
            assertEquals("fail",result.get("upload"));

            s = Request.Post(endpoint + "/" + md.get(2)).bodyByteArray(by.get(2)).execute().returnContent().asString();
            Response = (Map<String, Object>) objectMapper.readValue(s, Map.class);
            result = (Map<String, String>) Response.get("result");
            assertEquals(3,Response.get("code"));
            assertEquals("File with the same md5 already exists",Response.get("message"));
            assertEquals("fail",result.get("upload"));

            /*s = Request.Post(endpoint + "/" + md.get(3)).bodyByteArray(by.get(3)).execute().returnContent().asString();
            Response = (Map<String, Object>) objectMapper.readValue(s, Map.class);
            result = (Map<String, String>) Response.get("result");
            assertEquals(result.get("upload"), "fail");*/


        }catch(Exception e){}

    }

    @Test
    public void test02_existsTest() {
        try {
            //exists
            File file = new File("test4.txt");
            byte[] utf8bytes = bom(file);
            String md5 = Utils.calculateMD5(utf8bytes);
            String s = Request.Get(endpoint + "/" + md5 + "/exists").execute().returnContent().asString();
            Map<String, Object> Response = (Map<String, Object>) objectMapper.readValue(s, Map.class);
            Map<String, String> result = (Map<String, String>) Response.get("result");
            assertEquals(0,Response.get("code"));
            assertEquals("",Response.get("message"));
            assertEquals("true",result.get("exists"));

            //not exists
            /*file = new File("co3.txt");
            utf8bytes = bom(file);
            md5 = Utils.calculateMD5(utf8bytes);
            s = Request.Get(endpoint + "/" + md5 + "/exists").execute().returnContent().asString();
            Response = (Map<String, Object>) objectMapper.readValue(s, Map.class);
            result = (Map<String, String>) Response.get("result");
            assertEquals(result.get("exists"), "false");*/

        } catch (Exception e) {}//bom()
    }

    @Test
    public void test03_downloadTest()throws Exception{
        Request.Get(endpoint + "/" +md.get(0)).execute().returnContent().asString();
        File file=new File(md.get(0)+".txt");
        byte[] bytes= Files.readAllBytes(file.toPath());
        assertEquals("UTF-8",guessEncoding(bytes));
        boolean isStringSame=Arrays.equals(by.get(0), bytes);
        assertEquals(true, isStringSame);

        String s=Request.Get(endpoint + "/" +md.get(0)).execute().returnContent().asString();
        Map<String, Object> Response = (Map<String, Object>) objectMapper.readValue(s, Map.class);
        Map<String, String> result = (Map<String, String>) Response.get("result");
        assertEquals(4,Response.get("code"));
        assertEquals("File ask to set duplicate",Response.get("message"));

        s=Request.Get(endpoint + "/3333").execute().returnContent().asString();
        Response = (Map<String, Object>) objectMapper.readValue(s, Map.class);
        result = (Map<String, String>) Response.get("result");
        assertEquals(1,Response.get("code"));
        assertEquals("File required is not found in database",Response.get("message"));

    }

    @Test
    public void test04_compareTest()throws Exception{
        String s=Request.Get(endpoint + "/" +md.get(1)+"/compare/"+md.get(2)).execute().returnContent().asString();
        Map<String, Object> Response = (Map<String, Object>) objectMapper.readValue(s, Map.class);
        Map<String, String> result = (Map<String, String>) Response.get("result");
        assertEquals("0.556",result.get("simple_similarity"));
        assertEquals("4",result.get("Levenshtein_distance"));

        s=Request.Get(endpoint + "/333/compare/"+md.get(2)).execute().returnContent().asString();
        Response = (Map<String, Object>) objectMapper.readValue(s, Map.class);
        result = (Map<String, String>) Response.get("result");
        assertEquals(1,Response.get("code"));
        assertEquals("File required is not found in database",Response.get("message"));
    }


    private static byte[] bom(File file)throws IOException {
        byte[] bytes= Files.readAllBytes(file.toPath());//get bytes
        CharsetDetector detector=new CharsetDetector();
        detector.setText(bytes);
        CharsetMatch match=detector.detect();//get the most likely encoding.
        //convert the bytes to string using original encoding and convert to the utf8 bytes using utf8 encoding
        byte[] utf8bytes=match.getString().getBytes(StandardCharsets.UTF_8);

        //boolean withBom=false;
        if(utf8bytes.length>=3 && utf8bytes[0]==-17 && utf8bytes[1]==-69 && utf8bytes[2]==-65){
            //withBom=true;
            byte[]tmp=new byte[utf8bytes.length-3];
            System.arraycopy(utf8bytes,3,tmp,0,tmp.length);
            utf8bytes=tmp;
        }
        /*if(withBom){
            System.out.println(star+"detect charset: "+match.getName()+"-BOM");
        }else{
            System.out.println(star+"detect charset: "+match.getName());
        }*/
        return utf8bytes;
    }

    //from https://blog.csdn.net/henryzhang2009/article/details/43703955?utm_source=blogxgwz3
    public static String guessEncoding(byte[] bytes) {
        String DEFAULT_ENCODING = "UTF-8";
        org.mozilla.universalchardet.UniversalDetector detector =
                new org.mozilla.universalchardet.UniversalDetector(null);
        detector.handleData(bytes, 0, bytes.length);
        detector.dataEnd();
        String encoding = detector.getDetectedCharset();
        detector.reset();
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }
        return encoding;
    }
}
