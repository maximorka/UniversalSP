package comUniversal.lowLevel.Demodulator;

import org.apache.commons.math3.complex.Complex;

public interface DemOut {
    void data(int difBit,Complex sempl);
}
