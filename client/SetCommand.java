package client;

public class SetCommand implements IJsonCommand {
    private final String type = "set";
    private String key;
    private String value;
    
    public SetCommand(String key, String value) {
        this.key = key;
        this.value = value;
    }
    
}
