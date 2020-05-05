package comUniversal;

import comUniversal.lowLevel.EthernetDriver;

public class Core {
    private static Core instance = new Core();
    public EthernetDriver ethernetDriver = new EthernetDriver();


    /**
     * Повертає унікальний екземпляр "ядра"
     *
     * @return
     */
    public static Core getInstance() {
        return instance;
    }


}
