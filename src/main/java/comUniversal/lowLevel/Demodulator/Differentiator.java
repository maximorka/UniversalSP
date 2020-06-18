package comUniversal.lowLevel.Demodulator;

import org.apache.commons.math3.complex.Complex;

public class Differentiator {

    private Complex[] delay = new Complex[2];

    public Differentiator(){
        delay[0] = new Complex(0.f, 0.f);
        delay[1] = new Complex(0.f, 0.f);
    }

    float get(Complex sempl){

        double up = delay[0].getReal() * (sempl.getImaginary() - delay[1].getImaginary());
        double dowm = delay[0].getImaginary() * (sempl.getReal() - delay[1].getReal());

        delay[1] = new Complex(delay[0].getReal(), delay[0].getImaginary());
        delay[0] = new Complex(sempl.getReal(), sempl.getImaginary());

        return (float)(up - dowm);
    }

}
