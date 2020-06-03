package comUniversal.lowLevel.Demodulator;

import comUniversal.util.MyComplex;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.ArrayList;
import java.util.List;

public class OptimalNonCoherent {

    private AutomaticFrequencyTuning automaticFrequencyTuning;
    private MovingAverage channelFilter;
    private LineDelay lineDelay;
    private Clocker clocker;


    public OptimalNonCoherent(float relativeBaudRate){

        automaticFrequencyTuning = new AutomaticFrequencyTuning();
        lineDelay = new LineDelay((int)(1.f/relativeBaudRate));
        channelFilter = new MovingAverage((int)(1.f/relativeBaudRate));
        clocker = new Clocker(relativeBaudRate);
    }

    public void setRelativeBaudRate(float relativeBaudRate){
        lineDelay = new LineDelay((int)(1.f/relativeBaudRate));
        channelFilter = new MovingAverage((int)(1.f/relativeBaudRate));
        clocker.setRelativeBaudRate(relativeBaudRate);
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
    private void toListenersIq(MyComplex sempl){
        if(!listeners.isEmpty())
            for(IqOutDebug listener: listeners)
                listener.sempl(sempl);
    }


    public void demodulate(MyComplex sempl){

        Complex inSempl = new Complex(sempl.re, sempl.im);

        Complex outAft = automaticFrequencyTuning.tuning(inSempl);

        Complex outCf = channelFilter.average(outAft);

        Complex outLd = lineDelay.delay(outCf);

        Complex outLd_ = outLd.conjugate();

        Complex mul = outLd_.multiply(outCf);

        if(clocker.update(mul))
            toListenersSymbol(clocker.getBit());


        toListenersIq(sempl);

    }

    private MyComplex mixer(MyComplex x, MyComplex y){
        return new MyComplex(x.re * y.re - x.im * y.im, x.im * y.re + x.re * y.im);
    }


}

class Pll{

    public Vco vco;
    private LoopFilter loopFilter;

    private MyComplex mixer(MyComplex x, MyComplex y){
        return new MyComplex(x.re * y.re - x.im * y.im, x.im * y.re + x.re * y.im);
    }

    public Pll(){
        vco = new Vco();
        loopFilter = new LoopFilter(0.01f, 0.000001f, 2.f* (float)Math.PI * 500.f / 48000.f);
    }

    private float phaseDetect(MyComplex sempl){
        if(sempl.re == 0.f){
            return 0.f;
        } else {
            return (float) Math.atan(sempl.im / sempl.re);
        }
    }

    public MyComplex add(MyComplex sempl){
        MyComplex gen = vco.generate();
        gen.im *= -1.f;
        MyComplex out = mixer(sempl, gen);
        float error = phaseDetect(out);
        float errorLoopFilter = loopFilter.update(error);
        vco.update(errorLoopFilter);
        return out;
    }

}

class Vco{

    private float phaseAccum = 0.f;
    private MyComplex out = new MyComplex(1.f, 0.f);

    public MyComplex getOut(){
        return out;
    }

    public MyComplex generate(){
        out = new MyComplex((float)Math.cos(phaseAccum), (float)Math.sin(phaseAccum));
        return out;
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

    public Complex filter(Complex sempl){
        System.arraycopy(lineDelay, 1, lineDelay, 0, lineDelay.length - 1);
        lineDelay[lineDelay.length - 1] = sempl;
        float re = 0.f, im = 0.f;
        for(int i = 0; i < lineDelay.length; i++){
            re += lineDelay[i].getReal() * coefficients[i];
            im += lineDelay[i].getImaginary() * coefficients[i];
        }
        return new Complex(re, im);
    }

}

class AutomaticFrequencyTuning{

    private Bpf bpf;
    private FastFourierTransformer fft;
    private LineDelay delayForFft, delayForBpf;
    private float accumVco, phaseVco;
    private int timeTuning, semplCounter;
    private Complex[] inFft, outFft, collect;

    public AutomaticFrequencyTuning(){
        int lengthFft = 8192;
        timeTuning = 3000 / 100;
        semplCounter = 0;
        accumVco = 0.f;
        phaseVco = 0.f;
        bpf = new Bpf(bpfCoefficients);
        delayForFft = new LineDelay(lengthFft/2);
        delayForBpf = new LineDelay(bpfCoefficients.length/2);
        fft = new FastFourierTransformer(DftNormalization.STANDARD);
        inFft = new Complex[lengthFft];
        outFft = new Complex[lengthFft];
        collect = new Complex[timeTuning];
        for(int i = 0; i < lengthFft; i++){
            inFft[i] = new Complex(0.f, 0.f);
            outFft[i] = new Complex(0.f, 0.f);
        }
    }


