package comUniversal;

import comUniversal.lowLevel.DriverHorizon.*;


import comUniversal.lowLevel.DriverHorizon.TransferDataBytes;
import comUniversal.lowLevel.EthernetDriver;

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

            try { Thread.sleep(1000);} catch (InterruptedException e) {}
            driverHorizon.ducSetMode(Mode.ENABLE);
            driverHorizon.ducSetWidth(Width.kHz_3);



            Complex[] sempls = new Complex[30];
            for(int i = 0; i < sempls.length; i++)
                sempls[i] = new Complex(0,0);


            while (true) {

                if (ethernetDriver.isConnect()) {

                    driverHorizon.ducSetIq(sempls);

                    try { Thread.sleep(1000);} catch (InterruptedException e) {}



                }
            }
        }
    }

    private Core(){
        TransferDataBytes listener = new TransferDataBytes() {
            @Override
            public void SendData(byte[] data) {
                ethernetDriver.writeBytes(data);
            }
        };
        driverHorizon.addTransferListener(listener);


        update = new Update();
        update.start();
    }


}
