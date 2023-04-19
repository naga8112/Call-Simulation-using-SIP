import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Main
{
    private static final int MAXI = 2048;
    private static HashMap<String,String> reg = new HashMap<String,String>();
    private static HashMap<String,callDetails> currcalls = new HashMap<String,callDetails>();
    
    public static void main(String[] args) throws IOException{           
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.print("Enter the port number of the server : ");
        int serverport = Integer.parseInt(br.readLine());
        
        System.out.print("Enter the IP address of the server : ");
        String serverip = br.readLine();
        
        System.out.println("Server Started. Listening for requests "+serverport);
        
        DatagramSocket socket = new DatagramSocket(serverport) ;
        DatagramPacket packet = new DatagramPacket(new byte[MAXI], MAXI);
       
            
        while(true){
            socket.receive(packet);
            
            InetAddress clientAddress = packet.getAddress();
            int clientPort = packet.getPort();
            
            byte[] arr = new byte[packet.getLength()];
            System.arraycopy(packet.getData(), packet.getOffset(), arr, 0, arr.length);
            String requestMsg = new String(arr);//PRINT THIS IF NEEDED
            //System.out.println(requestMsg);
            
            StringTokenizer st = new StringTokenizer(requestMsg,"\r\n");
            String firstline = st.nextToken();
            String msg = firstline.substring(0, firstline.indexOf(" "));
            
            if("REGISTER".equals(msg)){
                enterRequest r = new enterRequest(); 
                String fname = "";
                String nextLine = "";
                while(st.hasMoreTokens())
                {
                    nextLine = st.nextToken();
                    fname = nextLine.substring(0,nextLine.indexOf(":"));
                    if("Via".equals(fname))
                        r.via = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("From".equals(fname))
                        r.from = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("To".equals(fname))
                        r.to = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Call-ID".equals(fname))
                        r.callId = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("CSeq".equals(fname))
                        r.cSeq = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Contact".equals(fname))
                        r.contact = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Allow".equals(fname))
                        r.allow = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Max-Forwards".equals(fname))
                        r.maxForwards = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Allow-Events".equals(fname))
                        r.allowEvents = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("User-Agent".equals(fname))
                        r.userAgent = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Supported".equals(fname))
                        r.supported = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Expires".equals(fname))
                        r.expires = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Content-Length".equals(fname))
                        r.contentLength = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                }
                
                String cn = r.contact.substring(r.contact.indexOf(":")+1, r.contact.indexOf(";"));
                String number,ipPort;
                
                if(!cn.contains(">")){
                    number = cn.substring(0, cn.indexOf("@"));
                    ipPort = cn.substring(cn.indexOf("@")+1, cn.length());
                }
                else{
                    number = cn.substring(0, cn.indexOf("@"));
                    ipPort = cn.substring(cn.indexOf("@")+1, cn.length()-1);
                }
                boolean alreadyRegistered = reg.containsKey(number);
                int expires = Integer.parseInt(r.expires.trim());
                
                if(!alreadyRegistered && expires > 0){
                    System.out.println("Phone "+number+" is Successfully Registered at "+ipPort+" .");
                    reg.put(number, ipPort);
                }
                    
                else if(alreadyRegistered && expires == 0){
                    System.out.println("Phone "+number+" is Successfully UNreg.");
                    reg.remove(number);
                }
                
                byte[] send = r.OK_200().getBytes();
                DatagramPacket p = new DatagramPacket(new byte[MAXI], MAXI);
                p.setAddress(clientAddress);
                p.setPort(clientPort);
                p.setData(send);
                socket.send(p);
            }
            
            if("INVITE".equals(msg)){
                Calling r = new Calling();
                String fname = "";
                String nextLine = "";
                while(st.hasMoreTokens()){
                    nextLine = st.nextToken();
                    fname = nextLine.substring(0,nextLine.indexOf(":"));
                    
                    if("Via".equals(fname))
                        r.via.add(nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length()));
                    else if("From".equals(fname))
                        r.from = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("To".equals(fname))
                        r.to = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Call-ID".equals(fname))
                        r.callId = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("CSeq".equals(fname))
                        r.cSeq = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Contact".equals(fname))
                        r.contact = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Content-Type".equals(fname))
                        r.contentType = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Allow".equals(fname))
                        r.allow = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Max-Forwards".equals(fname))
                        r.maxForwards = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Supported".equals(fname))
                        r.supported = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("P-Early-Media".equals(fname))
                        r.pEarlyMedia = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("User-Agent".equals(fname))
                        r.userAgent = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("P-Preferred-Identity".equals(fname))
                        r.prefferedIdentity = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Content-Length".equals(fname))
                    {
                        r.contentLength = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                        break;
                    }
                }
                while(st.hasMoreTokens())  {
                    nextLine = st.nextToken();
                    fname = nextLine.substring(0,nextLine.indexOf("="));
                    if("v".equals(fname))
                        r.v = nextLine.substring(nextLine.indexOf("=")+1,nextLine.length());
                    else if("o".equals(fname))
                        r.o = nextLine.substring(nextLine.indexOf("=")+1,nextLine.length());
                    else if("s".equals(fname))
                        r.s = nextLine.substring(nextLine.indexOf("=")+1,nextLine.length());
                    else if("c".equals(fname))
                        r.c = nextLine.substring(nextLine.indexOf("=")+1,nextLine.length());
                    else if("t".equals(fname))
                        r.t = nextLine.substring(nextLine.indexOf("=")+1,nextLine.length());
                    else if("m".equals(fname))
                        r.m = nextLine.substring(nextLine.indexOf("=")+1,nextLine.length());
                    else if("a".equals(fname))
                        r.a.add(nextLine.substring(nextLine.indexOf("=")+1,nextLine.length()));
                }
                
                String caller = r.from.substring(r.from.indexOf(":")+1, r.from.indexOf("@"));
                
                String callee = firstline.substring(firstline.indexOf(":")+1,firstline.indexOf("@"));
                String calleeIp = extract(reg.get(callee),0);
                String calleePort = extract(reg.get(callee),1);
                
                System.out.println("INVITE coming from "+caller+" to " + callee);
                
                byte[] send = r.TRYING_100().getBytes();
                DatagramPacket p = new DatagramPacket(new byte[MAXI], MAXI);
                p.setAddress(clientAddress);
                p.setPort(clientPort);
                p.setData(send);
                socket.send(p);
                
                byte[] send1 = r.forwardInvite(firstline, serverip, serverport).getBytes();
                DatagramPacket p1 = new DatagramPacket(new byte[MAXI], MAXI);
                p1.setAddress(InetAddress.getByName(calleeIp));
                p1.setPort(Integer.parseInt(calleePort.trim()));
                p1.setData(send1);
                socket.send(p1);
                
                callDetails cd = new callDetails();
                cd.caller = caller;
                cd.called = callee;
                
                String callId = r.callId.substring(0, r.callId.indexOf("@"));
                currcalls.put(callId, cd);
                
            }
            if("SIP/2.0 180 Ringing".equals(firstline)){
                Ringing r = new Ringing(); 
                String fname = "";
                String nextLine = "";
                while(st.hasMoreTokens()){
                    nextLine = st.nextToken();
                    fname = nextLine.substring(0,nextLine.indexOf(":"));
                    if("Via".equals(fname))
                        r.via.add(nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length()));
                    else if("From".equals(fname))
                        r.from = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("To".equals(fname))
                        r.to = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Call-ID".equals(fname))
                        r.callId = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("CSeq".equals(fname))
                        r.cSeq = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Contact".equals(fname))
                        r.contact = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Allow".equals(fname))
                        r.allow = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Max-Forwards".equals(fname))
                        r.maxForwards = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("User-Agent".equals(fname))
                        r.userAgent = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Supported".equals(fname))
                        r.supported = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Content-Length".equals(fname))
                        r.contentLength = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                }
                String clientname = r.from.substring(r.from.indexOf(":")+1, r.from.indexOf("@"));
                String clientip = extract(reg.get(clientname),0);
                String clientport = extract(reg.get(clientname),1);
                
                System.out.println("Ringing forwarded to "+clientname+" at "+clientip+":"+clientport+" .");
                
                byte[] send = r.forwardRinging(serverip).getBytes();
                DatagramPacket p = new DatagramPacket(new byte[MAXI], MAXI);
                p.setAddress(InetAddress.getByName(clientip));
                p.setPort(Integer.parseInt(clientport.trim()));
                p.setData(send);
                socket.send(p);
            }
            if("SIP/2.0 200 OK".equals(firstline)){
                Connected r = new Connected();
                String fname = "";
                String nextLine = "";
                while(st.hasMoreTokens()){
                    nextLine = st.nextToken();
                    fname = nextLine.substring(0,nextLine.indexOf(":"));
                    if("Via".equals(fname))
                        r.via.add(nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length()));
                    else if("From".equals(fname))
                        r.from = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("To".equals(fname))
                        r.to = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Call-ID".equals(fname))
                        r.callId = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("CSeq".equals(fname))
                        r.cSeq = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Contact".equals(fname))
                        r.contact = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Content-Type".equals(fname))
                        r.contentType = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Allow".equals(fname))
                        r.allow = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Max-Forwards".equals(fname))
                        r.maxForwards = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Supported".equals(fname))
                        r.supported = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("P-Early-Media".equals(fname))
                        r.pEarlyMedia = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("User-Agent".equals(fname))
                        r.userAgent = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("P-Preferred-Identity".equals(fname))
                        r.prefferedIdentity = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Content-Length".equals(fname)){
                        r.contentLength = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                        break;
                    }
                }
                while(st.hasMoreTokens()){
                    nextLine = st.nextToken();
                    fname = nextLine.substring(0,nextLine.indexOf("="));
                    if("v".equals(fname))
                        r.v = nextLine.substring(nextLine.indexOf("=")+1,nextLine.length());
                    else if("o".equals(fname))
                        r.o = nextLine.substring(nextLine.indexOf("=")+1,nextLine.length());
                    else if("s".equals(fname))
                        r.s = nextLine.substring(nextLine.indexOf("=")+1,nextLine.length());
                    else if("c".equals(fname))
                        r.c = nextLine.substring(nextLine.indexOf("=")+1,nextLine.length());
                    else if("t".equals(fname))
                        r.t = nextLine.substring(nextLine.indexOf("=")+1,nextLine.length());
                    else if("m".equals(fname))
                        r.m = nextLine.substring(nextLine.indexOf("=")+1,nextLine.length());
                    else if("a".equals(fname))
                        r.a.add(nextLine.substring(nextLine.indexOf("=")+1,nextLine.length()));
                }
                String clientname = r.from.substring(r.from.indexOf(":")+1, r.from.indexOf("@"));
                String clientip = extract(reg.get(clientname),0);
                String clientport = extract(reg.get(clientname),1);
                System.out.println("OK sent to "+clientname+" at "+clientip+":"+clientport+" .");
                byte[] send = r.forwardOk(serverip).getBytes();
                DatagramPacket p = new DatagramPacket(new byte[MAXI], MAXI);
                p.setAddress(InetAddress.getByName(clientip));
                p.setPort(Integer.parseInt(clientport.trim()));
                p.setData(send);
                socket.send(p);  
            }   
            packet.setLength(MAXI);
        }
    }
    private static String extract(String s,int ch){
        if(ch == 0) return s.substring(0, s.indexOf(":"));
        else return s.substring(s.indexOf(":")+1);
    }
}