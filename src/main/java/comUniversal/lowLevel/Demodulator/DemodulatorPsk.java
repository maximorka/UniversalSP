package comUniversal.lowLevel.Demodulator;

import comUniversal.util.Complex;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class DemodulatorPsk {

    private float baudeRate = 0.f;
    private float samplingFrequency = 0.f;
    private LineDelay lineDelay;
    private Movingaverage filter;
    private Clocker clocker;

    public DemodulatorPsk(float baudeRate, float samplingFrequency){
        this.baudeRate = baudeRate;
        this.samplingFrequency = samplingFrequency;
        this.lineDelay = new LineDelay((int)(this.samplingFrequency/this.baudeRate));
        this.filter = new Movingaverage((int)(this.samplingFrequency/this.baudeRate));
        this.clocker = new Clocker(this.baudeRate/this.samplingFrequency);
    }

    public void setParametrs(float baudeRate, float samplingFrequency) {
        this.baudeRate = baudeRate;
        this.samplingFrequency = samplingFrequency;
        this.lineDelay = new LineDelay((int) (this.samplingFrequency / this.baudeRate));
        this.filter = new Movingaverage((int) (this.samplingFrequency / this.baudeRate));
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

    public void demodulate(Complex sempl){

        Complex filtered = filter.update(sempl);

        Complex delay = lineDelay.delayer(filtered);

        Complex mixer = new Complex(0.f, 0.f);
        mixer.re = filtered.re * delay.re + filtered.im * delay.im;
        mixer.im = filtered.im * delay.re - filtered.re * delay.im;

        if(clocker.update(mixer))
            toListenersSymbol(clocker.symbol);
    }

}

class LineDelay{
    private Queue<Complex> line;
    private int delay = 0;

    public LineDelay(int delay){
        this.delay = delay;
        line = new ArrayDeque<Complex>();
        for(int i = 0; i < delay; i++)
            line.add(new Complex(0.f, 0.f));
    }

    public Complex delayer(Complex sempl){
        line.add(sempl);
        return line.poll();
    }
}

class Movingaverage{

    private Complex inrtegrator;
    private int lenght = 0;
    private LineDelay lineDelay;

    public Movingaverage(int lenght){
        this.lenght = lenght;
        this.lineDelay = new LineDelay(this.lenght);
        this.inrtegrator = new Complex(0.f, 0.f);
    }

    public Complex update(Complex sempl){
        Complex delayed = lineDelay.delayer(sempl);
        inrtegrator.re += sempl.re;
        inrtegrator.im += sempl.im;
        inrtegrator.re -= delayed.re;
        inrtegrator.im -= delayed.im;
        return new Complex(inrtegrator.re / lenght, inrtegrator.im / lenght);
    }
}

class Clocker{

    private String bitData = new String();
    private LoopFilter loopFilter = new LoopFilter();
    private float relativeBaudeRate = 0.f;
    private float timer = 0.f;
    private float halfRight = 0.f;
    private float halfLeft = 0.f;
    public int symbol = 0;
    private float timeError = 0.f;

    public Clocker(float relativeBaudeRate){
        this.relativeBaudeRate = relativeBaudeRate;
    }

    public boolean update(Complex sempl){
        boolean result = false;

        if(timer < 0.75f)
            halfLeft += sempl.re;

        if(timer >= 0.25f)
            halfRight += sempl.re;

        timer += relativeBaudeRate;
        timer += timeError;

        if (timer >= 1.f) {
            timer -= 1.f;
            result = true;
            timeError = Math.abs(halfRight) - Math.abs(halfLeft);
            timeError = Math.signum(timeError);
            timeError = loopFilter.update(timeError);
            halfRight = 0.f;
            halfLeft = 0.f;
            symbol = (sempl.re >= 0.f)? 0 : 1;


            char bit = (symbol==0)? '0' : '1';
            bitData += bit;
            if(bitData.length() == 100) {
                //System.out.println(bitData);
                bitData = new String();
            }

        }
        return result;
    }
}

class LoopFilter{

    private float integrator = 0.f;
    public float kp = 0.01f;
    public float ki = 0.0001f;
    public float upTheshhold = 0.0001f;
    public float downTheshhold = -0.0001f;

    public float update(float data){
        float result = integrator + data * kp;
        if(result > upTheshhold)
            result = upTheshhold;
        if(result < downTheshhold)
            result = downTheshhold;
        integrator += data * ki;
        if(integrator > upTheshhold)
            integrator = upTheshhold;
        if(integrator < downTheshhold)
            integrator = downTheshhold;
        return  result;
    }
}