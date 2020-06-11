package comUniversal.deviceLevel;

import comUniversal.BitLevel.decoder.KylymDecoder;
import comUniversal.Core;
import comUniversal.lowLevel.Debuger.Debuger;
import comUniversal.lowLevel.Demodulator.OptimalNonCoherent;
import comUniversal.lowLevel.DriverEthernet.EthernetDriver;
import comUniversal.lowLevel.DriverHorizon.DriverHorizon;
import comUniversal.lowLevel.DriverHorizon.Mode;
import comUniversal.lowLevel.DriverHorizon.Width;
import org.apache.commons.math3.complex.Complex;

import java.io.IOException;

public class Dev extends Program {
    private Update update;
    public EthernetDriver ethernetDriver;
    private Debuger debuger;

    public DriverHorizon driverHorizon;
    public OptimalNonCoherent optimalNonCoherentDåmodulatorPsk100;
    public OptimalNonCoherent optimalNonCoherentDåmodulatorPsk250;
    public KylymDecoder kylymDecoder100;
    public KylymDecoder kylymDecoder250;

    public Dev(){
        this.ethernetDriver = new EthernetDriver();

        try {debuger = new Debuger();} catch (
                IOException e) {
            System.out.println(e);
        }

        driverHorizon = new DriverHorizon();
        optimalNonCoherentDåmodulatorPsk100 = new OptimalNonCoherent(100.f/3000.f);
        kylymDecoder100 = new KylymDecoder(100);
        kylymDecoder100.setRunning(true);
        optimalNonCoherentDåmodulatorPsk250 = new OptimalNonCoherent(250.f/3000.f);
        kylymDecoder250 = new KylymDecoder(250);
        kylymDecoder250.setRunning(true);

        driverHorizon.addDdcMode(data -> Core.getCore().receiverUPSWindowUI.getModeRx(data));
        driverHorizon.addDdcWidth(data -> Core.getCore().receiverUPSWindowUI.getWidthRx(data));
        driverHorizon.addDdcFrequency(data -> Core.getCore().receiverUPSWindowUI.getFrequencyRx(data));

        driverHorizon.addDdcIQ(sempl -> optimalNonCoherentDåmodulatorPsk100.demodulate(sempl));
        driverHorizon.addDdcIQ(sempl -> optimalNonCoherentDåmodulatorPsk250.demodulate(sempl));
        driverHorizon.addDdcIQ(sempl -> debuger.show(new Complex(sempl.re, sempl.im)));

        optimalNonCoherentDåmodulatorPsk100.addListenerSymbol(data -> kylymDecoder100.addData(data));
        optimalNonCoherentDåmodulatorPsk250.addListenerSymbol(data -> kylymDecoder250.addData(data));

        update = new Update();
        update.start();
    }

    public boolean conect(String typeDevice){
        int port = typeDevice.equals("Ãîðèçîíò")?80:81;
        boolean stateCon = ethernetDriver.doInit(Core.getCore().receiverUPSWindowUI.getIP(), port);
        if(stateCon){
            ethernetDriver.clearReceiverListener();
            ethernetDriver.addReceiverListener(data -> driverHorizon.parse(data));
            driverHorizon.addTransferListener(data -> ethernetDriver.writeBytes(data));
            driverHorizon.ddcSetWidth(Width.kHz_3);
            driverHorizon.ddcSetMode(Mode.ENABLE);
        }
        return stateCon;
    }
    public void disConect(String typeDevice){

        driverHorizon.ducSetMode(Mode.DISABLE);
        driverHorizon.ddcSetMode(Mode.DISABLE);
        kylymDecoder100.setRunning(false);
        kylymDecoder250.setRunning(false);
        ethernetDriver.closeSocket();
    }
    public void getParamsRx() {
        if(driverHorizon!= null){
            driverHorizon.ddcGetFrequency();
            driverHorizon.ddcGetWidth();
            driverHorizon.ddcGetMode();
        }
    }

    @Override
    public void fromReceiver(Complex sample) {

    }

    @Override
    public void toTransiver(Complex sample) {

    }

    @Override
    public boolean connect(String typeDevice) {
        int port = typeDevice.equals("Ãîðèçîíò")?80:81;
        boolean stateCon = ethernetDriver.doInit(Core.getCore().receiverUPSWindowUI.getIP(), port);
        if(stateCon){
            ethernetDriver.clearReceiverListener();
            ethernetDriver.addReceiverListener(data -> driverHorizon.parse(data));
            driverHorizon.addTransferListener(data -> ethernetDriver.writeBytes(data));
            driverHorizon.ddcSetWidth(Width.kHz_3);
            driverHorizon.ddcSetMode(Mode.ENABLE);
        }
        return stateCon;
    }

    @Override
    public int getData(int[] data) {
        return 0;
    }

    class Update extends Thread {
        @Override
        public void run() {
            while (!ethernetDriver.isConect()){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (true) {
                getParamsRx();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
