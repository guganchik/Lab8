package app;

import data.Request;
import data.Response;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

class Client {
    
    SocketAddress address;
    long time;
    
    public Client(SocketAddress address) {
        this.address = address;
        time = System.currentTimeMillis();
    }

    public SocketAddress getAddress() {
        return address;
    }

    public long getTime() {
        return time;
    }
    
    public void updateTime() {
        time = System.currentTimeMillis();
    }
}


public class NetworkProvider {
    
    private static final int BUFFER_SIZE = 1024;
    
    private final DatagramSocket datagramSocket;
    
    private final HashMap<String,Client> clients;
    

    public NetworkProvider(int port) throws SocketException {
        clients = new HashMap();
        datagramSocket = new DatagramSocket(port);
        datagramSocket.setSoTimeout(20);
        System.out.println("=========== Server started on port: " + port + " ===========");
    }

    public Request receive() {
        ByteBuffer buf;
        try {
            
            /**
            try {
                TimeUnit.SECONDS.sleep(30);
            } catch (InterruptedException ex) {
                Logger.getLogger(NetworkProvider.class.getName()).log(Level.SEVERE, null, ex);
            }
            */
            
            buf = ByteBuffer.allocate(BUFFER_SIZE);
            
            DatagramPacket datagramPacket = new DatagramPacket(buf.array(), buf.array().length);
            
            datagramSocket.receive(datagramPacket);
            SocketAddress sa = datagramPacket.getSocketAddress();
            
            InetSocketAddress isa = (InetSocketAddress) sa;
            updateClient(isa.getAddress().getHostAddress() + ":" + isa.getPort(), sa);
            
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(buf.array()));
            Request request = (Request) objectInputStream.readObject();
            request.setClient(sa);
            return request;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(NetworkProvider.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            //Logger.getLogger(NetworkProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        //buf = null;
        System.gc();
        return null;
    }
    
    public synchronized void send(SocketAddress sa, Response response) {
        // TODO: send all
        ForkJoinPool pool = ForkJoinPool.commonPool();
        pool.invoke(new RecursiveAction() {
            @Override
            protected void compute() {
                ObjectOutputStream objectOutputStream = null;
                try {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                    objectOutputStream.writeObject(response);
                    DatagramPacket datagramPacket = new DatagramPacket(byteArrayOutputStream.toByteArray(), byteArrayOutputStream.toByteArray().length);
                    if (response.isSendAll()) {
                        synchronized (clients) {
                            for (Client client : clients.values()) {
                                datagramPacket.setSocketAddress(client.getAddress());
                                datagramSocket.send(datagramPacket);
                            }
                        }
                    } else {
                        datagramPacket.setSocketAddress(sa);
                        datagramSocket.send(datagramPacket);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(NetworkProvider.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        objectOutputStream.close();
                    } catch (IOException ex) {
                        Logger.getLogger(NetworkProvider.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }
    
    private void updateClient(String key, SocketAddress sa) {
        Client client = clients.get(key);
        if (client == null) {
            client = new Client(sa);
            clients.put(key, client);
        } else {
            client.updateTime();
        }
    }
    
    public void printClients() {
        for (Map.Entry<String,Client> client : clients.entrySet()){
            System.out.println(client.getKey() + " = " + client.getValue());
        }    
    }
}
