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
  
public class JuJpcapPacket {  
	
	private String strFile = "/home/zero/Experiment/a.txt";
	File file = new File(strFile);
      
    public static void main(String[] args)  
    {  
    	 String strFile = "/home/zero/Experiment/a.txt";
    	File file = new File(strFile);
    	 

    	
    	
       /* --------------    µÚÒ»²½°ó¶¨ÍøÂçÉè±¸       -------------- */  
        NetworkInterface[] devices = JpcapCaptor.getDeviceList(); 
        //获取本机上的网络接口对象数组  
          
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
        //intrface参数是监听的网卡；snaplen参数是一次捕获的最大字节数量（本例：65535）；
        //promisc参数，如果为true，接口被设置为混杂模式（本例：true）；
        //to_ms参数，设定processPacket()中的Timeout（本例：20）；当指定接口不能被打开抛出IO异常。  
       /* ----------µÚ¶þ²½×¥°ü-----------------  */
        int i = 0;  
        while(true)  
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
                IPPacket ip = (IPPacket)packet;//Ç¿×ª  
                  
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
          
          
          
          
    }  
  
}  