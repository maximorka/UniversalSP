package comUniversal;

import comUniversal.lowLevel.DriverEthernet.EthernetDriver;

public class Core {
    private static Core core = new Core();
    public EthernetDriver ethernetDriver = new EthernetDriver();
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
        update = new Update();
        update.start();
    }


}
