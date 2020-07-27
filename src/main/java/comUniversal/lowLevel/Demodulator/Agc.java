package comUniversal.lowLevel.Demodulator;

import org.apache.commons.math3.complex.Complex;

public class Agc {
    private float reference, dBGainUp, dBGainLow;
    private int length;

    private MovingAverage lpf;
    private LineDelay lineDelay;

    public Agc(float reference, float dBGainUp, float dBGainLow, int length){
        this.reference = reference;
        this.dBGainUp = dBGainUp;
        this.dBGainLow = dBGainLow;
        this.length = length;
        lpf = new MovingAverage(this.length);
        lineDelay = new LineDelay(this.length / 2);


        
    }

    public Complex update(Complex sempl){

        Complex semplDelay = lineDelay.delay(sempl);

        float level = (float)sempl.abs();
        float levelLpf = lpf.average(level);

        float gain = 1.f;
        if (levelLpf != 0.f)
            gain = reference / levelLpf;

//        float dBGain = (float)(10.f * Math.log10(level));
//        if(dBGain > dBGainUp) {gain = (float)Math.pow(10.f, dBGainUp / 10.f);}
//        if(dBGain < dBGainLow) {gain = (float)Math.pow(10.f, dBGainLow / 10.f);}

//        System.out.println("Gain = " + gain);

        return semplDelay.multiply(gain);
    }
}
