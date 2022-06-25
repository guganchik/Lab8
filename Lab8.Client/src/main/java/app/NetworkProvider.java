package app;

import data.Request;
import data.Response;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkProvider extends Observable implements Runnable {
    
    private static final int BUFFER_SIZE = 1024 * 1024;
    
    InetSocketAddress socketAddress;
    DatagramChannel datagramChannel;
    
    Thread thread;
    
    public NetworkProvider(String address, int port) throws SocketException, IOException {
        socketAddress = new InetSocketAddress(address, port);
        datagramChannel = DatagramChannel.open();
        datagramChannel.socket().setSoTimeout(10);
        datagramChannel.configureBlocking(false);
    }
    
    public void startListener() {
        thread = new Thread(this);
        thread.start();        
    }
    
    @Override
    public void run() {
        while (true) {
            Response response = receive();
            if (response != null) {
                //System.out.println("Response!");
                setChanged();
                notifyObservers(response);
            }
        }
    }
    
    private Response receive() {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        try {
            SocketAddress address = null;
            //int i = 0;
            while (address == null) {
                address = datagramChannel.receive(buffer);
                try {
                    Thread.currentThread().sleep(50);
                } catch (InterruptedException ex) {
                    Logger.getLogger(NetworkProvider.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(buffer.array()));
            Response response = (Response) objectInputStream.readObject();
            return response;
        } catch (IOException ex) {
            Logger.getLogger(NetworkProvider.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(NetworkProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void send(Request request) {
        ObjectOutputStream os = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            os = new ObjectOutputStream(out);
            os.writeObject(request);
            datagramChannel.send(ByteBuffer.wrap(out.toByteArray()), socketAddress);
        } catch (IOException ex) {
            Logger.getLogger(NetworkProvider.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(NetworkProvider.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
