package comUniversal;

import comUniversal.lowLevel.DriverHorizon.DriverHorizon;
import comUniversal.lowLevel.DriverHorizon.TransferDataBytes;
import comUniversal.lowLevel.DriverHorizon.Width;
import comUniversal.lowLevel.EthernetDriver;

public class BufferController {
    /**
     * Розмір одного семпла на передачу, в байтах
     */
    public static final int TX_SINGLE_SAMPLE_SIZE_IN_BYTES = 3;
    /**
     * Розмір блока семплів на передачу (враховуючи команду)
     */
    public static final int TX_BLOCK_SAMPLE_SIZE_IN_BYTES = TX_SINGLE_SAMPLE_SIZE_IN_BYTES * 64;

    protected volatile int sampleFreq = 48_000;
    protected volatile int sampleCountPer10ms = sampleFreq / 100;
    protected volatile int byteCountPer10ms = sampleCountPer10ms * 3;
    protected volatile int sampleCountPer1ms = sampleFreq / 1000;

    private boolean finishingWork = false;

    public EthernetDriver ethernetDriver = new EthernetDriver();
    private DriverHorizon driverHorizon = new DriverHorizon();
    private ModulatorTest modulatorTest = new ModulatorTest();
    //private DriverHorizon driverHorizon;
    private int percentBuffer = 0 ;
    private WorkingThread workingThread;


    private BufferController(){
        ethernetDriver.doInit("192.168.0.1", 80);
        TransferDataBytes listener = new TransferDataBytes() {
            @Override
            public void SendData(byte[] data) {
                ethernetDriver.writeBytes(data);
            }
        };
        driverHorizon.addTransferListener(listener);
        driverHorizon.ducSetWidth(Width.kHz_3);
        workingThread = new WorkingThread();
        workingThread.start();
    }
class WorkingThread extends Thread{
    @Override
    public void run() {
        while (true){
            long start = System.currentTimeMillis();

            if (finishingWork) {
                continue;
            }
            if( percentBuffer>60){
                continue;
            }
            Complex sample[] = modulatorTest.getIQ();
            for (int i = 0; i <sample.length ; i++) {
                driverHorizon.ducSetIq(sample[i]);
            }

        }
    }
}


    public void sendIQ(){
        modulatorTest.getIQ();
        Complex sample[] = new Complex[30];
        for (int i = 0; i <sample.length ; i++) {
            System.out.println("send");
            sample[i] = new Complex(1,0);
            driverHorizon.ducSetIq(sample[i]);
        }

    }

    public static void main(String[] args) {
        BufferController bufferController = new BufferController();

        while (true){

            bufferController.sendIQ();
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
