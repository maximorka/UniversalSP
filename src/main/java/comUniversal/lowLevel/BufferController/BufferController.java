package comUniversal.lowLevel.BufferController;

import comUniversal.util.MyComplex;
import comUniversal.util.Params;

import java.util.ArrayList;
import java.util.List;

public class BufferController {

    protected volatile int sampleFreq = 48_000;
    protected volatile int sampleCountPer10ms = sampleFreq / 100;
    protected volatile int sampleCountPer1ms = sampleFreq / 1000;

    private boolean startWork = false;

    private int percentBuffer = 0 ;
    private WorkingThread workingThread;
    private float k = 0;
    private GetIQSource getIQSource;
    private int borderProcent;

    // Listeners
    private List<IBufferController> controllers = new ArrayList<>();
    public void addTransferListener(IBufferController listener){controllers.add(listener);}
    public void clearTransferListener(){controllers.clear();}
    private void toListenersTransferDataBytes(MyComplex sample){
        if(!controllers.isEmpty())
            for(IBufferController listener: controllers)
                listener.sendData(sample);
    }

    public void setSources(GetIQSource sources) {
        this.getIQSource = sources;
    }

    public void updateSampleFrequency(int frequency) {
        this.sampleFreq = frequency;
    }

    public void updatePercent(int percent) {
        this.percentBuffer = percent;
    }

    public BufferController(int sampleFreq ){

        borderProcent = Integer.parseInt(Params.SETTINGS.getString("border_procent", "6"));

        this.sampleFreq = sampleFreq;

        sampleCountPer10ms = sampleFreq / 100;
        sampleCountPer1ms = sampleFreq / 1000;

        workingThread = new WorkingThread();
        workingThread.start();
    }
public void setWorkingThread(boolean work){
    workingThread.stop();
}
    class WorkingThread extends Thread {
        @Override
        public void run() {
            long start = System.currentTimeMillis();
            long executeTime = 0;

            while (true) {
                while ((System.currentTimeMillis() - start) < 10) {
                    try {
                        Thread.sleep(0,100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                executeTime = System.currentTimeMillis() - start;
                start = System.currentTimeMillis();
                if(executeTime>100){
                    executeTime=100;
                }
                //System.out.println("wokr");
               // if (Core.getCore().device[0].ethernetDriver.isConect()) {
                    int needSendSample = sampleCountPer1ms * (int) executeTime;
                    if (percentBuffer < borderProcent) {
                        needSendSample += sampleCountPer1ms * 2;
                    }
                    for (int i = 0; i < needSendSample; i++) {
                        MyComplex sample = new MyComplex(0.0F, 0.0F);
                        if (getIQSource != null) {
                            sample = getIQSource.getIQ();
                        }
                        toListenersTransferDataBytes(sample);
                    }

              //  }
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
