package server;

import com.google.gson.JsonElement;

public class GetCommand implements ICommand {

    private JsonElement key;
    private JsonElement value;
    
    
    public GetCommand(JsonElement key) {
        super();
        this.key = key;
    }


    @Override
    public boolean execute(Database database) throws RuntimeException {
        value = database.get(key);
        return true;
    }


    @Override
    public Response getResponse() {
        return new DataResponse(value);
    }

}
