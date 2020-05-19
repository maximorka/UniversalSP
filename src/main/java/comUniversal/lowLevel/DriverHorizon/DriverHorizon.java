package comUniversal.lowLevel.DriverHorizon;

import comUniversal.util.Complex;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class DriverHorizon {

    // DDC command
    private byte ddc_get_iq = 0;
    private byte ddc_set_mode = 1;
    private byte ddc_get_mode =  2;
    private byte ddc_set_width =  3;
    private byte ddc_get_width =  4;
    private byte ddc_set_frequency =  5;
    private byte ddc_get_frequency =  6;
    private byte ddc_reset =  7;

    // DUC command
    private byte duc_set_iq = 0;
    private byte duc_set_mode = 9;
    private byte duc_get_mode =  10;
    private byte duc_set_width =  11;
    private byte duc_get_width =  12;
    private byte duc_set_frequency =  13;
    private byte duc_get_frequency =  14;
    private byte duc_reset =  15;
    private byte duc_clear_buffer =  16;
    private byte duc_get_buffer_percent =  17;

    // Interface
    private byte ethernet_set = 21;
    private byte ethernet_get = 22;
    private byte ethernet_reset = 29;
    private byte init = 30;

    // Listeners
    private List<TransferDataBytes> transfer = new ArrayList<>();
    public void addTransferListener(TransferDataBytes listener){transfer.add(listener);}
    public void clearTransferListener(){transfer.clear();}
    private void toListenersTransferDataBytes(byte[] data){
        if(!transfer.isEmpty())
            for(TransferDataBytes listener: transfer)
                listener.SendData(data);
    }

    // Common methods
    private void sendCommand(byte command){sendCommand(command, 0);}
    private void sendCommand(byte command, Complex[] packet){

        byte[] result = new byte[4 + 64*3];
        ByteBuffer re_im = ByteBuffer.allocate(4);
        int re = 0, im = 0, index = 0;

        result[index++] = command;
        result[index++] = 0x56;
        result[index++] = 0x34;
        result[index++] = 0x12;
        for (Complex sempl : packet) {
            re = (int)(2047*sempl.re);
            im = (int)(2047*sempl.im);
            re &= 0xFFF;
            im &= 0xFFF;
            re_im.putInt((re<<12)|im);
            result[index++] = re_im.array()[3];
            result[index++] = re_im.array()[2];
            result[index++] = re_im.array()[1];
            re_im.clear();
        }
        toListenersTransferDataBytes(result);

    }

    private void sendCommand(byte command, int data){
        byte[] result = new byte[8];
        result[0] = command;
        result[1] = 0x56;
        result[2] = 0x34;
        result[3] = 0x12;
        ByteBuffer b = ByteBuffer.allocate(4);
        b.putInt(data);
        result[4] = b.array()[3];
        result[5] = b.array()[2];
        result[6] = b.array()[1];
        result[7] = b.array()[0];
        toListenersTransferDataBytes(result);
    }
    private void sendCommand(byte command, int data1, int data2, int data3, int data4){
        byte[] result = new byte[20];
        result[0] = command;
        result[1] = 0x56;
        result[2] = 0x34;
        result[3] = 0x12;
        ByteBuffer b = ByteBuffer.allocate(4);
        b.putInt(data1);
        result[4] = b.array()[3];
        result[5] = b.array()[2];
        result[6] = b.array()[1];
        result[7] = b.array()[0];
        b.clear();
        b.putInt(data2);
        result[8] = b.array()[3];
        result[9] = b.array()[2];
        result[10] = b.array()[1];
        result[11] = b.array()[0];
        b.clear();
        b.putInt(data3);
        result[12] = b.array()[3];
        result[13] = b.array()[2];
        result[14] = b.array()[1];
        result[15] = b.array()[0];
        b.clear();
        b.putInt(data4);
        result[16] = b.array()[3];
        result[17] = b.array()[2];
        result[18] = b.array()[1];
        result[19] = b.array()[0];
        toListenersTransferDataBytes(result);
    }

    // DDC methods
    public void ddcSetMode(Mode mode){
        int data = -1;
        if(mode == Mode.DISABLE) data = 0;
        else if(mode == Mode.ENABLE) data = 1;
        else if(mode == Mode.TEST) data = 2;
        sendCommand(ddc_set_mode, data);
    }
    public void ddcGetMode() {sendCommand(ddc_get_mode);}
    public void ddcSetWidth(Width width){
        int data = -1;
        if(width == Width.kHz_48) data = 0;
        else if(width == Width.kHz_24) data = 1;
        else if(width == Width.kHz_12) data = 2;
        else if(width == Width.kHz_6) data = 3;
        else if(width == Width.kHz_3) data = 4;
        sendCommand(ddc_set_width, data);
    }
    public void ddcGetWidth(){sendCommand(ddc_get_width);}
    public Status ddcSetFrequency(int frequency){
        if(frequency < 0) return Status.IS_NEGATIVE;
        else if(frequency > 1000000) return Status.IS_TO_HIGH;
        else {
            sendCommand(ddc_set_frequency, frequency);
            return Status.IS_CORRECT;
        }
    }
    public void ddcGetFrequency(){sendCommand(ddc_get_frequency);}
    public void ddcReset(){sendCommand(ddc_reset);}

    // DUC methods
    private int semplCounter = 0;
    private Complex[] samplePacket = new Complex[64];
    public void ducSetIq(Complex sempl){
        samplePacket[semplCounter++] = sempl;
        if(semplCounter == 64) {
            semplCounter = 0;
            sendCommand(duc_set_iq, samplePacket);
        }
    }
    public void ducSetIq(Complex[] sempls){
        for(Complex sempl: sempls)
            ducSetIq(sempl);
    }


    public void ducSetMode(Mode mode){
        int data = -1;
        if(mode == Mode.DISABLE) data = 0;
        else if(mode == Mode.ENABLE) data = 1;
        else if(mode == Mode.TEST) data = 2;
        sendCommand(duc_set_mode, data);
    }
    public void ducGetMode(){sendCommand(duc_get_mode);}
    public void ducSetWidth(Width width){
        int data = -1;
        if(width == Width.kHz_48) data = 0;
        else if(width == Width.kHz_24) data = 1;
        else if(width == Width.kHz_12) data = 2;
        else if(width == Width.kHz_6) data = 3;
        else if(width == Width.kHz_3) data = 4;
        sendCommand(duc_set_width, data);
    }
    public void ducGetWidth(){sendCommand(duc_get_width);}
    public Status ducSetFrequency(int frequency){
        if(frequency < 0) return Status.IS_NEGATIVE;
        else if(frequency > 1000000) return Status.IS_TO_HIGH;
        else {
            sendCommand(duc_set_frequency, frequency);
            return Status.IS_CORRECT;
        }
    }
    public void ducGetFrequency(){sendCommand(duc_get_frequency);}
    public void ducReset(){sendCommand(duc_reset);}
    public void ducClearBuffer(){sendCommand(duc_clear_buffer);}
    public void ducGetBufferPercent(){sendCommand(duc_get_buffer_percent);}

    // Inertface
    public void ethernetSet(int ip, int mask, int port, int gateWay){
         sendCommand(ethernet_set, ip, mask, port, gateWay);
    }
    public void ethernetGet(){sendCommand(ethernet_get);}
    public void ethernetReset(){sendCommand(ethernet_reset);}
    public void init(){sendCommand(init);}



    // Parser describe ---------------------------------


    // Listeners DdcIQ
    private List<DdcIQ> ddcIQ = new ArrayList<>();
    public void addDdcIQ(DdcIQ listener){ddcIQ.add(listener);}
    public void clearDdcIQ(){ddcIQ.clear();}
    private void toListenersDdcIQ(Complex sempl){
        if(!ddcIQ.isEmpty())
            for(DdcIQ listener: ddcIQ)
                listener.sempl(sempl);
    }
    // Listeners DdcMode
    private List<DdcMode> ddcMode = new ArrayList<>();
    public void addDdcMode(DdcMode listener){ddcMode.add(listener);}
    public void clearDdcMode(){ddcMode.clear();}
    private void toListenersDdcMode(Mode mode){
        if(!ddcMode.isEmpty())
            for(DdcMode listener: ddcMode)
                listener.mode(mode);
    }
    // Listeners DdcWidth
    private List<DdcWidth> ddcWidth = new ArrayList<>();
    public void addDdcWidth(DdcWidth listener){ddcWidth.add(listener);}
    public void clearDdcWidth(){ddcWidth.clear();}
    private void toListenersDdcWidth(Width width){
        if(!ddcWidth.isEmpty())
            for(DdcWidth listener: ddcWidth)
                listener.width(width);
    }
    // Listeners DdcFrequency
    private List<DdcFrequency> ddcFrequency = new ArrayList<>();
    public void addDdcFrequency(DdcFrequency listener){ddcFrequency.add(listener);}
    public void clearDdcFrequency(){ddcFrequency.clear();}
    private void toListenersDdcFrequency(int frequency){
        if(!ddcFrequency.isEmpty())
            for(DdcFrequency listener: ddcFrequency)
                listener.frequency(frequency);
    }

    // Listeners DucMode
    private List<DucMode> ducMode = new ArrayList<>();
    public void addDucMode(DucMode listener){ducMode.add(listener);}
    public void clearDucMode(){ducMode.clear();}
    private void toListenersDucMode(Mode mode){
        if(!ducMode.isEmpty())
            for(DucMode listener: ducMode)
                listener.mode(mode);
    }
    // Listeners DucWidth
    private List<DucWidth> ducWidth = new ArrayList<>();
    public void addDucWidth(DucWidth listener){ducWidth.add(listener);}
    public void clearDucWidth(){ducWidth.clear();}
    private void toListenersDucWidth(Width width){
        if(!ducWidth.isEmpty())
            for(DucWidth listener: ducWidth)
                listener.width(width);
    }
    // Listeners DucFrequency
    private List<DucFrequency> ducFrequency = new ArrayList<>();
    public void addDucFrequency(DucFrequency listener){ducFrequency.add(listener);}
    public void clearDucFrequency(){ducFrequency.clear();}
    private void toListenersDucFrequency(int frequency){
        if(!ducFrequency.isEmpty())
            for(DucFrequency listener: ducFrequency)
                listener.frequency(frequency);
    }
    // Listeners DucBufferPercent
    private List<DucBufferPercent> ducBufferPercent = new ArrayList<>();
    public void addDucBufferPercent(DucBufferPercent listener){ducBufferPercent.add(listener);}
    public void clearDucBufferPercent(){ducBufferPercent.clear();}
    private void toListenersDucBufferPercent(int percent){
        if(!ducBufferPercent.isEmpty())
            for(DucBufferPercent listener: ducBufferPercent)
                listener.percent(percent);
    }

    // Listeners EthernetSettings
    private List<EthernetSettings> ethernetSettings = new ArrayList<>();
    public void addEthernetSettings(EthernetSettings listener){ethernetSettings.add(listener);}
    public void clearEthernetSettings(){ethernetSettings.clear();}
    private void toListenersEthernetSettings(int ip, int mask, int port, int gateWay){
        if(!ethernetSettings.isEmpty())
            for(EthernetSettings listener: ethernetSettings)
                listener.ethernetSettings(ip, mask, port, gateWay);
    }

    // Listeners Error
    private List<Error> errors = new ArrayList<>();
    public void addError(Error listener){errors.add(listener);}
    public void clearError(){errors.clear();}
    private void toListenersError(int error){
        if(!errors.isEmpty())
            for(Error listener: errors)
                listener.error(error);
    }

    // Listeners Init
    private List<Init> inits = new ArrayList<>();
    public void addInit(Init listener){inits.add(listener);}
    public void clearInit(){inits.clear();}
    private void toListenersInit(int init){
        if(!inits.isEmpty())
            for(Init listener: inits)
                listener.init(init);
    }

    private int PACKET_SIZE = 64;
    private int MASK = 0x12345600;
    private int MASK_FIND = 32;
    private int TIME_DATA_COLLECT = 1000;

    private int byteCollecter = 0;

    private int state = MASK_FIND;
    private int byteCounter = 0;
    private long timer = 0;

    public void parse(byte data){
        byteCollecter >>= 8;
        byteCollecter &= 0x00FFFFFF;
        byteCollecter |= ((int)data << 24);
        byteCounter++;
        stateTable[state].handler();

        if (state == MASK_FIND) return;
        if(System.currentTimeMillis() - timer < TIME_DATA_COLLECT) return;
        System.out.println("Fuck, where are my bytes?");
        state = MASK_FIND;
    }

    public void parse(byte[] data){
        for(byte source: data)
            parse(source);
    }


    private Complex convertIntToComplex(int data){
        Complex sempl = new Complex(0,0 );
        int re = data & 0x0000FFFF;
        int im = data & 0xFFFF0000;
        re <<= 16;
        re >>= 16;
        im >>= 16;
        sempl.re = (float)re/32768.f;
        sempl.im = (float)im/32768.f;
        return sempl;
    }

    private void reserved(){
        System.out.println("I don't know this command " + state);
        state = MASK_FIND;
        stateTable[state].handler();
    }

    private void packet() {
        if(byteCounter % 4 != 0) return;
        toListenersDdcIQ(convertIntToComplex(byteCollecter));
        //System.out.println("I got a sempl! " + sempl.re + " " + sempl.im);
        if(byteCounter != 4 * PACKET_SIZE) return;
        state = MASK_FIND;
    }
    private void ddcMode() {
        if(byteCounter != 4) return;
        state = MASK_FIND;
        Mode mode = Mode.DISABLE;
        if(byteCollecter == 0) mode = Mode.DISABLE;
        else if(byteCollecter == 1) mode = Mode.ENABLE;
        else if(byteCollecter == 2) mode = Mode.TEST;
        else{System.out.println("Ooo shit !");}
        //System.out.println("Mode is " + mode);
        toListenersDdcMode(mode);
    }
    private void ddcWidth() {
        if(byteCounter != 4) return;
        state = MASK_FIND;
        Width width = Width.kHz_48;;
        if(byteCollecter == 0) width = Width.kHz_48;
        else if(byteCollecter == 1) width = Width.kHz_24;
        else if(byteCollecter == 2) width = Width.kHz_12;
        else if(byteCollecter == 3) width = Width.kHz_6;
        else if(byteCollecter == 4) width = Width.kHz_3;
        else{System.out.println("Ooo shit !");}
        //System.out.println("Width is " + width);
        toListenersDdcWidth(width);
    }
    private void ddcFrequency() {
        if(byteCounter != 4) return;
        state = MASK_FIND;
        //System.out.println("Frequency is " + byteCollecter);
        toListenersDdcFrequency(byteCollecter);
    }

    private void ducMode() {
        if(byteCounter != 4) return;
        state = MASK_FIND;
        Mode mode = Mode.DISABLE;;
        if(byteCollecter == 0) mode = Mode.DISABLE;
        else if(byteCollecter == 1) mode = Mode.ENABLE;
        else if(byteCollecter == 2) mode = Mode.TEST;
        else{System.out.println("Ooo shit !");}
        //System.out.println("Mode is " + mode);
        toListenersDucMode(mode);
    }
    private void ducWidth() {
        if(byteCounter != 4) return;
        state = MASK_FIND;
        Width width = Width.kHz_48;;
        if(byteCollecter == 0) width = Width.kHz_48;
        else if(byteCollecter == 1) width = Width.kHz_24;
        else if(byteCollecter == 2) width = Width.kHz_12;
        else if(byteCollecter == 3) width = Width.kHz_6;
        else if(byteCollecter == 4) width = Width.kHz_3;
        else{System.out.println("Ooo shit !");}
        //System.out.println("Width is " + width);
        toListenersDucWidth(width);
    }
    private void ducFrequency() {
        if(byteCounter != 4) return;
        state = MASK_FIND;
        //System.out.println("Frequency is " + byteCollecter);
        toListenersDucFrequency(byteCollecter);
    }
    private void ducBufferPercent() {
        if(byteCounter != 4) return;
            state = MASK_FIND;
            //System.out.println("% = " + byteCollecter);
            toListenersDucBufferPercent(byteCollecter);

    }

    private void ethernetSettings (){
        int Ip = 0, Mask = 0, Port = 0, GateWay = 0;
        if(byteCollecter == 4) Ip = byteCollecter;
        if(byteCollecter == 8) Mask = byteCollecter;
        if(byteCollecter == 12) Port = byteCollecter;
        if(byteCollecter == 16) GateWay = byteCollecter;
        if(byteCollecter != 16) return;
        state = MASK_FIND;
        //System.out.println("Ip = " + Ip);
        //System.out.println("Mask = " + Mask);
        //System.out.println("port = " + Port);
        //System.out.println("GateWay = " + GateWay);
        toListenersEthernetSettings(Ip, Mask, Port, GateWay);
    }

    //private String initStr = new String();
    private void initString() {
        if(byteCounter != 4) return;
        state = MASK_FIND;
        //System.out.println("init = " + byteCollecter);
        toListenersInit(byteCollecter);
    }

    private void errors() {
        if(byteCounter != 4) return;
        state = MASK_FIND;
        //System.out.println("Палундра !! Ашибка :" + byteCollecter);
        toListenersError(byteCollecter);

    }

    private void maskFind() {
        if((byteCollecter & 0xFFFFFFE0) != MASK) return;
        state = byteCollecter & 0x0000001F;
        byteCounter = 0;
        timer = System.currentTimeMillis();
        //System.out.println("Mask was found");
    }

    private interface stateHandler {void handler();}
    stateHandler[] stateTable = {
            this::packet,
            this::reserved,
            this::ddcMode,
            this::reserved,
            this::ddcWidth,
            this::reserved,
            this::ddcFrequency,
            this::reserved,
            this::reserved,
            this::reserved,
            this::ducMode,
            this::reserved,
            this::ducWidth,
            this::reserved,
            this::ducFrequency,
            this::reserved,
            this::reserved,
            this::ducBufferPercent,
            this::reserved,
            this::reserved,
            this::reserved,
            this::reserved,
            this::ethernetSettings,
            this::reserved,
            this::reserved,
            this::reserved,
            this::reserved,
            this::reserved,
            this::reserved,
            this::reserved,
            this::initString,
            this::errors,
            this::maskFind
    };

}

