package comUniversal.lowLevel.Demodulator;

import comUniversal.lowLevel.DriverHorizon.DdcIQ;
import comUniversal.util.Complex;

import java.util.ArrayList;
import java.util.List;

public class OptimalNonCoherentDеmodulatorPsk {

    private float baudeRate = 0.f;
    private float samplingFrequency = 0.f;
    private Bpf bpf;
    private Pll pll;
    private Movingaverage integrator;
    private Clocker clocker;

    public OptimalNonCoherentDеmodulatorPsk(float baudeRate, float samplingFrequency){
        this.baudeRate = baudeRate;
        this.samplingFrequency = samplingFrequency;
        this.integrator = new Movingaverage((int)(this.samplingFrequency/this.baudeRate));
        this.clocker = new Clocker(this.baudeRate/this.samplingFrequency);
        bpf = new Bpf(bpfCoefficients);
        pll = new Pll();
    }

    public void setParametrs(float baudeRate, float samplingFrequency) {
        this.baudeRate = baudeRate;
        this.samplingFrequency = samplingFrequency;
        this.integrator = new Movingaverage((int)(this.samplingFrequency/this.baudeRate));
        this.clocker = new Clocker(this.baudeRate/this.samplingFrequency);
//        bpf = new Bpf(bpfCoefficients);
//        pll = new Pll();
    }

    private List<Symbol> symbol = new ArrayList<>();
    public void addListenerSymbol(Symbol listener){symbol.add(listener);}
    public void clearListenersSymbol(){symbol.clear();}
    private void toListenersSymbol(int data) {
        if (!symbol.isEmpty())
            for (Symbol listener : symbol)
                listener.symbol(data);
    }

    private List<IqOutDebug> listeners = new ArrayList<>();
    public void addListenerIq(IqOutDebug listener){
        listeners.add(listener);
    }
    public void clearListenerIq(){
        listeners.clear();
    }
    private void toListenersIq(Complex sempl){
        if(!listeners.isEmpty())
            for(IqOutDebug listener: listeners)
                listener.sempl(sempl);
    }

    public void demodulate(Complex sempl){

        Complex outBpf = bpf.update(sempl);
        Complex outPll = pll.update(outBpf);
        Complex outInt = integrator.update(outPll);

        if(clocker.update(outInt)) {
            toListenersSymbol(clocker.getBit());
        }
        toListenersIq(outInt);
    }

