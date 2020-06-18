package comUniversal.lowLevel.Demodulator;

import org.apache.commons.math3.complex.Complex;

public class ZeroCrossDetector {

    private float lastSign = 0.f;

    public boolean detect(Complex sempl){
        boolean result = (Math.signum(sempl.getReal()) != lastSign);
        lastSign = (float)Math.signum(sempl.getReal());
        return result;
    }

}
