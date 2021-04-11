package client;

import com.beust.jcommander.Parameter;

public class GetCommand implements IJsonCommand {
    @Parameter(names = "-t")
    private final String type = "get";
    
    @Parameter(names = "-k")
    private String key;
    
    
    public GetCommand() {
    }


    public GetCommand(String key) {
        this.key = key;
    }
    
}
