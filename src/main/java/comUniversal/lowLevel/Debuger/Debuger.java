package comUniversal.lowLevel.Debuger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Debuger {

    private ServerSocket server = new ServerSocket(80);
    private Socket client;
    private OutputStream outputStream;
    ClientWaiting clientWaiting = new ClientWaiting();

    public Debuger() throws IOException {
        clientWaiting.start();
    }

    public void sendData(byte data) {
        if(client == null) return;
        try {
            outputStream.write(data);
            outputStream.flush();
        } catch (IOException e) {}
    }

    public class ClientWaiting extends Thread {

        public void run(){

            while(true) {
                try {
                    client = server.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    outputStream = client.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

