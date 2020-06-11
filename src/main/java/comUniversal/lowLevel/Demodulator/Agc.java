package comUniversal.lowLevel.Demodulator;

import org.apache.commons.math3.complex.Complex;

public class Agc {
    private float logR, alpha, dBGainUp, dBGainLow;
    private float accum = 0;
    public Agc(float r, float alpha, float dBGainUp, float dBGainLow){
        this.logR = (float)Math.log10(r);
        this.alpha = alpha;
        this.dBGainUp = dBGainUp;
        this.dBGainLow = dBGainLow;
    }
    float get(){return (float)Math.pow(accum, 10);}
    void update(Complex sempl){
        float level = (float)sempl.abs();
        float logLevel = (level != 0.f)? (float)Math.log10(level) : -10.f;
        accum += alpha * (logR - logLevel);
        if(accum > dBGainUp) accum = dBGainUp;
        if(accum < dBGainLow) accum = dBGainLow;
    }
}
