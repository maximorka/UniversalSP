package comUniversal;

import comUniversal.lowLevel.DriverEthernet.EthernetDriver;
import comUniversal.lowLevel.DriverHorizon.*;

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
    private int percentBuffer = 0 ;
    private WorkingThread workingThread;
    private float k = 0;


    private BufferController(int sampleFreq ){

        Core.getCore().driverHorizon.addDucBufferPercent(new DucBufferPercent() {
            @Override
            public void percent(int percent) {

                percentBuffer = percent;
            }
        });

        this.sampleFreq = sampleFreq;
        if(sampleFreq == 48000){
            k = 4;
        }else
            k = (float) 0.6;

        sampleCountPer10ms = sampleFreq / 100;
        byteCountPer10ms = sampleCountPer10ms * 3;
        sampleCountPer1ms = sampleFreq / 1000;

        workingThread = new WorkingThread();
        workingThread.start();
    }

    class WorkingThread extends Thread {
        @Override
        public void run() {
            long start = System.currentTimeMillis();
            long executeTime  = 1;

            while (true) {
                long delay  = 5;

                if (finishingWork) {
                    continue;
                }
                if (percentBuffer < 60) {
                    int needSendSample = sampleCountPer1ms * (int) executeTime;
                    needSendSample+= (int) ((int)(k*executeTime)*sampleCountPer1ms);
                    for (int i = 0; i < needSendSample ; i++) {
                        Complex sample = modulatorTest.getIQ();
                        driverHorizon.ducSetIq(sample);
                    }
                }

                executeTime = System.currentTimeMillis() - start;
                start = System.currentTimeMillis();

                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        BufferController bufferController = new BufferController(3000);

        while (true){

           // bufferController.sendIQ();
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
