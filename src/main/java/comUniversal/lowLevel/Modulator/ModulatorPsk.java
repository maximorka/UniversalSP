package comUniversal.lowLevel.Modulator;

import comUniversal.util.Complex;

import java.util.Random;

public class ModulatorPsk {

    private int[] sinchroSequence = {1,1,0,0};
    private int index = 0;

    private Random random = new Random();
    private SymbolSource symbolSource;
    private float relativeBaudeRate = 0.f;
    private float symbolPhase = 0.f;
    private int symbol = 0;
    private Complex[] constellation = {
            new Complex(1.f, 0.f),
            new Complex(-1.f, 0.f)
    };

    public void setSymbolSource(SymbolSource symbolSource){
        this.symbolSource = symbolSource;
    }

    public void setRelativeBaudeRate(float relativeBaudeRate){
        this.relativeBaudeRate = relativeBaudeRate;
    }

    public void symbolUpdate(){
        if(symbolSource != null)
            symbol = symbolSource.symbol();
        // for testing
        else{
            symbol = random.nextInt(2);
            //symbol = sinchroSequence[index % 4];
            //index++;
        }
    }

    public Complex getSempl(){
        if(symbolPhase >= 1.f) {
            symbolPhase -= 1.f;
            symbolUpdate();
        }
        symbolPhase += relativeBaudeRate;

        return constellation[symbol];
    }

}
