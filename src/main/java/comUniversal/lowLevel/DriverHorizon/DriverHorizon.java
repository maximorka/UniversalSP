package comUniversal.lowLevel.DriverHorizon;

import comUniversal.Complex;

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

    // Listeners
    private List<TransferDataBytes> transfer = new ArrayList<>();
    public void addTransferListener(TransferDataBytes listener){transfer.add(listener);}
    public void clearTransferListener(){transfer.clear();}
    private void toListeners(byte[] data){
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
        toListeners(result);

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
        toListeners(result);
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






}


