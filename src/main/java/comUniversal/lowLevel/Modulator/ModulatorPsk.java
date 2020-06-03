package comUniversal.lowLevel.Modulator;

import comUniversal.util.MyComplex;

import java.util.Random;

public class ModulatorPsk {

    private int[] sinchroSequence = {0,0,0,0,0,1, 0,0,0,1,1,1, 0,0,1,0,1,1, 0,0,1,1,0,1, 0,1,0,0,1,1, 0,1,0,1,0,1};
    //private int[] sinchroSequence = {0,0,0,1,1,1};

    private int index = 0;

    private Random random = new Random();
    private SymbolSource symbolSource;
    private float relativeBaudeRate = 0.f;
    private float symbolPhase = 0.f;
    private int symbol = -1;
    private MyComplex[] constellation = {
            new MyComplex(1.f, 0.f),
            new MyComplex(-1.f, 0.f)
    };

    public void setSymbolSource(SymbolSource symbolSource){
        this.symbolSource = symbolSource;
    }

    public void setRelativeBaudeRate(float relativeBaudeRate){
        this.relativeBaudeRate = relativeBaudeRate;
    }

    public void symbolUpdate(){
        if(symbolSource != null) {
            symbol = symbolSource.symbol();
           // if(symbol == -1) {
//                symbol = random.nextInt(2);
//                symbol = sinchroSequence[index % sinchroSequence.length];
//                index++;
          //  }
        }
    }

    public MyComplex getSempl(){
        if(symbolPhase >= 1.f) {
            symbolPhase -= 1.f;
            symbolUpdate();
        }
        symbolPhase += relativeBaudeRate;
        MyComplex result = new MyComplex(0.f, 0.f);
        if(symbol != -1)
            result = constellation[symbol];

        return result;
    }

}
