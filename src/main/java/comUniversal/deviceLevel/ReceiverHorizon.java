package comUniversal.deviceLevel;

import comUniversal.lowLevel.DriverHorizon.DdcFrequency;
import comUniversal.lowLevel.DriverHorizon.DdcIQ;
import comUniversal.lowLevel.DriverHorizon.DriverHorizon;
import org.apache.commons.math3.complex.Complex;

import java.util.ArrayDeque;
import java.util.Queue;

public class ReceiverHorizon extends Receiver {
    private DriverHorizon driverHorizon;
    private int frequency = 0;
    private boolean isFrequencyUpdate = false;
    private DdcFrequency frequencyListener = this::updateFrequency;
    private Queue <Complex> data  = new ArrayDeque<>();

    private void add(Complex sample){
        data.add(sample);
    }

    public DdcIQ ddcIQ = sempl -> add(sempl);


    public ReceiverHorizon(){
        driverHorizon = new DriverHorizon();
        driverHorizon.addDdcFrequency(frequencyListener);
        driverHorizon.addDdcIQ(ddcIQ);
    }

    private void updateFrequency(int frequency){
        this.frequency = frequency;
        this.isFrequencyUpdate = true;
    }

    public void setFrequency(int frequency) {
        driverHorizon.ddcSetFrequency(frequency);
    }

    @Override
    public void getFrequency(int frequency) {

    }

    @Override
    public void setMode(int mode) {

    }

    public int getFrequency() {
        driverHorizon.ddcGetFrequency();
        while(!isFrequencyUpdate);
        isFrequencyUpdate = false;
        return frequency;
    }

    @Override
    public void toEthernet(byte data) {

    }

    public void fromEthernet(byte data) {
        driverHorizon.parse(data);
    }
    public int getSample(Complex[] dataSample) {
        int length = Math.min(dataSample.length, data.size());
        for (int i = 0; i <length; i++)
            dataSample[i] = data.poll();
        return length;
    }

}
