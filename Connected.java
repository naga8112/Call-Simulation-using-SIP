public class Connected extends Calling{
    Connected(){
        super();
    }
    public String forwardOk(String servIp){
        String fwd_res = "SIP/2.0 200 OK\r\n";
        
        if(via.get(0).contains(",")){
            String modifiedVia = via.get(0).substring(via.get(0).indexOf(",")+1,via.get(0).length());
            via.set(0,modifiedVia);
        }
        else via.remove(0);
        
        for(int in=0;in<via.size();in++)
            fwd_res = fwd_res + "Via: " + via.get(in) + "\r\n";
            fwd_res = fwd_res + "From: " + from + "\r\n";
            fwd_res = fwd_res + "To: " + to + "\r\n";
            fwd_res = fwd_res+ "Call-ID: " + callId + "\r\n";
            fwd_res = fwd_res + "CSeq: " + cSeq + "\r\n";
        
            return fwd_res;
    }
}
