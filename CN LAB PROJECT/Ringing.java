import java.util.ArrayList;
public class Ringing extends Request{
    ArrayList<String> via;
    Ringing(){
        super();
        via = new ArrayList<>();
    }
    public String forwardRinging(String servIp){
        String fwd_res = "SIP/2.0 180 Ringing\r\n";
        
        String modifiedVia = via.get(0).substring(via.get(0).indexOf(",")+1,via.get(0).length());
        via.set(0,modifiedVia);
        
        for(int in=0;in<via.size();in++)
            fwd_res = fwd_res + "Via: " + via.get(in) + "\r\n";
        
        fwd_res = fwd_res + "From: " + from + "\r\n";
        fwd_res = fwd_res + "To: " + to + "\r\n";
        fwd_res = fwd_res+ "Call-ID: " + callId + "\r\n";
        fwd_res = fwd_res + "CSeq: " + cSeq + "\r\n";
        
        String modifiedContact = contact.substring(0,contact.indexOf("@")+1)+ servIp+">"; 
        fwd_res = fwd_res + "Contact: " + modifiedContact + "\r\n";

        fwd_res = fwd_res + "User-Agent: " + userAgent + "\r\n";
        
        fwd_res = fwd_res + "Content-Length: 0" + "\r\n\r\n";
        
        return fwd_res;
    }
}
