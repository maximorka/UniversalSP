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
    private byte[] mask = {0x00, 0x56, 0x34, 0x12};
    private ByteBuffer re = ByteBuffer.allocate(4);
    private ByteBuffer im = ByteBuffer.allocate(4);

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

        if(semplCounter == 0)
            sendData(mask);

        re.clear();
        im.clear();

        re.order(ByteOrder.LITTLE_ENDIAN);
        im.order(ByteOrder.LITTLE_ENDIAN);

        re.putFloat(sempl.re);
        im.putFloat(sempl.im);

        sendData(re.array());
        sendData(im.array());

        semplCounter++;

        if(semplCounter == 64)
            semplCounter = 0;

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

