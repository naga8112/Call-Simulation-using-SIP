public class enterRequest extends Request{
    String allowEvents,expires;
    
    public enterRequest(String via,String from,String to,String callId,String cSeq,String contact,String allow
    ,String maxForwards,String allowEvents,String userAgent,String supported,String expires,
    String contentLength){
        this.via = via;
        this.from = from;
        this.to = to;
        this.callId = callId;
        this.cSeq = cSeq;
        this.contact = contact;
        this.allow = allow;
        this.maxForwards = maxForwards;
        this.allowEvents = allowEvents;
        this.userAgent = userAgent;
        this.supported = supported;
        this.expires = expires;
        this.contentLength = contentLength;
    }
    public enterRequest(){
        super();
        this.allowEvents = "";
        this.expires = "";      
    }
    public String OK_200(){
        String result = "SIP/2.0 200 OK\r\n";
        
        result = result + "Via: " + via + "\r\n";
        result = result + "From: " + from + "\r\n";
        result = result + "To: " + to + "\r\n";
        result = result + "Call-ID: " + callId + "\r\n";
        result = result + "CSeq: " + cSeq + "\r\n";
        result = result + "Contact: " + contact + "\r\n";
        result = result + "Allow: " + allow + "\r\n";
        result = result + "Max-Forwards: " + maxForwards + "\r\n";
        result = result + "Allow-Events: " + allowEvents + "\r\n";
        result = result + "User-Agent: " + userAgent + "\r\n";
        result = result + "Supported: " + supported + "\r\n";
        result = result + "Expires: " + expires + "\r\n";
        result = result + "Content-Length: " + contentLength + "\r\n";
        
        result = result + "\r\n";
        return result;
    }
}