    public Complex tuning(Complex sempl){

        Complex outBpf = bpf.filter(sempl);

        Complex outMix = outBpf.multiply(outBpf);

        collect[semplCounter++] = outMix;

        Complex vco = new Complex((float)Math.cos(accumVco), (float)Math.sin(accumVco));

        Complex delayBpf = delayForBpf.delay(sempl);
        Complex delayFft = delayForFft.delay(delayBpf);

        Complex result = delayFft.multiply(vco);

        if(semplCounter == timeTuning){

            semplCounter = 0;

            System.arraycopy(inFft, collect.length, inFft, 0, inFft.length - collect.length);
            System.arraycopy(collect,0, inFft,inFft.length - collect.length, collect.length);

            outFft = fft.transform(inFft, TransformType.FORWARD);

            int index = 0;
            float energy = 0;

            for(int i = 0; i < outFft.length; i++){
                if(outFft[i].abs() > energy) {
                    energy = (float) outFft[i].abs();
                    index = i;
                }
            }

            if(index > outFft.length / 2)
                index -= outFft.length;

            float relativeFrequency = (float)index / (float) outFft.length / 2.f;

            //System.out.println("frequency = " + 3000.f * relativeFrequency);

            phaseVco = 2.f * (float)Math.PI * -relativeFrequency;

        }

        accumVco += phaseVco;
        accumVco %= 2.f * (float)Math.PI;

        return result;
    }

