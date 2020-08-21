package comUniversal.BitLevel.decoder;

public class MessageShowed {

    private String removeCharAt(String s, int pos) {
        return s.substring(0, pos) + s.substring(pos + 1);
    }

    public void Show(String message){

        if( (message == null) ||  (message == "") ||  (message.length() != 6)){
            System.out.println("Error, формат даних не коректний");
            return;
        } else if(message.charAt(0) == '1'){
            System.out.println(removeCharAt(message, 0) + " - перевідна");
        } else if(message.charAt(0) == '2'){
            System.out.println(removeCharAt(message, 0) + " - позивний");
        } else if((message.charAt(0) == '3') || (message.charAt(3) == '3')){
            System.out.println(removeCharAt(message, 0) + " - команди" );
        } else if(message.charAt(0) == '4'){
            System.out.println(removeCharAt(message, 0) + " - радіограма");
        } else {
            System.out.println(removeCharAt(message, 0));
        }

    }


}
