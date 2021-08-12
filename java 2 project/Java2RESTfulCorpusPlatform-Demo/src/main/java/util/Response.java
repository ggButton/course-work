package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * A class to format the response.
 *
 * @author Gao Manlin
 * @since JDK1.8
 *
 */
public class Response {
    /**
     * <code>code</code> is the state. <code>message</code> is hint. <code>result</code> presents the result.
     */
    int code;
    String message;
    ObjectNode result;

    /**
     * A mapper.
     */
    static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Constructor.
     * @param code,message
     * State and the error message.
     */
    public Response(int code, String message) {
        this.code = code;
        this.message = message;
        this.result = objectMapper.createObjectNode();
    }

    /**
     * Constructor.
     * @param code,message,result
     * Construct the Response with code,message and result.
     */
    public Response(int code, String message, ObjectNode result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }
    /**
     * @return Return the code.
     */
    public int getCode() {
        return code;
    }
    /**
     * @return Return the message.
     */
    public String getMessage() {
        return message;
    }
    /**
     * @return Return the result.
     */
    public ObjectNode getResult() {
        return result;
    }
    /**
     * Set the code.
     */
    public void setCode(int code) {
        this.code = code;
    }
    /**
     * Set the message.
     */
    public void setMessage(String message) {
        this.message = message;
    }
    /**
     * Set the result.
     */
    public void setResult(ObjectNode result) {
        this.result = result;
    }
    /**
     * Set the objectMapper.
     */
    public static void setObjectMapper(ObjectMapper objectMapper) {
        Response.objectMapper = objectMapper;
    }
}
