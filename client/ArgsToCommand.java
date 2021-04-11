package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import com.beust.jcommander.Parameter;
import com.google.gson.Gson;

public class ArgsToCommand {
    @Parameter(names = { "-t", "--type" })
    private String type;

    @Parameter(names = { "-k", "--key" })
    private String key;

    @Parameter(names = { "-v", "--value" })
    private String value;

    @Parameter(names = { "-in", "--input" })
    private String fileName;

    public ArgsToCommand(String type, String key, String value) {
        super();
        this.type = type;
        this.key = key;
        this.value = value;
    }

    public ArgsToCommand() {

    }

    public String getCommandJSON() {
        IJsonCommand command = null;

        if (fileName != null) {
            return getCommandFromFile();
        } else {
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
                break;
            }

        }

        return command.toJson();
    }

    private String getCommandFromFile() {
        String filePath = "src/client/data/" + this.fileName;
        Path path = Paths.get(filePath);
        String json = null;
        try {
            json = new String(Files.readAllBytes(path));
        } catch (IOException e) {
//          e.printStackTrace();
            System.out.println("no file/command found");
        }
        return json;
    }
}
