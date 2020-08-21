package comUniversal.BitLevel.decoder;

import comUniversal.Core;

public class MessageShowed {

    private String removeCharAt(String s, int pos) {
        return s.substring(0, pos) + s.substring(pos + 1);
    }

    public void Show(String message){

        if( (message == null) ||  (message == "") ||  (message.length() != 6)){
            Core.getCore().informationMolotWindow.setTextMessageStrum("Error, формат даних не коректний");
            System.out.println("Error, формат даних не коректний");
            return;
        } else if(message.charAt(0) == 'C'){
            Core.getCore().informationMolotWindow.setTextMessageStrum(removeCharAt(message, 0) + " - перевідна");
            System.out.println(removeCharAt(message, 0) + " - перевідна");
        } else if(message.charAt(0) == 'B'){
            Core.getCore().informationMolotWindow.setTextMessageStrum(removeCharAt(message, 0) + " - позивний");
            System.out.println(removeCharAt(message, 0) + " - позивний");
        } else if((message.charAt(0) == 'D') || (message.charAt(3) == 'D')){
            Core.getCore().informationMolotWindow.setTextMessageStrum(removeCharAt(message, 0) + " - команди" );
            System.out.println(removeCharAt(message, 0) + " - команди" );
        } else if(message.charAt(0) == 'E'){
            Core.getCore().informationMolotWindow.setTextMessageStrum(removeCharAt(message, 0) + " - радіограма");
            System.out.println(removeCharAt(message, 0) + " - радіограма");
        } else {
            Core.getCore().informationMolotWindow.setTextMessageStrum(removeCharAt(message, 0));
            System.out.println(removeCharAt(message, 0));
        }

    }


}
