package dataProcess;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;
  
public class SumItUp {  
	
	private String strFile = "/home/zero/Experiment/a.txt";
	File file = new File(strFile);
      
	
	//2.run the functions in WriteSampleDateToCsv.class 
	public void runWSDTC() {
		WriteSampleDataToCsv gal = new WriteSampleDataToCsv();	
		gal.saveAsTrippleArray();
		gal.getTimesOfIpOnline();
	}
	//3.run the functions in the GetUserTypeFromCsv.class
	public void runGUTFC() {
		GetUserTypeFromCsv c = new GetUserTypeFromCsv();
		c.writeResultToFile();
	}
	
	//4.Transfer the file to another Computer whose Ip is 10.20.216.223
	public void runSocket() {
        try {  
            FileTransferClient client = new FileTransferClient(); // 鍚姩瀹㈡埛绔繛鎺�  
            client.sendFile(); // 浼犺緭鏂囦欢  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
	}
	
    public static void main(String[] args)  
    {  
    	
    	//1.capture traffics
    	 String strFile = "/home/zero/Experiment/a.txt";
    	File file = new File(strFile);


    	
    	
          
        NetworkInterface[] devices = JpcapCaptor.getDeviceList();  
          
        for(NetworkInterface n : devices)  
        {  
            System.out.println(n.name + "     |     " + n.description);  
        }  
        System.out.println("-------------------------------------------");  
          
        JpcapCaptor jpcap = null;  
        int caplen = 1512;  
        boolean promiscCheck = true;  
          
        try{  
            jpcap = JpcapCaptor.openDevice(devices[0], caplen, promiscCheck, 50);  
        }catch(IOException e)  
        {  
            e.printStackTrace();  
        }  
          
        
        int i = 0;  
        
        //Through the end time and start time , we could precisely capture traffics in a week
        long startTime = System.currentTimeMillis();
        long endTime = startTime + 604800000; // 1 week = 7days*24h*3600s*1000 ms
        
        while(endTime>System.currentTimeMillis())   
        {  
        	
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            Packet packet  = jpcap.getPacket();  
            if(packet instanceof IPPacket && ((IPPacket)packet).version == 4)  
            {  
                i++;  
                IPPacket ip = (IPPacket)packet;//脟驴脳陋  
                  
                System.out.println("IPv4");  
                System.out.println(new Date().getTime());
                System.out.println("Priority:" + ip.priority);  
                System.out.println("Differentiate: biggest throughput " + ip.t_flag);  
                System.out.println("highest available:" + ip.r_flag);  
                System.out.println("length:" + ip.length);  
                System.out.println("identification:" + ip.ident);  
                System.out.println("DF:Don't Fragment: " + ip.dont_frag);  
                System.out.println("NF:Nore Fragment: " + ip.more_frag);  
                System.out.println("pian pian yi:" + ip.offset);  
                System.out.println("sheng cun shi jian "+ ip.hop_limit);  
                  
                String protocol ="";  
                switch(new Integer(ip.protocol))  
                {  
                case 1:protocol = "ICMP";break;  
                case 2:protocol = "IGMP";break;  
                case 6:protocol = "TCP";break;  
                case 8:protocol = "EGP";break;  
                case 9:protocol = "IGP";break;  
                case 17:protocol = "UDP";break;  
                case 41:protocol = "IPv6";break;  
                case 89:protocol = "OSPF";break;  
                default : break;  
                }  
                System.out.println("protocol:" + protocol);  
                System.out.println("sourceIP: " + ip.src_ip.getHostAddress());  
                System.out.println("DstIP: " + ip.dst_ip.getHostAddress());  
                System.out.println("SrcHostName " + ip.src_ip);  
                System.out.println("DstHostName " + ip.dst_ip);  
                System.out.println("----------------------------------------------"); 
                
                String piecePacket = "timestamp"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"   src_ip:"+ip.src_ip+"    dst_ip:"+ip.dst_ip+"\r\n";
            	FileOutputStream fis = null;
        		try {
        			fis = new FileOutputStream(file,true);
        		} catch (FileNotFoundException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
        		try {
        			fis.write(piecePacket.getBytes());
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
        		finally {
        			try {
        				fis.flush();
        				fis.close();
        			} catch (IOException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}
        			
        		}
            }  
        }  
          
        SumItUp siu = new SumItUp();
    	
    	//2.run the functions in WriteSampleDateToCsv.class 
        siu.runWSDTC() ;
    
    	//3.run the functions in the GetUserTypeFromCsv.class
        siu.runGUTFC(); 
    	
    	//4.Transfer the file to another Computer whose Ip is 10.20.216.223
        siu.runSocket() ;
          
          
    }  
 
}  