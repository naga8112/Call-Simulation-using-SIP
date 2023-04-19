public class Request{
    String via,from,to,callId,cSeq,contact,allow,maxForwards,userAgent,supported,contentLength;
    
    public Request()
    {
        this.allow = "";
        this.cSeq = "";
        this.callId = "";
        this.contact = "";
        this.contentLength = "";
        this.from = "";
        this.maxForwards = "";
        this.supported = "";
        this.to = "";
        this.userAgent = "";
        this.via = "";
    }
}
