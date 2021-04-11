package server;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class DataResponse extends Response {

    private JsonElement value;
    public DataResponse(JsonElement value) {
        super("OK");
        this.value = value;
    }

}
