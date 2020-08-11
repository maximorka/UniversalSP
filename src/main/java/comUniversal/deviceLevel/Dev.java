package comUniversal.deviceLevel;

import comUniversal.BitLevel.GroupAdd;
import comUniversal.BitLevel.InfAdd;
import comUniversal.BitLevel.decoder.KylymDecoder;
import comUniversal.Core;
import comUniversal.lowLevel.BufferController.BufferController;
import comUniversal.lowLevel.Debuger.Debuger;
import comUniversal.lowLevel.Demodulator.OptimalNonCoherent;
import comUniversal.lowLevel.DriverEthernet.EthernetDriver;
import comUniversal.lowLevel.DriverHorizon.DriverHorizon;
import comUniversal.lowLevel.DriverHorizon.Mode;
import comUniversal.lowLevel.DriverHorizon.Width;
import comUniversal.lowLevel.Modulator.ModulatorPsk;
import org.apache.commons.math3.complex.Complex;

import java.io.IOException;

public class Dev extends Program {
    private Update update;
    public GroupAdd groupAdd;
    public InfAdd infAdd;
    public EthernetDriver ethernetDriver;
    private Debuger debuger;
    public ModulatorPsk modulatorPsk;
    public BufferController bufferController;
    public DriverHorizon driverHorizon;
    public OptimalNonCoherent optimalNonCoherentD�modulatorPsk100;
    public OptimalNonCoherent optimalNonCoherentD�modulatorPsk250;
    public KylymDecoder kylymDecoder100;
    public KylymDecoder kylymDecoder250;
    public Dev (String settings){
        this.ethernetDriver = new EthernetDriver();
        driverHorizon = new DriverHorizon();
        bufferController = new BufferController(3000);
    }
    public Dev(){

        this.ethernetDriver = new EthernetDriver();
        try {debuger = new Debuger();} catch (
                IOException e) {
            System.out.println(e);
        }
        infAdd = new InfAdd();
        groupAdd = new GroupAdd();
        driverHorizon = new DriverHorizon();
        optimalNonCoherentD�modulatorPsk100 = new OptimalNonCoherent(100);
        kylymDecoder100 = new KylymDecoder(100);

        optimalNonCoherentD�modulatorPsk250 = new OptimalNonCoherent(250);
        kylymDecoder250 = new KylymDecoder(250);

        modulatorPsk = new ModulatorPsk();

        modulatorPsk.setRelativeBaudeRate(250.f / 3000.f);

        bufferController = new BufferController(3000);
        bufferController.addTransferListener(sample -> driverHorizon.ducSetIq(sample));
        driverHorizon.addDucBufferPercent(percent -> bufferController.updatePercent(percent));
        bufferController.setSources(() -> modulatorPsk.getSempl());

        driverHorizon.addDdcMode(data -> Core.getCore().receiverUPSWindowUI.getModeRx(data));
        driverHorizon.addDdcWidth(data -> Core.getCore().receiverUPSWindowUI.getWidthRx(data));
        driverHorizon.addDdcFrequency(data -> Core.getCore().receiverUPSWindowUI.getFrequencyRx(data));

        driverHorizon.addDdcIQ(sempl -> optimalNonCoherentD�modulatorPsk100.demodulate(sempl));
        driverHorizon.addDdcIQ(sempl -> optimalNonCoherentD�modulatorPsk250.demodulate(sempl));
        //driverHorizon.addDdcIQ(sempl -> debuger.show(new Complex(sempl.re, sempl.im)));
//optimalNonCoherentD�modulatorPsk100.addListenerIq(sempl -> debuger.show(sempl));

        optimalNonCoherentD�modulatorPsk100.addSemplListener((difBit, sempl) -> kylymDecoder100.addData(difBit, sempl));
        optimalNonCoherentD�modulatorPsk250.addSemplListener((difBit, sempl) -> kylymDecoder250.addData(difBit, sempl));

        //modulatorPsk.setSymbolSource(() -> groupAdd.getBit());

        modulatorPsk.setSymbolSource(() -> infAdd.getBit());
//optimalNonCoherentD�modulatorPsk250.addListenerSymbol(data -> kylymDecoder250.addData(data));
        kylymDecoder100.addMessageListener(data -> Core.getCore().informationWindow.setTextMessage(data));
        kylymDecoder250.addMessageListener(symbol -> Core.getCore().informationWindow.setTextMessage(symbol));

        kylymDecoder100.addStartRadiogramListener(() -> Core.getCore().informationWindow.enterTime());
        kylymDecoder250.addStartRadiogramListener(() -> Core.getCore().informationWindow.enterTime());

        optimalNonCoherentD�modulatorPsk100.addFrequencyListener(f -> Core.getCore().informationWindow.setFreq(f));
        optimalNonCoherentD�modulatorPsk250.addFrequencyListener(f -> Core.getCore().informationWindow.setFreq(f));

        kylymDecoder100.addAlgoritmListener((algorit, speed) -> Core.getCore().informationWindow.setAlgoritm(algorit, speed));
        kylymDecoder250.addAlgoritmListener((algorit, speed) -> Core.getCore().informationWindow.setAlgoritm(algorit, speed));
        update = new Update();
        update.start();
    }

    public boolean conect(String typeDevice){
        int port = typeDevice.equals("��������")?80:81;
       // String rm = Core.getCore().receiverUPSWindowUI.getRM();
        System.out.println("Port: "+port);
        String ip = Core.getCore().receiverUPSWindowUI.item.get("ip");
        boolean stateCon = ethernetDriver.doInit(ip, port);
        if(stateCon){
            ethernetDriver.clearReceiverListener();
            ethernetDriver.addReceiverListener(data -> driverHorizon.parse(data));
            driverHorizon.addTransferListener(data -> ethernetDriver.writeBytes(data));
            driverHorizon.ddcSetWidth(Width.kHz_3);
            driverHorizon.ddcSetMode(Mode.ENABLE);
            driverHorizon.ducSetWidth(Width.kHz_3);
            driverHorizon.ducSetMode(Mode.ENABLE);
        }
        return stateCon;
    }
    public boolean conect(String ip, int port){
        boolean stateCon = ethernetDriver.doInit(ip, port);
        if(stateCon){
            ethernetDriver.clearReceiverListener();
            ethernetDriver.addReceiverListener(data -> driverHorizon.parse(data));
            driverHorizon.addTransferListener(data -> ethernetDriver.writeBytes(data));
        }
        return stateCon;
    }
    public void disConect(String typeDevice){

        driverHorizon.ducSetMode(Mode.DISABLE);
        driverHorizon.ddcSetMode(Mode.DISABLE);
//        kylymDecoder100.setRunning(false);
        //kylymDecoder250.setRunning(false);
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
        int port = typeDevice.equals("��������")?80:81;
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
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
