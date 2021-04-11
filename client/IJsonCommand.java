package client;

import com.google.gson.Gson;

public interface IJsonCommand {
    public default String toJson() {
        return new Gson().toJson(this);
    }
}
