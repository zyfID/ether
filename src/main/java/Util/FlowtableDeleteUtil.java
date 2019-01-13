package Util;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FlowtableDeleteUtil {
	private String deploy;
	private String switchName;
	//private SplitResponesByDpid srb = new SplitResponesByDpid();
	
	public FlowtableDeleteUtil() {
		
	}
	
	public FlowtableDeleteUtil(String deploy,String switchName) {
		// TODO Auto-generated constructor stub
		this.switchName = switchName;
		this.deploy = deploy;
	}
	
	public void writeJmx(String switchName,String deploy) {
		 SplitResponesByDpid srb = new SplitResponesByDpid();
		File temJmx = new File("E:\\apache-jmeter-4.0\\bin\\FlowtableDeleteTem.jmx");
		File newJmx = new File("E:\\apache-jmeter-4.0\\bin\\FlowtableDeleteTem"+switchName+".jmx");
		if(newJmx.exists()) {
			newJmx.delete();
		}
		try {
			String str = srb.readFile(temJmx);
			str = str.replaceAll("\\{&quot;dpid&quot;:0000000000000003,&quot;cookie&quot;:0,&quot;cookie_mask&quot;:0,&quot;table_id&quot;:0,&quot;idle_timeout&quot;:0,&quot;hard_timeout&quot;:0,&quot;priority&quot;:4444,&quot;buffer_id&quot;:0,&quot;flag&quot;:0,&quot;match&quot;:\\{\\},&quot;action&quot;:\\[\\]\\}",deploy);
			srb.writeToFile(str, newJmx);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void writeBatAndRun(String switchName) {
		// TODO Auto-generated method stub
		 SplitResponesByDpid srb = new SplitResponesByDpid();
		File temBat = new File("E:\\apache-jmeter-4.0\\bin\\bat\\FlowtableDeleteTem.bat");
		File newBat = new File("E:\\apache-jmeter-4.0\\bin\\bat\\FlowtableDeleteTem"+switchName+".bat");
/*		File newJtl = new File("C:\\apache-jmeter-4.0\\bin\\FlowtableDeleteTem"+switchName+".jtl");
		if(newJtl.exists()) {
			newJtl.delete();
		}*/
		if(newBat.exists()) {
			newBat.delete();
		}
		String str;
		try {
			str = srb.readFile(temBat);
			str = str.replaceAll("FlowtableDeleteTem.jmx",
					"FlowtableDeleteTem"+switchName+".jmx");
/*			str = str.replaceAll("FlowtableDeleteTem.jtl", 
					"FlowtableDeleteTem"+switchName+".jtl");*/
			srb.writeToFile(str, newBat);
			String s = "cmd /c start /b "+newBat.getPath();
			//System.out.println(s);
			new BatUtil(s);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}
