package comUniversal.lowLevel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class EthernetDriver {
    private OutputStream outputStream;
    private InputStream inputStream;
    private String ip;
    private int port;
    private boolean connect = false;
    private Socket socket;

    private ReadThread readThread;


    private long lastReceiveTime;

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

    public void closeSocket(){
        if (socket != null && !socket.isClosed()) {
            try {
                if (readThread != null) {
                    readThread.running = false;
                }
                socket.close();
            } catch ( IOException e) {
                e.printStackTrace();
            }
        }
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
                        //System.out.println("RX:"+dataSize);
                    }

                } catch (IOException e) {
                    connect = false;
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    public void writeByte(byte data) {
        if(socket!=null) {
            if (!socket.isClosed()) {
                try {
                    outputStream.write(data);
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void writeBytes(byte[] data) {

        for(byte out: data)
            writeByte(out);
    }

public boolean isConect(){
        return connect;
}

}
