import java.util.ArrayList;

public class Calling extends Request{
    String contentType,pEarlyMedia,prefferedIdentity;
    String v,o,s,c,t,m;
    ArrayList<String> a,via;
    
    public Calling(){
        super();
        this.contentType = "";
        this.pEarlyMedia = "";
        this.prefferedIdentity = "";
        
        this.v = "";
        this.c = "";
        this.m = "";
        this.o = "";
        this.s = "";
        this.t = "";
        
        a = new ArrayList<>();
        via = new ArrayList<>();
    }
    public String TRYING_100(){
        String trying_res = "SIP/2.0 100 TRYING\r\n";
        
        String upperViaFeild = via.get(0);
        String recieved = upperViaFeild.substring(upperViaFeild.indexOf(" ")+1, upperViaFeild.indexOf(":"));
        upperViaFeild = upperViaFeild + ";recieved=" + recieved;
        via.set(0, upperViaFeild);
        
        for(int in=0;in<via.size();in++)
            trying_res = trying_res + "Via: " + via.get(in) + "\r\n";
        
        trying_res = trying_res + "From: " + from + "\r\n";
        trying_res = trying_res + "To: " + to + "\r\n";
        trying_res = trying_res + "Call-ID: " + callId + "\r\n";
        trying_res = trying_res + "CSeq: " + cSeq + "\r\n";
        trying_res = trying_res + "Allow: " + allow + "\r\n";
        trying_res = trying_res + "User-Agent: " + userAgent + "\r\n";
        trying_res = trying_res + "Supported: " + supported + "\r\n";
        trying_res = trying_res + "Content-Length: 0" + "\r\n";
        
        trying_res = trying_res + "\r\n";
        return trying_res;
    }
    
    public String forwardInvite(String line1,String servIp,int servPort)    {
        String forwardresponse = line1 + "\r\n";
        
        via.add(0,"SIP/2.0/UDP "+servIp+":"+servPort+";branch=z9hG4bK2d4790");
        
        for(int in=0;in<via.size();in++)
            forwardresponse = forwardresponse + "Via: " + via.get(in) + "\r\n";
        
        forwardresponse = forwardresponse + "From: " + from + "\r\n";
        forwardresponse = forwardresponse + "To: " + to + "\r\n";
        forwardresponse = forwardresponse+ "Call-ID: " + callId + "\r\n";
        forwardresponse = forwardresponse + "CSeq: " + cSeq + "\r\n";
        
        String modifiedContact = contact.substring(0,contact.indexOf("@")+1)+ servIp+">"; 
        forwardresponse = forwardresponse + "Contact: " + modifiedContact + "\r\n";
        
        forwardresponse = forwardresponse + "Content-Type: " + contentType + "\r\n";
        forwardresponse = forwardresponse + "Max-Forwards: " + (Integer.parseInt(maxForwards.trim())-1) + "\r\n";
        forwardresponse = forwardresponse + "User-Agent: " + userAgent + "\r\n";
        forwardresponse = forwardresponse + "Content-Length: " + contentLength + "\r\n\r\n";
        
        forwardresponse = forwardresponse + "v=" + v + "\r\n";
        forwardresponse = forwardresponse + "o=" + o + "\r\n";
        forwardresponse = forwardresponse + "s=" + s + "\r\n";
        forwardresponse = forwardresponse + "c=" + c + "\r\n";
        forwardresponse = forwardresponse + "t=" + t + "\r\n";
        forwardresponse = forwardresponse + "m=" + m + "\r\n";
        
        for(int in=0;in<a.size();in++)
            forwardresponse = forwardresponse + "a=" + a.get(in) + "\r\n";
        
        return forwardresponse;
    }
}
