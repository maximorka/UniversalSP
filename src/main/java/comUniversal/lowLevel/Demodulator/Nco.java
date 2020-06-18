package comUniversal.lowLevel.Demodulator;

import org.apache.commons.math3.complex.Complex;

public class Nco {

    private float phase = 0.f;
    private float shift = 0.f;

    public Nco(float shift){
        this.shift = shift;
    }

    Complex get(){
        phase += shift;

        if(phase > Math.PI)
            phase -= 2.f * Math.PI;
        if(phase < -Math.PI)
            phase += 2.f * Math.PI;

        return new Complex(Math.cos(phase), Math.sin(phase));
    }
}
