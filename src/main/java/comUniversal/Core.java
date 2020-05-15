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
                    //System.out.println("Hello");
                }
            }
        }
    }
    public void setRunning(boolean running) {
        this.running = running;
    }
    private Core(){

        ethernetDriver.addReceiverListener(new ReceiverDataBytes() {
            @Override
            public void ReceiveData(byte[] data) {
                driverHorizon.parse(data);
            }
        });

        ethernetDriver.doInit("192.168.0.1", 80);

        update = new Update();
        update.start();

        TransferDataBytes listener = new TransferDataBytes() {
            @Override
            public void SendData(byte[] data) {
                ethernetDriver.writeBytes(data);
            }
        };
        driverHorizon.addTransferListener(listener);
    }
    public void setDriver() {
        driverHorizon = new DriverHorizon();
        ethernetDriver.addReceiverListener(data -> driverHorizon.parse(data));
        driverHorizon.addTransferListener(data -> ethernetDriver.writeBytes(data));
    }
}
