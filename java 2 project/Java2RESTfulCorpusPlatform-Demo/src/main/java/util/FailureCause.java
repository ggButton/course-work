package util;

/**
 * Enumeration of failure cause.
 *
 * @author Gao Manlin
 * @since JDK1.8
 *
 */
public enum FailureCause{
    /**
     * File not in the database
     */
    FILE_NOT_FOUND(1, "File required is not found in database"),
    /**
     * Hash is not the same as provided.
     */
    HASH_NOT_MATCH(2, "hash not match"),
    /**
     * File has already existed.
     */
    ALREADY_EXIST(3, "File with the same md5 already exists"),
    /**
     * File has set up.
     */
    FILE_SET_DUPLICATE(4,"File ask to set duplicate");

    /**
     * {@code code} and {@code message}
     */
    int code;
    String message;

    /**
     * Constructor
     */
    FailureCause(int code, String message) {
        this.code = code;
        this.message = message;
    }
}