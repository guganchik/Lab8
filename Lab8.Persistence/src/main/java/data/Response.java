package data;

import java.io.Serializable;

public class Response implements Serializable {
    
    public String command;
    public int result;
    public Serializable object;
    
    
    public String output;
    public String[] args;
    public String login;
    public String hash;
    
    public boolean sendAll = false;
    
    

    public Response(String command, int result) {
        this.command = command;
        this.result = result;
    }
    
    public Response(String command, int result, Serializable object, boolean sendAll) {
        this.command = command;
        this.result = result;
        this.object = object;
        this.sendAll = sendAll;
    }
    
    public Response(String command, String[] args, String output) {
        this.command = command;
        this.output = output;
    }

    public Response(String command, String[] args, String output, String login, String hash) {
        this.command = command;
        this.output = output;
        this.login = login;
        this.hash = hash;
    }
    
    public Response(String command, String[] args, String output, Class<? extends Serializable> object) {
        this.command = command;
        this.output = output;
        this.object = object;
    }

    public boolean isSendAll() {
        return sendAll;
    }


    public String getCommand() {
        return command;
    }

    public String getOutput() {
        return output;
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
    
    
    

    @Override
    public String toString() {
        return "Response{" + "command=" + command + ", result=" + result + ", object=" + ((object==null)?null:object.toString()) + '}';
    }

    
    
    public int getResult() {
        return result;
    }
    
    public Serializable getObject() {
        return object;
    }
    
    
    
}
