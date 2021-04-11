package server;

import com.google.gson.JsonElement;

public class SetCommand implements ICommand {

    private JsonElement key;
    private JsonElement value;
    private Response response;
        
    
    public SetCommand(JsonElement key, JsonElement value) {
        super();
        this.key = key;
        this.value = value;
    }

    

    @Override
    public boolean execute(Database database) {
        database.setData(key, value);
        response = new Response("OK");
        return true;
    }



    @Override
    public Response getResponse() {
        return response;
    }

}
