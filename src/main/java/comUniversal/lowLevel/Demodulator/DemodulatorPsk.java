package comUniversal.lowLevel.Demodulator;

import comUniversal.util.MyComplex;
import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;
import java.util.List;

public class DemodulatorPsk {

    private float baudeRate = 0.f;
    private float samplingFrequency = 0.f;
    private LineDelay lineDelay;
    private MovingAverage filter;
    private Clocker clocker;

    public DemodulatorPsk(float baudeRate, float samplingFrequency){
        this.baudeRate = baudeRate;
        this.samplingFrequency = samplingFrequency;
        this.lineDelay = new LineDelay((int)(this.samplingFrequency/this.baudeRate));
        this.filter = new MovingAverage((int)(this.samplingFrequency/this.baudeRate));
        this.clocker = new Clocker(this.baudeRate/this.samplingFrequency);
    }

    public void setParametrs(float baudeRate, float samplingFrequency) {
        this.baudeRate = baudeRate;
        this.samplingFrequency = samplingFrequency;
        this.lineDelay = new LineDelay((int) (this.samplingFrequency / this.baudeRate));
        this.filter = new MovingAverage((int) (this.samplingFrequency / this.baudeRate));
        this.clocker = new Clocker(this.baudeRate / this.samplingFrequency);
    }
    private List<Symbol> symbol = new ArrayList<>();
    public void addListenerSymbol(Symbol listener){symbol.add(listener);}
    public void clearListenersSymbol(){symbol.clear();}
    private void toListenersSymbol(int data) {
        if (!symbol.isEmpty())
            for (Symbol listener : symbol) {
                //System.out.print(data);
                listener.symbol(data);
            }
    }

    public void demodulate(MyComplex sempl){

        //MyComplex filtered = filter.average(sempl);

        //MyComplex delay = lineDelay.delay(filtered);

        MyComplex mixer = new MyComplex(0.f, 0.f);
//        mixer.re = filtered.re * delay.re + filtered.im * delay.im;
//        mixer.im = filtered.im * delay.re - filtered.re * delay.im;

//        if(clocker.update(mixer))
//            toListenersSymbol(clocker.getBit());
    }

}

class LineDelay{

    private Complex[] line;

    public LineDelay(int window){
        line = new Complex[window];
        for(int i = 0; i < line.length; i++)
            line[i] = new Complex(0.f, 0.f);
    }

    public Complex delay(Complex sempl){
        Complex out = new Complex(line[0].getReal(), line[0].getImaginary());
        System.arraycopy(line, 1, line, 0, line.length - 1);
        line[line.length - 1] = sempl;
        return out;
    }
}

class MovingAverage{

    private Complex integrator;
    private LineDelay lineDelay;
    private int window;

    public MovingAverage(int window){
        this.window = window;
        lineDelay = new LineDelay(window);
        integrator = new Complex(0.f, 0.f);
    }

    public Complex average(Complex sempl){
        integrator = integrator.add(sempl);
        Complex last = lineDelay.delay(sempl);
        integrator = integrator.subtract(last);
        return new Complex(integrator.getReal() / window, integrator.getImaginary() / window);
    }
}

class Clocker{

    private String bitData;
    private LoopFilter loopFilter;
    private float relativeBaudRate, timer, timeError, halfRight, halfLeft;
    private Complex lastSempl;
    private int symbol;
    private int lastBit = 0;

    public Clocker(float relativeBaudRate){
        this.relativeBaudRate = relativeBaudRate;
        this.loopFilter = new LoopFilter(0.0001f, 0.000001f, 0.1f/3000.f);
        this.timer = 0.f;
        this.halfRight = 0.f;
        this.halfLeft = 0.f;
        this.lastSempl = new Complex(0.f, 0.f);
        this.timeError = 0.f;
        this.bitData = new String();
        this.symbol = 0;
    }

    public void setRelativeBaudRate(float relativeBaudRate){
        this.relativeBaudRate = relativeBaudRate;
        this.timer = 0.f;
        this.halfRight = 0.f;
        this.halfLeft = 0.f;
        this.lastSempl = new Complex(0.f, 0.f);
        this.timeError = 0.f;
        this.bitData = new String();
        this.symbol = 0;
    }

    public int getBit(){
        return this.symbol;
    }

    public float energy = 0;

    public boolean update(Complex sempl){
        boolean result = false;

        if(timer < 0.75f)
            halfLeft += sempl.getReal();

        if(timer >= 0.25f)
            halfRight += sempl.getReal();

        timer += relativeBaudRate;
        timer += timeError;


        if (timer >= 1.f) {
            timer -= 1.f;
            result = true;
            timeError = Math.abs(halfRight) - Math.abs(halfLeft);
            timeError = Math.signum(timeError);
            timeError = loopFilter.update(timeError);
            halfRight = 0.f;
            halfLeft = 0.f;


            int currentBit = (sempl.getReal() >= 0.f)? 0 : 1;

            symbol = (currentBit == lastBit)? 0 : 1;

            lastBit = currentBit;
//            Complex dif = sempl.multiply(lastSempl);
//            lastSempl = new Complex(sempl.getReal(), sempl.getImaginary());


//            MyComplex out = new MyComplex(0.f, 0.f);
//            out.re = sempl.re * lastSempl.re + sempl.im * lastSempl.im;
//            out.im = sempl.im * lastSempl.re - sempl.re * lastSempl.im;
//            lastSempl = new MyComplex(sempl.re, sempl.im);

//            symbol = (dif.getReal() >= 0.f)? 0 : 1;
//
//            char s = (currentBit == 0)? '0' : '1';
//            bitData += s;
//            if(bitData.length() == 12*6) {
//                System.out.println(bitData);
//                bitData = new String();
//            }

        }
        return result;
    }
}

class LoopFilter{

    public float integrator, kp, ki, theshhold;

    public LoopFilter(float kp, float ki, float theshhold){
        this.kp = kp;
        this.ki = ki;
        this.theshhold = theshhold;
        this.integrator = 0.f;
    }

    public float update(float data){
        float result = integrator + data * kp;
        integrator += data * ki;
        if(Math.abs(integrator) > theshhold)
            integrator = Math.signum(integrator) * theshhold;
//        if(Math.abs(result) > theshhold)
//            result = Math.signum(result) * theshhold;
        return  result;
    }
}