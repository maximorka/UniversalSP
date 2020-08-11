package comUniversal.lowLevel.DriverEthernet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class EthernetDriver {
    private OutputStream outputStream;
    private InputStream inputStream;
    private String ip;
    private int port;
    private boolean connect = false;
    private Socket socket;

    private ReadThread readThread;

    private long lastReceiveTime;

    // Listeners
    private List<ReceiverDataBytes> receiver = new ArrayList<>();
    public void addReceiverListener(ReceiverDataBytes listener){receiver.add(listener);}
    public void clearReceiverListener(){receiver.clear();}


    public boolean isConnect(){
        if(socket != null){
            return socket.isConnected();
        }else return false;

    }
    public boolean doInit(String ip, int port) {
        this.socket = new Socket();
        this.ip = ip;
        this.port = port;
        return initSocket();
    }


    private boolean initSocket() {
        if (readThread != null) {
            readThread.running = false;

        }
        closeSocket();

        try {
            socket = new Socket();
            System.out.println("Ip " + ip);
            System.out.println("Port " + port);
            socket.connect(new InetSocketAddress(ip,port),3_000);
            connect = true;

            System.out.println("Connected");

            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
            readThread = new ReadThread();
            readThread.start();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean closeSocket(){
        boolean con = true;
        if (socket != null && !socket.isClosed()) {
            try {
                if (readThread != null) {
                    readThread.running = false;
                }
                socket.close();
                con = false;
            } catch ( IOException e) {
                e.printStackTrace();
            }
        }
        return con;
    }

    class ReadThread extends Thread{

        private byte[] inputBuffer = new byte[2048];
        private boolean running = true;
        public void setRunning(boolean running) {
            this.running = running;
        }
        @Override
        public void run() {
            while(running){

                int data = 0;
                try {
                    data = inputStream.read(inputBuffer);
                    if (data > 0) {
                        connect = true;
                        if(!receiver.isEmpty())
                            for(ReceiverDataBytes listener: receiver)
                                for (int i = 0; i < data ; i++) {
                                    listener.ReceiveData(inputBuffer[i]);

                                }

                    }

                } catch (IOException e) {
                    connect = false;
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void writeByte(byte data) {

        if (connect) {
            try {
                outputStream.write(data);
               // outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
        else{
            System.out.println("try");
        }
    }
    public void writeBytes(byte[] data) {
        if (connect) {
            try {
                for (byte out : data)
                    outputStream.write(out);
               // outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

public boolean isConect(){
        return connect;
}

}
