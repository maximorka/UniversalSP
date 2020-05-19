package comUniversal.lowLevel.Debuger;

import comUniversal.util.Complex;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class Debuger {

    private ServerSocket server = new ServerSocket(80);
    private Socket client;
    private OutputStream outputStream;
    private ClientWaiting clientWaiting = new ClientWaiting();
    private int semplCounter = 0;
    private byte[] packet = new byte[4 + 64*(4+4)];
    private ByteBuffer re = ByteBuffer.allocate(4);
    private ByteBuffer im = ByteBuffer.allocate(4);
    private int index = 0;

    public Debuger() throws IOException {
        clientWaiting.start();
    }

    private void sendData(byte[] data) {
        if(client == null) return;
        try {
            outputStream.write(data);
            outputStream.flush();
        } catch (IOException e) {}
    }

    public void show(Complex sempl){

        if(index == 0) {
            packet[index++] = 0x00;
            packet[index++] = 0x56;
            packet[index++] = 0x34;
            packet[index++] = 0x12;
        }

        re.clear();
        im.clear();
        re.putFloat(sempl.re);
        im.putFloat(sempl.im);

        packet[index++] = re.array()[3];
        packet[index++] = re.array()[2];
        packet[index++] = re.array()[1];
        packet[index++] = re.array()[0];
        packet[index++] = im.array()[3];
        packet[index++] = im.array()[2];
        packet[index++] = im.array()[1];
        packet[index++] = im.array()[0];

        if(index == packet.length) {
            index = 0;
            sendData(packet);
        }

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

