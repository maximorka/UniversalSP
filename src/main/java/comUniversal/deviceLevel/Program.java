package comUniversal.deviceLevel;

import org.apache.commons.math3.complex.Complex;

public abstract class Program {
    abstract public void fromReceiver(Complex sample);
    abstract public void toTransiver(Complex sample);
    abstract public boolean connect(String typeDevice);
    abstract public int getData(int[] data);

    class WorkingThread extends Thread {
        @Override
        public void run() {
            //getData();
        }
    }
}
