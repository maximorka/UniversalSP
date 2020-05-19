package comUniversal;

import comUniversal.lowLevel.BufferController.BufferController;
import comUniversal.lowLevel.Debuger.Debuger;
import comUniversal.lowLevel.DriverEthernet.EthernetDriver;
import comUniversal.lowLevel.DriverHorizon.DriverHorizon;
import comUniversal.lowLevel.Modulator.ModulatorPsk;
import comUniversal.ui.MainUI;
import comUniversal.ui.ReceiverUPSWindowUI;
import comUniversal.ui.TransiverUPSWindow;
import comUniversal.ui.TransmitterUPSWindowUI;
import comUniversal.util.Complex;

import java.io.IOException;

public class Core {
    private static Core core = new Core();
    Debuger debuger;
    public EthernetDriver ethernetDriver;
    public DriverHorizon driverHorizon;
    public BufferController bufferController;
    public ModulatorPsk modulatorPsk;
    public MainUI mainUI = new MainUI();
    public ReceiverUPSWindowUI receiverUPSWindowUI = new ReceiverUPSWindowUI();
    public TransiverUPSWindow transiverUPSWindow = new TransiverUPSWindow();
    public TransmitterUPSWindowUI transmitterUPSWindowUI = new TransmitterUPSWindowUI();
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

                }
            }
        }
    }
    public void setRunning(boolean running) {
        this.running = running;
    }
    private Core(){

        try {debuger = new Debuger();} catch (IOException e) {}

        ethernetDriver = new EthernetDriver();
        driverHorizon = new DriverHorizon();
        bufferController = new BufferController(3000);
        modulatorPsk = new ModulatorPsk();

        ethernetDriver.addReceiverListener(data -> driverHorizon.parse(data));
        driverHorizon.addTransferListener(data -> ethernetDriver.writeBytes(data));

        driverHorizon.addInit(data->transiverUPSWindow.getInit(data));
        driverHorizon.addDdcMode(data->transiverUPSWindow.getModeRx(data));
        driverHorizon.addDdcWidth(data->transiverUPSWindow.getWidthRx(data));
        driverHorizon.addDdcFrequency(data->transiverUPSWindow.getFrequencyRx(data));
        driverHorizon.addDucMode(data->transiverUPSWindow.getModeTx(data));
        driverHorizon.addDucWidth(data->transiverUPSWindow.getWidthTx(data));
        driverHorizon.addDucFrequency(data->transiverUPSWindow.getFrequencyTx(data));
        driverHorizon.addDucBufferPercent(data->transiverUPSWindow.updatePercent(data));
        driverHorizon.addEthernetSettings((ip, mask, port, gateWay) -> transiverUPSWindow.updateEthernet(ip, mask, port, gateWay));



        //* Тестує Бобер
//        modulatorPsk.setRelativeBaudeRate(100.f/3000.f);
//        bufferController.updateSampleFrequency(3000);
//        bufferController.setSources(() -> modulatorPsk.getSempl());
//        bufferController.addTransferListener(sample -> driverHorizon.ducSetIq(sample));
//        driverHorizon.addTransferListener(data -> ethernetDriver.writeBytes(data));
//        driverHorizon.addDucBufferPercent(percent -> bufferController.updatePercent(percent));
//        ethernetDriver.addReceiverListener(data -> driverHorizon.parse(data));
//        ethernetDriver.doInit("192.168.0.2", 81);
//        try {Thread.sleep(1000);} catch (InterruptedException e) {}
//        driverHorizon.ducSetWidth(Width.kHz_3);
//        driverHorizon.ducSetMode(Mode.ENABLE);
//        try {Thread.sleep(1000);} catch (InterruptedException e) {}
        //*/

        update = new Update();
        update.start();

    }
    public void setDriver() {

    }
}
