package UI;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import Util.FileUtil;
import Util.SplitResponesByDpid;


/**
 * @author jusang
 * 用于替换配置文件里的控制器IP
 */
public class ChangeControllerIP {
	public static void main(String[] args) {
	    ArrayList<String> files = new ArrayList<String>();
	    File file = new File("E:/apache-jmeter-4.0/bin");
	    File[] tempList = file.listFiles();
	    int index =0;
	    for (int i = 0; i < tempList.length; i++) {
	        if (tempList[i].isFile()) {
//	            
	        	File f = tempList[i];
	           try {
				String s = new SplitResponesByDpid().readFile(f);
				String src =s ;
				s =s.replaceAll("10.20.221.33", "10.20.0.3");
				if(!s.equals(src)){
					f.delete();
					new SplitResponesByDpid().writeToFile(s, f);
					System.out.println(tempList[i].getName());
					System.out.println(index++);
				}
			
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        	
	        	
	        }
	        
	    }
	    System.out.println("over");
	 //   return files;

	}
}
