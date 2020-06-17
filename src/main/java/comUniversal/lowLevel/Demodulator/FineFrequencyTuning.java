package comUniversal.lowLevel.Demodulator;

import org.apache.commons.math3.complex.Complex;

public class FineFrequencyTuning {

    private MovingAverage before, after;
    private double oldAngle = 0.f, phaseAccum = 0.f;
    private LineDelay lineDelay;


    public FineFrequencyTuning() {
        before = new MovingAverage(30);
        after = new MovingAverage(3000);
        lineDelay = new LineDelay((3000+30)/2);
    }

    public Complex tuning(Complex sempl){

        Complex outBefore = before.average(sempl);

        Complex outMix = outBefore.multiply(outBefore);

        Complex outAfter = after.average(outMix);

        double angle = outAfter.getArgument();

        double diff = angle - oldAngle;

        if(diff > Math.PI)
            diff -= 2.f * Math.PI;

        if(diff < -Math.PI)
            diff += 2.f * Math.PI;

        oldAngle = angle;

        phaseAccum += (-diff / 2.f);

        if(phaseAccum > Math.PI)
            phaseAccum -= 2.f * Math.PI;

        if(phaseAccum < -Math.PI)
            phaseAccum += 2.f * Math.PI;

        Complex outClear = new Complex(Math.cos(phaseAccum), Math.sin(phaseAccum));

        Complex outDelay = lineDelay.delay(sempl);

        Complex outMixer = outDelay.multiply(outClear);

        return outMixer;
    }

}
