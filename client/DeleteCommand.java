package client;


public class DeleteCommand implements IJsonCommand {
    private final String type = "delete";
    private String key;
    
    public DeleteCommand(String key) {
        this.key = key;
    }
    
}
