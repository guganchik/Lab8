package data;

import java.io.Serializable;
import java.net.SocketAddress;

public class Request implements Serializable {
    
    public String command;
    public String[] args;
    public Serializable object;
    public SocketAddress client;
    public String login;
    public String hash;

    public Request(String command, String[] args, String login, String hash) {
        this.command = command;
        this.args = args;
        this.login = login;
        this.hash = hash;
    }
    
    public Request(String command, String[] args, Serializable object, String login, String hash) {
        this.command = command;
        this.args = args;
        this.object = object;
        this.login = login;
        this.hash = hash;
    }

    public void setClient(SocketAddress client) {
        this.client = client;
    }

    public String getCommand() {
        return command;
    }

    public Serializable getObject() {
        return object;
    }

    public SocketAddress getClient() {
        return client;
    }

    public String[] getArgs() {
        return args;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
