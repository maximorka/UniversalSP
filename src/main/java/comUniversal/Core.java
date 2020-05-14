package comUniversal;

import comUniversal.lowLevel.DriverEthernet.EthernetDriver;

import comUniversal.lowLevel.DriverHorizon.DriverHorizon;
import comUniversal.lowLevel.DriverHorizon.TransferDataBytes;
import comUniversal.lowLevel.DriverEthernet.ReceiverDataBytes;
import comUniversal.lowLevel.DriverHorizon.DriverHorizon;
import javafx.geometry.HorizontalDirection;


public class Core {
    private static Core core = new Core();
    public EthernetDriver ethernetDriver = new EthernetDriver();
    public DriverHorizon driverHorizon = new DriverHorizon();
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
                    //System.out.println("Hello");
                }
            }
        }
    }
    public void setRunning(boolean running) {
        this.running = running;
    }
    private Core(){

        ethernetDriver.addReceiverListener(data -> driverHorizon.parse(data));
        driverHorizon.addTransferListener(data -> ethernetDriver.writeBytes(data));

        update = new Update();
        update.start();

    }


}
