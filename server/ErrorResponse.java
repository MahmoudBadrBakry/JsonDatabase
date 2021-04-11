package server;

public class ErrorResponse extends Response {

    private String reason;
    
    public ErrorResponse(String reason) {
        super("ERROR");
        this.reason = reason;
    }

}
