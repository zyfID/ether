package Util;


import java.io.File;
import java.io.IOException;
import java.util.List;




public class FlowtableAddUtil {
	//private SplitResponesByDpid srb;
//	private List<String> l;
	private String switchName;
	private String deploy;
	
	public FlowtableAddUtil() {
		
	}
	
	public FlowtableAddUtil(String switchName,String deploy) {
		// TODO Auto-generated constructor stub
		
		//this.l = l;
		this.switchName = switchName;
		this.deploy = deploy;
	}

	public void writeJmx(String switchName,String deploy) {
		File temFile =  new  File("E:\\apache-jmeter-4.0\\bin\\FlowtableAddTem.jmx");
		File jmxFile = new File("E:\\apache-jmeter-4.0\\bin\\FlowtableAddTem"+switchName+".jmx");
		if(jmxFile.exists()) {
			jmxFile.delete();
		}
	
			try {
				SplitResponesByDpid srb = new SplitResponesByDpid();
				String temStr = srb.readFile(temFile);
				temStr = temStr.replaceAll("\\{&quot;dpid&quot;:1,&quot;priority&quot;:22222,&quot;match&quot;:\\{&quot;in_port&quot;:1\\},&quot;instructions&quot;:\\[\\{&quot;type&quot;:&quot;METER&quot;,&quot;meter_id&quot;:2\\}\\]\\}",
						deploy);////
				srb.writeToFile(temStr, jmxFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		
	}
	
	public void writeBatAndRun(String switchName) {
/*		File jtlFile = new File("C:\\apache-jmeter-4.0\\bin\\FlowtableAddTem"+switchName+".jtl");
		if(jtlFile.exists()) {
			jtlFile.delete();
		}*/
		String jtlStr;
		File temFile = new File("E:\\apache-jmeter-4.0\\bin\\bat\\FlowtableAddTem.bat");
		File newBatFile = new File("E:\\apache-jmeter-4.0\\bin\\bat\\FlowtableAddTem"+switchName+".bat");
		if(newBatFile.exists()) {
			newBatFile.delete();
		}
		try {
			SplitResponesByDpid srb = new SplitResponesByDpid();
			jtlStr = srb.readFile(temFile);
			jtlStr = jtlStr.replaceAll("FlowtableAddTem.jmx", 
					"FlowtableAddTem"+switchName+".jmx");
			//jtlStr = jtlStr.replaceAll("FlowtableAddTem.jtl", "FlowtableAddTem"+switchName+".jtl");
			srb.writeToFile(jtlStr, newBatFile);
			String s = "cmd /c start /b "+newBatFile.getPath();
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