    private float[] bpfCoefficients = {
            -17.96633661712444050E-6f,
            32.46782273589473530E-6f,
            100.1355657630664950E-6f,
            53.29510640897244400E-6f,
            -113.2255938476920110E-6f,
            -206.0143222136724150E-6f,
            -52.70133065006471900E-6f,
            230.9042106917715390E-6f,
            305.3274777915445950E-6f,
            7.048739387696470790E-6f,
            -380.4819071996450930E-6f,
            -384.0886685261700680E-6f,
            90.54907242888177170E-6f,
            553.2233871074464560E-6f,
            427.4148718975955030E-6f,
            -243.8877836602549680E-6f,
            -736.7528045346923590E-6f,
            -420.4594223022256760E-6f,
            452.9318089416035490E-6f,
            915.3698333623467530E-6f,
            349.4538554270355350E-6f,
            -713.2278096376010130E-6f,
            -0.001070611169870332f,
            -202.8059700901564500E-6f,
            0.001015520010766139f,
            0.001182045181675499f,
            -27.80498204471946270E-6f,
            -0.001345605731819324f,
            -0.001228272789447314f,
            346.3980820326301570E-6f,
            0.001684457192830370f,
            0.001188093991742695f,
            -751.7867180947596350E-6f,
            -0.002008620884443837f,
            -0.001041787660635979f,
            0.001236856916427388f,
            0.002290889314644658f,
            772.4432371398388570E-6f,
            -0.001788074924333807f,
            -0.002501222584870641f,
            -367.2775366899847430E-6f,
            0.002385257533624068f,
            0.002607879850854013f,
            -181.1312890146513720E-6f,
            -0.003001621270965906f,
            -0.002578704072127850f,
            873.7569442404228540E-6f,
            0.003604105428536847f,
            0.002382488153323404f,
            -0.001704240724780265f,
            -0.004153938901099980f,
            -0.001990336897522012f,
            0.002658250489133729f,
            0.004607391302330209f,
            0.001376926819232365f,
            -0.003713102328931580f,
            -0.004916613029807749f,
            -521.5535221005276300E-6f,
            0.004837635169053608f,
            0.005030422650719344f,
            -591.1589494653901510E-6f,
            -0.005992288181639275f,
            -0.004894834328032874f,
            0.001971036774768433f,
            0.007129272791566537f,
            0.004453014600802527f,
            -0.003622818971330974f,
            -0.008192640858041890f,
            -0.003644177938684561f,
            0.005547593215465602f,
            0.009117896789478610f,
            0.002400589554264672f,
            -0.007745691221564811f,
            -0.009830511618982636f,
            -641.1472118351719020E-6f,
            0.010221962397785053f,
            0.010242096067591241f,
            -0.001741519686475492f,
            -0.012995344103421409f,
            -0.010241617958741674f,
            0.004907160411858899f,
            0.016117135947611740f,
            0.009675579615219991f,
            -0.009123629534131853f,
            -0.019709247518545352f,
            -0.008301153091872824f,
            0.014897708398431304f,
            0.024055415912480185f,
            0.005663071539164637f,
            -0.023330375624796916f,
            -0.029861916309501923f,
            -706.4049557271273440E-6f,
            0.037323784175190132f,
            0.039234830188732597f,
            -0.009882111655156874f,
            -0.067612656241084909f,
            -0.061672864768133044f,
            0.045281313750387471f,
            0.211156804598513215f,
            0.335731002757630015f,
            0.335731002757630015f,
            0.211156804598513215f,
            0.045281313750387471f,
            -0.061672864768133044f,
            -0.067612656241084909f,
            -0.009882111655156874f,
            0.039234830188732597f,
            0.037323784175190132f,
            -706.4049557271273440E-6f,
            -0.029861916309501923f,
            -0.023330375624796916f,
            0.005663071539164637f,
            0.024055415912480185f,
            0.014897708398431304f,
            -0.008301153091872824f,
            -0.019709247518545352f,
            -0.009123629534131853f,
            0.009675579615219991f,
            0.016117135947611740f,
            0.004907160411858899f,
            -0.010241617958741674f,
            -0.012995344103421409f,
            -0.001741519686475492f,
            0.010242096067591241f,
            0.010221962397785053f,
            -641.1472118351719020E-6f,
            -0.009830511618982636f,
            -0.007745691221564811f,
            0.002400589554264672f,
            0.009117896789478610f,
            0.005547593215465602f,
            -0.003644177938684561f,
            -0.008192640858041890f,
            -0.003622818971330974f,
            0.004453014600802527f,
            0.007129272791566537f,
            0.001971036774768433f,
            -0.004894834328032874f,
            -0.005992288181639275f,
            -591.1589494653901510E-6f,
            0.005030422650719344f,
            0.004837635169053608f,
            -521.5535221005276300E-6f,
            -0.004916613029807749f,
            -0.003713102328931580f,
            0.001376926819232365f,
            0.004607391302330209f,
            0.002658250489133729f,
            -0.001990336897522012f,
            -0.004153938901099980f,
            -0.001704240724780265f,
            0.002382488153323404f,
            0.003604105428536847f,
            873.7569442404228540E-6f,
            -0.002578704072127850f,
            -0.003001621270965906f,
            -181.1312890146513720E-6f,
            0.002607879850854013f,
            0.002385257533624068f,
            -367.2775366899847430E-6f,
            -0.002501222584870641f,
            -0.001788074924333807f,
            772.4432371398388570E-6f,
            0.002290889314644658f,
            0.001236856916427388f,
            -0.001041787660635979f,
            -0.002008620884443837f,
            -751.7867180947596350E-6f,
            0.001188093991742695f,
            0.001684457192830370f,
            346.3980820326301570E-6f,
            -0.001228272789447314f,
            -0.001345605731819324f,
            -27.80498204471946270E-6f,
            0.001182045181675499f,
            0.001015520010766139f,
            -202.8059700901564500E-6f,
            -0.001070611169870332f,
            -713.2278096376010130E-6f,
            349.4538554270355350E-6f,
            915.3698333623467530E-6f,
            452.9318089416035490E-6f,
            -420.4594223022256760E-6f,
            -736.7528045346923590E-6f,
            -243.8877836602549680E-6f,
            427.4148718975955030E-6f,
            553.2233871074464560E-6f,
            90.54907242888177170E-6f,
            -384.0886685261700680E-6f,
            -380.4819071996450930E-6f,
            7.048739387696470790E-6f,
            305.3274777915445950E-6f,
            230.9042106917715390E-6f,
            -52.70133065006471900E-6f,
            -206.0143222136724150E-6f,
            -113.2255938476920110E-6f,
            53.29510640897244400E-6f,
            100.1355657630664950E-6f,
            32.46782273589473530E-6f,
            -17.96633661712444050E-6f
    };

}