package comUniversal.BitLevel.decoder;

import comUniversal.Core;

public class MessageShowed {

    private String removeCharAt(String s, int pos) {
        return s.substring(0, pos) + s.substring(pos + 1);
    }

    public void Show(String message){

        if( (message == null) ||  (message == "") ||  (message.length() != 6)){
            Core.getCore().informationMolotWindow.setTextMessageStrum("Error, формат даних не коректний");
            //System.out.println("Error, формат даних не коректний");
            return;
        } else if((message.charAt(0) == 'C') && (message.charAt(3) != 'D')){

            Core.getCore().informationMolotWindow.setTextMessageStrum(message + " - перевідна");
            //System.out.println(removeCharAt(message, 0) + " - перевідна");



        } else if((message.charAt(0) == 'B') && (message.charAt(3) != 'D')){
            Core.getCore().informationMolotWindow.setTextMessageStrum(message + " - позивний");
            //System.out.println(removeCharAt(message, 0) + " - позивний");
        } else if((message.charAt(0) == 'D') || (message.charAt(3) == 'D')){
            String command1 = String.valueOf(message.charAt(1)) + String.valueOf(message.charAt(2));
            String command2 = String.valueOf(message.charAt(4)) + String.valueOf(message.charAt(5));
            if(command1.equals(command2)) {
                if((command1.charAt(0) != '*') && command1.charAt(1) != '*' ) {
                    Core.getCore().informationMolotWindow.setTextMessageStrum(message + " - команда");
                    //System.out.println(removeCharAt(message, 0) + " - команди" );
                }
            }
        } else if((message.charAt(0) == 'E') && (message.charAt(3) != 'D')){
            Core.getCore().informationMolotWindow.setTextMessageStrum(message + " - радіограма");
            //System.out.println(removeCharAt(message, 0) + " - радіограма");
        } else {
            Core.getCore().informationMolotWindow.setTextMessageStrum(message);
            //System.out.println(removeCharAt(message, 0));
        }

    }


}
