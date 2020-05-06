package comUniversal;


import comUniversal.lowLevel.EthernetDriver;

public class Core {
    private static Core core = new Core();
    public EthernetDriver ethernetDriver = new EthernetDriver();
    private DriverHorizon driverHorizon = new DriverHorizon();
    private Update update;
    private boolean running ;

    public boolean connected;

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
                    System.out.println("Hello");
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }
            }
        }
    }
    public void setRunning(boolean running) {
        this.running = running;
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
