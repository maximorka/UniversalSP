package comUniversal;

import comUniversal.lowLevel.BufferController.BufferController;
import comUniversal.lowLevel.BufferController.GetIQSource;
import comUniversal.lowLevel.BufferController.IBufferController;
import comUniversal.lowLevel.DriverEthernet.EthernetDriver;
import comUniversal.lowLevel.DriverHorizon.*;
import comUniversal.lowLevel.Modulator.ModulatorPsk;
import comUniversal.ui.ReceiverUPSWindowUI;
import comUniversal.ui.TransiverUPSWindow;
import comUniversal.ui.TransmitterUPSWindowUI;
import comUniversal.util.Complex;


public class Core {
    private static Core core = new Core();
    public EthernetDriver ethernetDriver;
    public DriverHorizon driverHorizon;
    public BufferController bufferController;
    public ModulatorPsk modulatorPsk;
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

        ethernetDriver = new EthernetDriver();
        driverHorizon = new DriverHorizon();
        bufferController = new BufferController(3000);
        modulatorPsk = new ModulatorPsk();

        modulatorPsk.setRelativeBaudeRate(100.f/3000.f);
        bufferController.updateSampleFrequency(3000);
        bufferController.setSources(() -> modulatorPsk.getSempl());
        bufferController.addTransferListener(sample -> driverHorizon.ducSetIq(sample));
        driverHorizon.addTransferListener(data -> ethernetDriver.writeBytes(data));
        driverHorizon.addDucBufferPercent(percent -> bufferController.updatePercent(percent));
        ethernetDriver.addReceiverListener(data -> driverHorizon.parse(data));

        ethernetDriver.doInit("192.168.0.2", 81);

        try {Thread.sleep(1000);} catch (InterruptedException e) {}
        driverHorizon.ducSetWidth(Width.kHz_3);
        driverHorizon.ducSetMode(Mode.ENABLE);
        try {Thread.sleep(1000);} catch (InterruptedException e) {}




        update = new Update();
        update.start();

    }
    public void setDriver() {

    }
}
