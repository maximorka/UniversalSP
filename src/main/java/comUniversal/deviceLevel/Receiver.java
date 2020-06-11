package comUniversal.deviceLevel;

import org.apache.commons.math3.complex.Complex;

public abstract class Receiver {
    abstract public void toEthernet(byte data);
    abstract public void fromEthernet(byte data);
    abstract public void setFrequency(int frequency);
    abstract public void getFrequency(int frequency);
    abstract public void setMode(int mode);//for

    abstract public int getSample(Complex[] data);
}
