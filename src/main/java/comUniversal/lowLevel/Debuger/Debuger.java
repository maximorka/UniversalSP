package comUniversal.lowLevel.Debuger;

import comUniversal.lowLevel.DriverEthernet.EthernetDriver;
import comUniversal.lowLevel.DriverEthernet.ReceiverDataBytes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
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
        if(client!=null) {
            if (!client.isClosed()) {
                try {
                    outputStream.write(data);
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class ClientWaiting extends Thread {

        public void run(){
            System.out.println("Waiting client ...");
            try {
                client = server.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Client is conected");
            try {
                outputStream = client.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

