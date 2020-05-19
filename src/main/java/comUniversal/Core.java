package comUniversal;

import comUniversal.lowLevel.BufferController.BufferController;
import comUniversal.lowLevel.Debuger.Debuger;
import comUniversal.lowLevel.Demodulator.DemodulatorPsk;
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
    public Debuger debuger;
    public EthernetDriver ethernetDriver;
    public DriverHorizon driverHorizon;
    public BufferController bufferController;
    public ModulatorPsk modulatorPsk;
    public DemodulatorPsk demodulatorPsk;
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
        modulatorPsk.setRelativeBaudeRate(100.f/3000.f);
        demodulatorPsk = new DemodulatorPsk(100.f,3000.f);

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
        bufferController.addTransferListener(sample -> driverHorizon.ducSetIq(sample));
        driverHorizon.addDucBufferPercent(percent -> bufferController.updatePercent(percent));
        bufferController.setSources(() -> modulatorPsk.getSempl());
        driverHorizon.addDdcIQ(sempl -> demodulatorPsk.demodulate(sempl));

        update = new Update();
        update.start();

    }
    public void setDriver() {

    }
}
