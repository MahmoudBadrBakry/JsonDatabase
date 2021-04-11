package server;

public class ExitCommand implements ICommand {

    private Response response;
    
    @Override
    public boolean execute(Database database) {
        database.save();
        response = new Response("OK");
        return false;
    }

    @Override
    public Response getResponse() {
        return response;
    }


}
