package comUniversal;

import comUniversal.lowLevel.DriverEthernet.EthernetDriver;
import comUniversal.lowLevel.DriverHorizon.DriverHorizon;
import comUniversal.ui.ReceiverUPSWindowUI;
import comUniversal.ui.TransiverUPSWindow;
import comUniversal.ui.TransmitterUPSWindowUI;


public class Core {
    private static Core core = new Core();
    public EthernetDriver ethernetDriver = new EthernetDriver();
    public ReceiverUPSWindowUI receiverUPSWindowUI = new ReceiverUPSWindowUI();
    public TransiverUPSWindow transiverUPSWindow = new TransiverUPSWindow();
    public TransmitterUPSWindowUI transmitterUPSWindowUI = new TransmitterUPSWindowUI();

    public DriverHorizon driverHorizon;
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
        driverHorizon = new DriverHorizon();
        ethernetDriver.addReceiverListener(data -> driverHorizon.parse(data));
        driverHorizon.addTransferListener(data -> ethernetDriver.writeBytes(data));
        update = new Update();
        update.start();

    }
    public void setDriver() {

    }
}
