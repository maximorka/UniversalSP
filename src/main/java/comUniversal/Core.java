package comUniversal;

import comUniversal.lowLevel.Debuger.Debuger;
import comUniversal.ui.InformationWindow;
import comUniversal.ui.MainUI;
import comUniversal.ui.ReceiverUPSWindowUI;
import comUniversal.ui.TransmitterUPSWindowUI;

import java.io.IOException;

public class Core {
    private static Core core = new Core();
//    public Debuger debuger;
//    public GroupAdd groupAdd;
//    public EthernetDriver ethernetDriver;
//    public DriverHorizon driverHorizon;
//    public BufferController bufferController;
//    public ModulatorPsk modulatorPsk;
//    public DemodulatorPsk demodulatorPsk;
//    public KylymDecoder kylymDecoder;
    public MainUI mainUI = new MainUI();
    public ReceiverUPSWindowUI receiverUPSWindowUI = new ReceiverUPSWindowUI();
    public TransmitterUPSWindowUI transmitterUPSWindowUI = new TransmitterUPSWindowUI();
    public InformationWindow informationWindow = new InformationWindow();
    private Update update;
    private boolean running = false;
    public int countConectedDevice = 0;
    public Device device[]=new Device[2];
    private long index;
    /**
     * Повертає унікальний екземпляр "ядра"
     *
     * @return
     */
    public static Core getCore() {
        return core;
    }

    class Update extends Thread {
        @Override
        public void run() {

            while (!(Core.getCore().device[0].ethernetDriver.isConect()||Core.getCore().device[1].ethernetDriver.isConect())) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            while (true) {
                    if (running) {
                        if (Core.getCore().device[0].ethernetDriver.isConect()) {
                            device[0].getParamsTx();
                        }
                        if (Core.getCore().device[1].ethernetDriver.isConect()) {
                            device[1].getParamsRx();
                        }
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    public void setRunning(boolean running) {
        this.running = running;
    }
    private Core(){


        device[0] = new Device();
        device[1] = new Device();
//        groupAdd = new GroupAdd();
//        this.ethernetDriver = new EthernetDriver();
//        driverHorizon = new DriverHorizon();
//        kylymDecoder = new KylymDecoder();
//        modulatorPsk = new ModulatorPsk();
//        modulatorPsk.setRelativeBaudeRate(100.f/3000.f);
//        bufferController = new BufferController(3000);
//        demodulatorPsk = new DemodulatorPsk(100.f,3000.f);
//
//        ethernetDriver.addReceiverListener(data -> driverHorizon.parse(data));
//        driverHorizon.addTransferListener(data -> ethernetDriver.writeBytes(data));
//
//       driverHorizon.addDucMode(data->transmitterUPSWindowUI.getModeTx(data));
//       driverHorizon.addDucWidth(data->transmitterUPSWindowUI.getWidthTx(data));
//       driverHorizon.addDucFrequency(data->transmitterUPSWindowUI.getFrequencyTx(data));
//       driverHorizon.addDucBufferPercent(data->transmitterUPSWindowUI.updatePercent(data));
//
//       driverHorizon.addDdcMode(data->receiverUPSWindowUI.getModeRx(data));
//       driverHorizon.addDdcWidth(data->receiverUPSWindowUI.getWidthRx(data));
//       driverHorizon.addDdcFrequency(data->receiverUPSWindowUI.getFrequencyRx(data));
//
////      driverHorizon.addEthernetSettings((ip, mask, port, gateWay) -> transiverUPSWindow.updateEthernet(ip, mask, port, gateWay));
//        bufferController.addTransferListener(sample -> driverHorizon.ducSetIq(sample));
//        driverHorizon.addDucBufferPercent(percent -> bufferController.updatePercent(percent));
//        bufferController.setSources(() -> modulatorPsk.getSempl());
//        driverHorizon.addDdcIQ(sempl -> demodulatorPsk.demodulate(sempl));
//
//        demodulatorPsk.addListenerSymbol(data->kylymDecoder.addData(data));
//        modulatorPsk.setSymbolSource(() -> groupAdd.getBit());
//        groupAdd.addRadiogramPercentListener(percent -> informationWindow.updatePercentRadiogram(percent));

        update = new Update();
        update.start();

    }
    public boolean setDriverConnect(boolean ifConnect, String typeRx, String typeTx, int typeProg) {
        boolean state = false;
        System.out.println("RX: "+typeRx+" TX"+typeTx+" Prog"+typeProg);

        if(typeProg==1){
            state = device[1].initRx(typeRx,typeProg,"PSK","100",receiverUPSWindowUI,informationWindow,receiverUPSWindowUI.getIP(),ifConnect);
            countConectedDevice =state?1:0;
        }
        else if (receiverUPSWindowUI.getIP().equals(transmitterUPSWindowUI.getIP())){
            state = device[0].initRxTx(typeRx,typeProg,"PSK","100","PSK","100",transmitterUPSWindowUI,receiverUPSWindowUI,informationWindow,receiverUPSWindowUI.getIP(),ifConnect);
            countConectedDevice = state?1:0;
        }else {
            state  = device[0].initTx(typeTx,typeProg,"PSK","100",transmitterUPSWindowUI,informationWindow,transmitterUPSWindowUI.getIP(),ifConnect);
            state &= device[1].initRx(typeRx,typeProg,"PSK","100",receiverUPSWindowUI,informationWindow,receiverUPSWindowUI.getIP(),ifConnect);
            countConectedDevice =state?2:0;
        }
        return state;
    }


}
