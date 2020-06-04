package comUniversal;

import comUniversal.BitLevel.GroupAdd;
import comUniversal.lowLevel.BufferController.BufferController;
import comUniversal.lowLevel.Debuger.Debuger;
import comUniversal.lowLevel.Demodulator.OptimalNonCoherentDеmodulatorPsk;
import comUniversal.lowLevel.DriverEthernet.EthernetDriver;
import comUniversal.lowLevel.DriverHorizon.DriverHorizon;
import comUniversal.lowLevel.DriverHorizon.Mode;
import comUniversal.lowLevel.DriverHorizon.Width;
import comUniversal.lowLevel.Modulator.ModulatorPsk;
import comUniversal.ui.InformationWindow;
import comUniversal.ui.ReceiverUPSWindowUI;
import comUniversal.ui.TransmitterUPSWindowUI;

import java.io.IOException;

public class Device {
    private Debuger debuger;
    public GroupAdd groupAdd;
    public EthernetDriver ethernetDriver;
    public DriverHorizon driverHorizon;
    public BufferController bufferController;
    public ModulatorPsk modulatorPsk;
    //public DemodulatorPsk demodulatorPsk;
    public OptimalNonCoherentDеmodulatorPsk optimalNonCoherentDеmodulatorPsk;
    public KylymDecoder kylymDecoder;

    public Device(){
        this.ethernetDriver = new EthernetDriver();
        try {debuger = new Debuger();} catch (IOException e) {}
    }

    public void sendCommand(String command){
    }
    public boolean initRxTx(String typeDevice,
                            String typeProg,
                            String typeModulator,
                            String speedModulator,
                            String typeDemodulator,
                            String speedDemodulator,
                            TransmitterUPSWindowUI transmitterUPSWindowUI,
                            ReceiverUPSWindowUI receiverUPSWindowUI,
                            InformationWindow informationWindow,
                            String ip, boolean state ){
        boolean stateCon = false;
        if(state) {
            int port = typeDevice.equals("Горизонт")?80:81;
            stateCon = ethernetDriver.doInit(ip, port);
            driverHorizon = new DriverHorizon();

            groupAdd = new GroupAdd();
            kylymDecoder = new KylymDecoder();
            kylymDecoder.setRunning(true);

            modulatorPsk = new ModulatorPsk();

            modulatorPsk.setRelativeBaudeRate(100.f / 3000.f);

            bufferController = new BufferController(3000);

            //demodulatorPsk = new DemodulatorPsk(100.f, 3000.f);
            optimalNonCoherentDеmodulatorPsk = new OptimalNonCoherentDеmodulatorPsk(100.f / 3000.f);

            ethernetDriver.clearReceiverListener();
            ethernetDriver.addReceiverListener(data -> driverHorizon.parse(data));
            driverHorizon.addTransferListener(data -> ethernetDriver.writeBytes(data));

            driverHorizon.addDucMode(data -> transmitterUPSWindowUI.getModeTx(data));
            driverHorizon.addDucWidth(data -> transmitterUPSWindowUI.getWidthTx(data));
            driverHorizon.addDucFrequency(data -> transmitterUPSWindowUI.getFrequencyTx(data));
            driverHorizon.addDucBufferPercent(data -> transmitterUPSWindowUI.updatePercent(data));

            driverHorizon.addDdcMode(data -> receiverUPSWindowUI.getModeRx(data));
            driverHorizon.addDdcWidth(data -> receiverUPSWindowUI.getWidthRx(data));
            driverHorizon.addDdcFrequency(data -> receiverUPSWindowUI.getFrequencyRx(data));

//      driverHorizon.addEthernetSettings((ip, mask, port, gateWay) -> transiverUPSWindow.updateEthernet(ip, mask, port, gateWay));
            bufferController.addTransferListener(sample -> driverHorizon.ducSetIq(sample));
            driverHorizon.addDucBufferPercent(percent -> bufferController.updatePercent(percent));
            bufferController.setSources(() -> modulatorPsk.getSempl());
            //driverHorizon.addDdcIQ(sempl -> demodulatorPsk.demodulate(sempl));
            driverHorizon.addDdcIQ(sempl -> optimalNonCoherentDеmodulatorPsk.demodulate(sempl));
            driverHorizon.addDdcIQ(sempl -> debuger.show(sempl));
            //optimalNonCoherentDеmodulatorPsk.addListenerIq(sempl -> debuger.show(sempl));


            //demodulatorPsk.addListenerSymbol(data -> kylymDecoder.addData(data));
            optimalNonCoherentDеmodulatorPsk.addListenerSymbol(data -> kylymDecoder.addData(data));

            modulatorPsk.setSymbolSource(() -> groupAdd.getBit());
            groupAdd.addRadiogramPercentListener(percent -> informationWindow.updatePercentRadiogram(percent));
            driverHorizon.ducSetWidth(Width.kHz_3);
            driverHorizon.ddcSetWidth(Width.kHz_3);

            driverHorizon.ducSetMode(Mode.ENABLE);
            driverHorizon.ddcSetMode(Mode.ENABLE);
        }
        else {
            closeDeviceRxTx();
            stateCon=ethernetDriver.closeSocket();
        }
        return stateCon;
    }

