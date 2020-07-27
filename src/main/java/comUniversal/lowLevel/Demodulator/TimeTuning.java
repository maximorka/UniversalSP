package comUniversal.lowLevel.Demodulator;

import org.apache.commons.math3.complex.Complex;

public class TimeTuning {

    private ZeroCrossDetector zeroCrossDetector;
    private ZeroCrossDetector outZeroCrossDetector;
    private Nco nco;
    private Agc agc;
    private MovingAverage timeFilter;
    private LineDelay lineDelay;
    private LineDelay signalLineDelay;
    private double oldAngle = 0.f, phaseAccum = 0.f;
    private Complex lastSempl = new Complex(0.f, 0.f);
    public Complex currentSempl = new Complex(0.f, 0.f);
    public int difBit = 0;

    public TimeTuning(float boudRate){
        agc = new Agc(1.f, 60.f, -40.f, 36);
        zeroCrossDetector = new ZeroCrossDetector();
        outZeroCrossDetector = new ZeroCrossDetector();
        nco = new Nco(2.f * (float)Math.PI * boudRate / 3000.f);
        timeFilter = new  MovingAverage(3001);
        lineDelay = new LineDelay(3000 / 2);
        signalLineDelay = new LineDelay((3000 / 2) + 4);
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
            this.currentSempl = agc.update(outDelaySempl);

//            this.currentSempl = new Complex(outDelaySempl.getReal(), outDelaySempl.getImaginary());

            this.difBit = (currentSempl.getReal() * lastSempl.getReal() < 0.f)? 1 : 0;
            lastSempl = new Complex(currentSempl.getReal(), currentSempl.getImaginary());
        }

        return crossOut;
    }

    private MovingAverage levelAvg = new MovingAverage(100);


    private float level(Complex sempl){

        Complex reference = new Complex(Math.signum(sempl.getReal()), 0.f);

        double a = reference.getReal() - sempl.getReal();
        double b = reference.getImaginary() - sempl.getImaginary();
        Complex dif = new Complex(a, b);

        float r = (float)dif.abs();

        if(r > 2.f)
            r = 2.f;

        return (2.f - r) / 2;

    }


}
