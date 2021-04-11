package server;

public interface ICommand {
    public boolean execute(Database database);

    public Response getResponse();
}
