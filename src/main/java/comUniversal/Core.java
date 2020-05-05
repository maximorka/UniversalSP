package comUniversal;

import comUniversal.lowLevel.DriverHorizon.DriverHorizon;


import comUniversal.lowLevel.EthernetDriver;
import comUniversal.lowLevel.DriverHorizon.Status;

public class Core {
    private static Core core = new Core();
    public EthernetDriver ethernetDriver = new EthernetDriver();
    private DriverHorizon driverHorizon = new DriverHorizon();
    private Update update;

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

                if (ethernetDriver.isConnect()) {
                    System.out.println("DFGdfgdf");
                    Status status = driverHorizon.setFrequency(128000);
                    System.out.println(status);

                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Core(){
        update = new Update();
        update.start();
    }


}