    public boolean initTx(String typeDevice,
                          String typeProg,
                          String typeModulator,
                          String speedModulator,
                          TransmitterUPSWindowUI transmitterUPSWindowUI,
                          InformationWindow informationWindow,
                          String ip,
                          boolean state) {
        boolean stateCon = false;
        if (state) {
            int port = typeDevice.equals("Горизонт")?80:81;
            System.out.println("ïnitTx");
            stateCon = ethernetDriver.doInit(ip, port);
            driverHorizon = new DriverHorizon();
            groupAdd = new GroupAdd();
            modulatorPsk = new ModulatorPsk();
            modulatorPsk.setRelativeBaudeRate(100.f / 3000.f);
            bufferController = new BufferController(3000);

            ///demodulatorPsk = new DemodulatorPsk(100.f,3000.f);
            ethernetDriver.clearReceiverListener();
            ethernetDriver.addReceiverListener(data -> driverHorizon.parse(data));
            driverHorizon.addTransferListener(data -> ethernetDriver.writeBytes(data));

            driverHorizon.addDucMode(data -> transmitterUPSWindowUI.getModeTx(data));
            driverHorizon.addDucWidth(data -> transmitterUPSWindowUI.getWidthTx(data));
            driverHorizon.addDucFrequency(data -> transmitterUPSWindowUI.getFrequencyTx(data));
            driverHorizon.addDucBufferPercent(data -> transmitterUPSWindowUI.updatePercent(data));


//      driverHorizon.addEthernetSettings((ip, mask, port, gateWay) -> transiverUPSWindow.updateEthernet(ip, mask, port, gateWay));
            bufferController.addTransferListener(sample -> driverHorizon.ducSetIq(sample));
            driverHorizon.addDucBufferPercent(percent -> bufferController.updatePercent(percent));
            bufferController.setSources(() -> modulatorPsk.getSempl());

            modulatorPsk.setSymbolSource(() -> groupAdd.getBit());
            groupAdd.addRadiogramPercentListener(percent -> informationWindow.updatePercentRadiogram(percent));
            driverHorizon.ducSetWidth(Width.kHz_3);
            driverHorizon.ducSetMode(Mode.ENABLE);
        } else {
            closeDeviceTx();
            stateCon = ethernetDriver.closeSocket();
        }
        return stateCon;
    }

    public boolean initRx(String typeDevice,
                          String typeProg,
                          String typeDemodulator,
                          String speedDemodulator,
                          ReceiverUPSWindowUI receiverUPSWindowUI,
                          InformationWindow informationWindow,
                          String ip, boolean state) {
        boolean stateCon = false;
        if (state) {
            int port = typeDevice.equals("Горизонт")?80:81;
            System.out.println("ïnitRx");
            stateCon = ethernetDriver.doInit(ip, port);
            driverHorizon = new DriverHorizon();
            //demodulatorPsk = new DemodulatorPsk(100.f, 3000.f);
            optimalNonCoherentDеmodulatorPsk = new OptimalNonCoherentDеmodulatorPsk(100.f / 3000.f);
            kylymDecoder = new KylymDecoder();
            kylymDecoder.setRunning(true);
            ethernetDriver.clearReceiverListener();
            ethernetDriver.addReceiverListener(data -> driverHorizon.parse(data));
            driverHorizon.addTransferListener(data -> ethernetDriver.writeBytes(data));

            driverHorizon.addDdcMode(data -> receiverUPSWindowUI.getModeRx(data));
            driverHorizon.addDdcWidth(data -> receiverUPSWindowUI.getWidthRx(data));
            driverHorizon.addDdcFrequency(data -> receiverUPSWindowUI.getFrequencyRx(data));

//      driverHorizon.addEthernetSettings((ip, mask, port, gateWay) -> transiverUPSWindow.updateEthernet(ip, mask, port, gateWay));

            //driverHorizon.addDdcIQ(sempl -> demodulatorPsk.demodulate(sempl));
            driverHorizon.addDdcIQ(sempl -> optimalNonCoherentDеmodulatorPsk.demodulate(sempl));
            driverHorizon.addDdcIQ(sempl -> debuger.show(sempl));
            //optimalNonCoherentDеmodulatorPsk.addListenerIq(sempl -> debuger.show(sempl));

            //demodulatorPsk.addListenerSymbol(data -> kylymDecoder.addData(data));
            optimalNonCoherentDеmodulatorPsk.addListenerSymbol(data -> kylymDecoder.addData(data));
            //modulatorPsk.setSymbolSource(() -> groupAdd.getBit());
            //groupAdd.addRadiogramPercentListener(percent -> informationWindow.updatePercentRadiogram(percent));
            driverHorizon.ddcSetWidth(Width.kHz_3);
            driverHorizon.ddcSetMode(Mode.ENABLE);
        } else {
            closeDeviceRx();
            stateCon=ethernetDriver.closeSocket();
        }
        return stateCon;
    }

    public void getParamsRxTx() {
        driverHorizon.ducGetFrequency();
        driverHorizon.ducGetWidth();
        driverHorizon.ducGetMode();

        driverHorizon.ddcGetFrequency();
        driverHorizon.ddcGetWidth();
        driverHorizon.ddcGetMode();
    }
    public void getParamsRx() {
        driverHorizon.ddcGetFrequency();
        driverHorizon.ddcGetWidth();
        driverHorizon.ddcGetMode();
    }
    public void getParamsTx() {
        driverHorizon.ducGetFrequency();
        driverHorizon.ducGetWidth();
        driverHorizon.ducGetMode();
    }
    public void closeDeviceRxTx(){
        driverHorizon.ducSetMode(Mode.DISABLE);
        driverHorizon.ddcSetMode(Mode.DISABLE);
        kylymDecoder.setRunning(false);
        bufferController.setWorkingThread(false);
    }
    public void closeDeviceRx(){
        driverHorizon.ducSetMode(Mode.DISABLE);
        driverHorizon.ddcSetMode(Mode.DISABLE);
        kylymDecoder.setRunning(false);
    }
    public void closeDeviceTx(){
        driverHorizon.ducSetMode(Mode.DISABLE);
        driverHorizon.ddcSetMode(Mode.DISABLE);
        bufferController.setWorkingThread(false);
    }
}
