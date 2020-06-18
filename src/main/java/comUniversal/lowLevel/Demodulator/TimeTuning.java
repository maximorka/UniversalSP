package comUniversal.lowLevel.Demodulator;

import org.apache.commons.math3.complex.Complex;

public class TimeTuning {

    private ZeroCrossDetector zeroCrossDetector;
    private ZeroCrossDetector outZeroCrossDetector;
    private Nco nco;
    private MovingAverage timeFilter;
    private LineDelay lineDelay;
    private LineDelay signalLineDelay;
    private double oldAngle = 0.f, phaseAccum = 0.f;
    public Complex sempl = new Complex(0.f, 0.f);
    private int symbol = 0;
    private int lastBit = 0;

    public TimeTuning(float boudRate){
        zeroCrossDetector = new ZeroCrossDetector();
        outZeroCrossDetector = new ZeroCrossDetector();
        nco = new Nco(2.f * (float)Math.PI * boudRate / 3000.f);
        timeFilter = new  MovingAverage(3000);
        lineDelay = new LineDelay(3000 / 2);
        signalLineDelay = new LineDelay((3000 / 2) + 4);
    }

    public int getBit(){
        return this.symbol;
    }

    public boolean tuning(Complex sempl){

        float cross = zeroCrossDetector.detect(sempl)? 1.f : 0.f;

        Complex outCross = new Complex(cross, cross);

        Complex outNco = nco.get();

        Complex outShiftCross = outNco.multiply(outCross);

        Complex outFilterTime = timeFilter.average(outShiftCross);

        Complex outLineDelayNco = lineDelay.delay(outNco);

        Complex outLineDelayNcoConj = outLineDelayNco.conjugate();

        Complex result = outFilterTime.multiply(outLineDelayNcoConj);

        double angle = result.getArgument();

        double diff = angle - oldAngle;

        if(diff > Math.PI)
            diff -= 2.f * Math.PI;

        if(diff < -Math.PI)
            diff += 2.f * Math.PI;

        oldAngle = angle;

        phaseAccum += (diff / 2.f);

        if(phaseAccum > Math.PI)
            phaseAccum -= 2.f * Math.PI;

        if(phaseAccum < -Math.PI)
            phaseAccum += 2.f * Math.PI;

        Complex outClear = new Complex(Math.cos(phaseAccum), Math.sin(phaseAccum));

        boolean crossOut = outZeroCrossDetector.detect(outClear);

        Complex outDelaySempl = signalLineDelay.delay(sempl);

        if(crossOut) {

            sempl = new Complex(outDelaySempl.getReal(), outDelaySempl.getImaginary());

            int currentBit = (sempl.getReal() >= 0.f)? 0 : 1;

            symbol = (currentBit == lastBit)? 0 : 1;

            lastBit = currentBit;

        }

        return crossOut;
    }

}
