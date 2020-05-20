package comUniversal;

import comUniversal.BitLevel.GroupAdd;
import comUniversal.lowLevel.BufferController.BufferController;
import comUniversal.lowLevel.Debuger.Debuger;
import comUniversal.lowLevel.Demodulator.DemodulatorPsk;
import comUniversal.lowLevel.DriverEthernet.EthernetDriver;
import comUniversal.lowLevel.DriverHorizon.DriverHorizon;
import comUniversal.lowLevel.Modulator.ModulatorPsk;
import comUniversal.ui.*;
import comUniversal.util.Complex;

import java.io.IOException;

public class Core {
    private static Core core = new Core();
    public Debuger debuger;
    public GroupAdd groupAdd;
    public EthernetDriver ethernetDriver;
    public DriverHorizon driverHorizon;
    public BufferController bufferController;
    public ModulatorPsk modulatorPsk;
    public DemodulatorPsk demodulatorPsk;
    public KylymDecoder kylymDecoder;
    public MainUI mainUI = new MainUI();
    public ReceiverUPSWindowUI receiverUPSWindowUI = new ReceiverUPSWindowUI();
    public TransiverUPSWindow transiverUPSWindow = new TransiverUPSWindow();
    public TransmitterUPSWindowUI transmitterUPSWindowUI = new TransmitterUPSWindowUI();
    public InformationWindow informationWindow = new InformationWindow();
    private Update update;
    private boolean running = false;
    /**
     * Повертає унікальний екземпляр "ядра"
     *
     * @return
     */
    public static Core getCore() {
        return core;
    }

    class Update extends Thread{
        @Override
        public void run() {

            Complex sempl = new Complex(0.f, 0.f);

            while (true) {

                if(running){
                    driverHorizon.ducGetFrequency();
                    driverHorizon.ducGetWidth();
                    driverHorizon.ducGetMode();

                    driverHorizon.ddcGetFrequency();
                    driverHorizon.ddcGetWidth();
                    driverHorizon.ddcGetMode();

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public void setRunning(boolean running) {
        this.running = running;
    }
    private Core(){

        try {debuger = new Debuger();} catch (IOException e) {}

        groupAdd = new GroupAdd();
        ethernetDriver = new EthernetDriver();
        driverHorizon = new DriverHorizon();
        kylymDecoder = new KylymDecoder();
        bufferController = new BufferController(3000);
        modulatorPsk = new ModulatorPsk();
        modulatorPsk.setRelativeBaudeRate(100.f/3000.f);
        demodulatorPsk = new DemodulatorPsk(100.f,3000.f);

        ethernetDriver.addReceiverListener(data -> driverHorizon.parse(data));
        driverHorizon.addTransferListener(data -> ethernetDriver.writeBytes(data));

       driverHorizon.addDucMode(data->transmitterUPSWindowUI.getModeTx(data));
       driverHorizon.addDucWidth(data->transmitterUPSWindowUI.getWidthTx(data));
       driverHorizon.addDucFrequency(data->transmitterUPSWindowUI.getFrequencyTx(data));
       driverHorizon.addDucBufferPercent(data->transmitterUPSWindowUI.updatePercent(data));

       driverHorizon.addDdcMode(data->receiverUPSWindowUI.getModeRx(data));
       driverHorizon.addDdcWidth(data->receiverUPSWindowUI.getWidthRx(data));
       driverHorizon.addDdcFrequency(data->receiverUPSWindowUI.getFrequencyRx(data));

//        driverHorizon.addEthernetSettings((ip, mask, port, gateWay) -> transiverUPSWindow.updateEthernet(ip, mask, port, gateWay));
        bufferController.addTransferListener(sample -> driverHorizon.ducSetIq(sample));
        driverHorizon.addDucBufferPercent(percent -> bufferController.updatePercent(percent));
        bufferController.setSources(() -> modulatorPsk.getSempl());
        driverHorizon.addDdcIQ(sempl -> demodulatorPsk.demodulate(sempl));

        demodulatorPsk.addListenerSymbol(data->kylymDecoder.addData(data));
        modulatorPsk.setSymbolSource(() -> groupAdd.getBit());

        update = new Update();
        update.start();

    }
    public void setDriver() {

    }
}
