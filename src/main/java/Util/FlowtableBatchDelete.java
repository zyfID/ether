package Util;

import java.io.File;
import java.io.IOException;

//批量删除
public class FlowtableBatchDelete {
	private static SplitResponesByDpid srb = new SplitResponesByDpid();
	
	public static void writeFile(String str) {
		File temFile = new File("E:\\apache-jmeter-4.0\\bin\\FlowentryDeleteTem.jmx");
		File newFile = new File("E:\\apache-jmeter-4.0\\bin\\FlowentryDeleteNewFile.jmx");
		if(newFile.exists()) {
			newFile.delete();
		}
		try {
			String temStr = srb.readFile(temFile);
			temStr = temStr.replaceAll("asdfasdfasdfasdf", str);
			srb.writeToFile(temStr, newFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void runBatchAddBat() {
		File temBat = new File("E:\\apache-jmeter-4.0\\bin\\bat\\FlowentryDelete.bat");
		String s ="cmd /c start /b " + temBat.getPath();
		new BatUtil(s);	
	}
}
