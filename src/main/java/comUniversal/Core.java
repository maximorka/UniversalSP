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

            while (true) {

                if (ethernetDriver.isConnect()) {

//                    driverHorizon.ddcSetMode(Mode.DISABLE); try { Thread.sleep(100);} catch (InterruptedException e) {}
//                    driverHorizon.ddcSetMode(Mode.ENABLE); try { Thread.sleep(100);} catch (InterruptedException e) {}
//                    driverHorizon.ddcSetMode(Mode.TEST); try { Thread.sleep(100);} catch (InterruptedException e) {}
//                    driverHorizon.ddcGetMode(); try { Thread.sleep(100);} catch (InterruptedException e) {}
//                    driverHorizon.ddcSetFrequency(123); try { Thread.sleep(100);} catch (InterruptedException e) {}
//                    driverHorizon.ddcGetFrequency(); try { Thread.sleep(100);} catch (InterruptedException e) {}
//                    driverHorizon.ddcSetWidth(Width.kHz_48); try { Thread.sleep(100);} catch (InterruptedException e) {}
//                    driverHorizon.ddcSetWidth(Width.kHz_24); try { Thread.sleep(100);} catch (InterruptedException e) {}
//                    driverHorizon.ddcSetWidth(Width.kHz_12); try { Thread.sleep(100);} catch (InterruptedException e) {}
//                    driverHorizon.ddcSetWidth(Width.kHz_6); try { Thread.sleep(100);} catch (InterruptedException e) {}
//                    driverHorizon.ddcSetWidth(Width.kHz_3); try { Thread.sleep(100);} catch (InterruptedException e) {}
//                    driverHorizon.ddcGetWidth(); try { Thread.sleep(100);} catch (InterruptedException e) {}
//                    driverHorizon.ddcReset(); try { Thread.sleep(100);} catch (InterruptedException e) {}
//
//                    driverHorizon.ducSetMode(Mode.DISABLE); try { Thread.sleep(100);} catch (InterruptedException e) {}
//                    driverHorizon.ducSetMode(Mode.ENABLE); try { Thread.sleep(100);} catch (InterruptedException e) {}
//                    driverHorizon.ducSetMode(Mode.TEST); try { Thread.sleep(100);} catch (InterruptedException e) {}
//                    driverHorizon.ducGetMode(); try { Thread.sleep(100);} catch (InterruptedException e) {}
//                    driverHorizon.ducSetFrequency(123); try { Thread.sleep(100);} catch (InterruptedException e) {}
//                    driverHorizon.ducGetFrequency(); try { Thread.sleep(100);} catch (InterruptedException e) {}
//                    driverHorizon.ducSetWidth(Width.kHz_48); try { Thread.sleep(100);} catch (InterruptedException e) {}
//                    driverHorizon.ducSetWidth(Width.kHz_24); try { Thread.sleep(100);} catch (InterruptedException e) {}
//                    driverHorizon.ducSetWidth(Width.kHz_12); try { Thread.sleep(100);} catch (InterruptedException e) {}
//                    driverHorizon.ducSetWidth(Width.kHz_6); try { Thread.sleep(100);} catch (InterruptedException e) {}
//                    driverHorizon.ducSetWidth(Width.kHz_3); try { Thread.sleep(100);} catch (InterruptedException e) {}
//                    driverHorizon.ducGetWidth(); try { Thread.sleep(100);} catch (InterruptedException e) {}
//                    driverHorizon.ducClearBuffer(); try { Thread.sleep(100);} catch (InterruptedException e) {}
//                    driverHorizon.ducGetBufferPercent(); try { Thread.sleep(100);} catch (InterruptedException e) {}
                    try { Thread.sleep(1000);} catch (InterruptedException e) {}
                    driverHorizon.ducReset();




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
