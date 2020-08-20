package comUniversal.lowLevel.Modulator;


import org.apache.commons.math3.complex.Complex;

import java.util.Random;

public class ModulatorPsk {

    private int[] sinchroSequence = {0,0,0,0,0,1, 0,0,0,1,1,1, 0,0,1,0,1,1, 0,0,1,1,0,1, 0,1,0,0,1,1, 0,1,0,1,0,1};
    private int[] meandr = {0, 1};
    //private int[] sinchroSequence = {0,0,0,1,1,1};


private int print =0;
    private int index = 0;
    private int counter = 0;

    private Random random = new Random();
    private SymbolSource symbolSource;
    private float relativeBaudeRate = 0.f;
    private float symbolPhase = 0.f;
    private int symbol = -1;
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
        if(symbolSource != null) {
            symbol = symbolSource.symbol();
           // if(symbol == -1) {
//                symbol = random.nextInt(2);
//                symbol = sinchroSequence[index % sinchroSequence.length];
//                index++;
          //  }
        } else {
            symbol = sinchroSequence[index % sinchroSequence.length];
            index++;
        }
    }

    public Complex getSempl(){

        Complex result;

        if(symbolPhase >= 1.f) {
            symbolPhase -= 1.f;
            symbolUpdate();
        }
        symbolPhase += relativeBaudeRate;

        if(symbol == -1) {
            result = new Complex(0.f, 0.f);
        } else {
            result = new Complex(constellation[symbol].getReal(), constellation[symbol].getImaginary());
        }
//        else {
//            result = constellation[counter % 2];
//            counter++;
//
//        }

//            System.out.println("Re = " + result.getReal());
//            System.out.println("Im = " + result.getImaginary());
//        System.out.print((int)result.getReal());
//        print++;
//        if(print==60){
//            System.out.println("");
//            print=0;
//        }
        return result;
    }

}
