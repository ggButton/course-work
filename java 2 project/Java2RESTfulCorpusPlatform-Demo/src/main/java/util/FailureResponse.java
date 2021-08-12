package util;
/**
 *
 *
 * @author Gao Manlin
 * @since JDK1.8
 *
 */
public class FailureResponse extends Response {

    /**
     * Construct from the super.
     */
    public FailureResponse(int code, String message) {
        super(code, message);
    }

    /**
     * Constructor.
     * @param failureCause
     * Need failureCause.
     */
    public FailureResponse(FailureCause failureCause){
        super(failureCause.code, failureCause.message);
    }

}

