package server;

import com.google.gson.JsonElement;

public class DeleteCommand implements ICommand {

    private JsonElement key;
//    private String value;
    private Response response;
    
    public DeleteCommand(JsonElement key) {
        this.key = key;
    }

    @Override
    public boolean execute(Database database) {
//        value = database.get(key);
        database.delete(key);
        response = new Response("OK");
        return true;
    }

    @Override
    public Response getResponse() {
        return response;
    }

}