    private float[] bpfCoefficients = {
            64.99876624632977950E-6f,
            151.3981568291913220E-6f,
            256.1165611330516190E-6f,
            374.8966222027231650E-6f,
            502.3723304823811870E-6f,
            632.1809798303781920E-6f,
            757.1191118444295400E-6f,
            869.3401019265502330E-6f,
            960.5895742391986690E-6f,
            0.001022473419369444f,
            0.001046751874708127f,
            0.001025651958339496f,
            952.1895647241250340E-6f,
            820.4917723598478010E-6f,
            626.1094119423132720E-6f,
            366.3097234773005650E-6f,
            40.33900963611274900E-6f,
            -350.3544208971329110E-6f,
            -801.9460326705500390E-6f,
            -0.001308117586779536f,
            -0.001860003160272496f,
            -0.002446203893938310f,
            -0.003052874967215413f,
            -0.003663886395526519f,
            -0.004261057222307981f,
            -0.004824460590646086f,
            -0.005332795086484512f,
            -0.005763815707333121f,
            -0.006094815887802823f,
            -0.006303150264833393f,
            -0.006366786346309174f,
            -0.006264872006715505f,
            -0.005978304815538262f,
            -0.005490288642919710f,
            -0.004786862807843792f,
            -0.003857389251612930f,
            -0.002694983837335242f,
            -0.001296878886947844f,
            335.2945479988514420E-6f,
            0.002195314456926102f,
            0.004272273424972350f,
            0.006550587428221161f,
            0.009010110842722812f,
            0.011626358025148893f,
            0.014370829143809681f,
            0.017211435267674928f,
            0.020113015130453167f,
            0.023037933547947757f,
            0.025946749247784347f,
            0.028798937933906248f,
            0.031553654809584573f,
            0.034170519568976251f,
            0.036610406075227593f,
            0.038836218598237667f,
            0.040813636600860445f,
            0.042511810639391690f,
            0.043903992970865433f,
            0.044968087911795622f,
            0.045687108834592052f,
            0.046049530872193545f,
            0.046049530872193545f,
            0.045687108834592052f,
            0.044968087911795622f,
            0.043903992970865433f,
            0.042511810639391690f,
            0.040813636600860445f,
            0.038836218598237667f,
            0.036610406075227593f,
            0.034170519568976251f,
            0.031553654809584573f,
            0.028798937933906248f,
            0.025946749247784347f,
            0.023037933547947757f,
            0.020113015130453167f,
            0.017211435267674928f,
            0.014370829143809681f,
            0.011626358025148893f,
            0.009010110842722812f,
            0.006550587428221161f,
            0.004272273424972350f,
            0.002195314456926102f,
            335.2945479988514420E-6f,
            -0.001296878886947844f,
            -0.002694983837335242f,
            -0.003857389251612930f,
            -0.004786862807843792f,
            -0.005490288642919710f,
            -0.005978304815538262f,
            -0.006264872006715505f,
            -0.006366786346309174f,
            -0.006303150264833393f,
            -0.006094815887802823f,
            -0.005763815707333121f,
            -0.005332795086484512f,
            -0.004824460590646086f,
            -0.004261057222307981f,
            -0.003663886395526519f,
            -0.003052874967215413f,
            -0.002446203893938310f,
            -0.001860003160272496f,
            -0.001308117586779536f,
            -801.9460326705500390E-6f,
            -350.3544208971329110E-6f,
            40.33900963611274900E-6f,
            366.3097234773005650E-6f,
            626.1094119423132720E-6f,
            820.4917723598478010E-6f,
            952.1895647241250340E-6f,
            0.001025651958339496f,
            0.001046751874708127f,
            0.001022473419369444f,
            960.5895742391986690E-6f,
            869.3401019265502330E-6f,
            757.1191118444295400E-6f,
            632.1809798303781920E-6f,
            502.3723304823811870E-6f,
            374.8966222027231650E-6f,
            256.1165611330516190E-6f,
            151.3981568291913220E-6f,
            64.99876624632977950E-6f
    };

}

class Pll{

    private Vco vco;
    private LoopFilter loopFilter;

    private Complex mixer(Complex x, Complex y){
        return new Complex(x.re * y.re - x.im * y.im, x.im * y.re + x.re * y.im);
    }

    public Pll(){
        vco = new Vco();
        loopFilter = new LoopFilter(0.01f, 0.00001f, 2.f* (float)Math.PI * 500.f /48000.f);
    }

    private float phaseDetect(Complex sempl){
//        float angle = (float) Math.atan2(sempl.im, sempl.re);
//        angle += Math.PI;
//        angle %= Math.PI;
//        angle -= Math.PI / 2.f;
//        return angle;
        return (float) Math.atan(sempl.im / sempl.re);
    }

    public Complex update(Complex sempl){

        Complex gen = vco.get();
        gen.im *= -1.f;

        Complex out = mixer(sempl, gen);

        float error = phaseDetect(out);

        error = loopFilter.update(error);
        vco.update(error);

        return out;

    }

}

class Vco{

    private float phaseAccum = 0.f;

    public Complex get(){
        return new Complex((float)Math.cos(phaseAccum), (float)Math.sin(phaseAccum));
    }

    public void update(float phase){
        phaseAccum += phase;
        phaseAccum %= 2*Math.PI;
    }
}

class Bpf{

    private float[] coefficients;
    private Complex[] lineDelay;

    public Bpf(float[] coefficients){
        this.coefficients = coefficients;
        lineDelay = new Complex[coefficients.length];
        for(int i = 0; i < lineDelay.length; i++)
            lineDelay[i] = new Complex(0.f, 0.f);
    }

    public Complex update(Complex sempl){
        System.arraycopy(lineDelay, 1, lineDelay, 0, lineDelay.length - 1);
        lineDelay[lineDelay.length - 1] = sempl;
        Complex out = new Complex(0.f, 0.f);
        for(int i = 0; i < lineDelay.length; i++){
            out.re += lineDelay[i].re * coefficients[i];
            out.im += lineDelay[i].im * coefficients[i];
        }
        return out;
    }

}