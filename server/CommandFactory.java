package server;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class CommandFactory {
    private String type;
    private JsonElement key;
    private JsonElement value;
    
//    public CommandFactory(String type, String key, String value) {
//        super();
//        this.type = type;
//        this.key = key;
//        this.value = value;
//    }
    
    public CommandFactory() {}

    public CommandFactory(JsonObject inputJson) {
        this.type = inputJson.get("type").getAsString();
        this.key = inputJson.get("key");
        this.value = inputJson.get("value");
    }

    public ICommand getCommand() {
        ICommand command = null;
        switch (type) {
        case "set":
            command = new SetCommand(key, value);
            break;
        case "get":
            command = new GetCommand(key);
            break;
        case "delete":
            command = new DeleteCommand(key);
            break;
        case "exit":
            command = new ExitCommand();
            break;
        default:
            throw new RuntimeException("No such command type");
        }       
        return command;
    }
}
