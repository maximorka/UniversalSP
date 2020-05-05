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

    private Socket socket;

    private ReadThread readThread;
   //private OutputThread outputThread;


    public boolean doInit(String ip, int port) {
        this.socket = new Socket();
        this.ip = ip;
        this.port = port;
        return initSocket();
    }


    private boolean initSocket() {
        try {
            socket.connect(new InetSocketAddress(ip,port),3_000);
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
                readThread.setRunning(false);
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
            while(this.running){

                int data = 0;
                try {
                    data = inputStream.available();
                    if (data > 0) {
                        int dataSize = inputStream.read(inputBuffer);
                        //System.out.println("RX:"+dataSize);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    public void writeBytes(byte[] data) {
        if ( !socket.isClosed()) {
            try {
                outputStream.write(data);
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
