package comUniversal.lowLevel.DriverHorizon;

import comUniversal.Core;

public class DriverHorizon {


    public Status setFrequency(int frequency){

        Status status = Status.SUCCESSFUL;

        if(frequency < 0)
            status = Status.IS_NEGATIVE;

        if(frequency > 1000000)
            status = Status.IS_TO_HIGH;

        byte[] command = new byte[8];
        command[0]= 0x05;
        command[1]= 0x56;
        command[2]= 0x34;
        command[3]= 0x12;
        command[4]= 0x01;
        command[5]= 0x00;
        command[6]= 0x00;
        command[7]= 0x00;


        Core.getCore().ethernetDriver.writeBytes(command);
        return status;
    }


    public static void main(String[] args) {

        DriverHorizon driverHorizon = new DriverHorizon();
        //Core.getCore().ethernetDriver.doInit("192.168.0.1",81);

        while(Core.getCore().ethernetDriver.isConnect());
        System.out.println("Connected");
        driverHorizon.setFrequency(128000);

    }
}



