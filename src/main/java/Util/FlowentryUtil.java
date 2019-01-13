package Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Collections;


//流表项类：分离
public class FlowentryUtil {
	private String jtl;
	private SplitResponesByDpid srb = new SplitResponesByDpid();
	
	public FlowentryUtil() {
		
	}
	public FlowentryUtil(String jtl) {
		this.jtl = jtl;
		System.out.println(this.jtl);
	}

	/*
	 * 输入：一个完整的流表项字符串
	 * 过程：将输入字符串分成一个个完成的流表项（以动作作为区分点，因为动作在每条流表项的开头）
	 *       去除掉开头的交换机号
	 * 输出：流表项字符串队列
	 * */
	
	public void writeJmxAndRun(String switchName) {
		
	}
	
	public List<String> splitJtl(){   
		List<String> al = new ArrayList<String>();
		//jtl = srb.readFile(f)
		String[] sp = jtl.split("\"actions\"");//分割字符串
		for(String s:sp) {
			al.add(s);
		}
		al.remove(0);
		/*for(String s:al) {
			
	
		}*/
		for(int i=al.size()-1;i>=0;i--) {
			String s = al.get(i);
			al.remove(i);
			s = s.substring(1);//从1开始到最后字符串
			s = s.substring(0, s.indexOf("}"));			
			al.add(s);
		}
		return al;
	}
	
	
	/*
	 * 输入：一个完整的流表项字符串
	 * 过程：首先引用上个函数的结果，即将获得jtl字符串对应的流表项字段串队列
	 *       然后将该队列中的流表项字符串转换为流表项对象
	 * 输出：一个装了流表项对象的队列
	 * 
	 * */
	public List<FlowentryClass> getClassList(){
		List<FlowentryClass> alf = new ArrayList<FlowentryClass>();
		List<String> al =  new ArrayList<>();
		al = this.splitJtl();//分成一个个流表项
		for(String s:al) {
			FlowentryClass fc = new FlowentryClass(s);    
			alf.add(fc);
		}
		return alf;
	}
	
	/*
	 * 获取字段串youWant出现的流表项中的字节数
	 * */
	public long getAcc (String youWant) {
		long a = 0;
		List<FlowentryClass> alf = new ArrayList<FlowentryClass>();
		alf = this.getClassList();
		long byteCount ;
		for(FlowentryClass fc :alf) {
			if(fc.getMatch().contains(youWant)) {
				//a+=(long)fc.getByte_count();
				if(fc.getByte_count() != "0") {
					fc.setByte_count(fc.getByte_count().trim());  //不去掉的空格 ，转行Long 后会报错
					byteCount = Long.valueOf(fc.getByte_count()).longValue();
				}
				else {
					 byteCount = 0;
				}
				a += byteCount;
			}
		}
		return a;
	} 
	
	
	/*
	 * 获取所有的匹配字段，返回一个匹配字段的包含MAC的匹配字段队列
	 * */
	public List<String> getMAC(){
		List<FlowentryClass> alf = new ArrayList<FlowentryClass>();
		List<String> al =  new ArrayList<>();
		alf = this.getClassList();
		for(FlowentryClass fc:alf) {
			String[] sp = fc.getMatch().split(",");
			//sp数组中，奇数为类型，偶数为数值ֵ
			for(int i=0;i<sp.length;i++) {
				if(!(al.contains(sp[i]))) {
					al.add(sp[i]);
				} 
			}
		}
		return al;
	}
	
	//以正则表达式的方式获取所有流表项中的MAC集合，非重复
		public List<String> getMAC(boolean ispattern){
			List<String> MacStr = new ArrayList<>();
			Pattern p = Pattern.compile("\\S\\S:\\S\\S:\\S\\S:\\S\\S:\\S\\S:\\S\\S");
			Matcher m = p.matcher(jtl);
			while(m.find()) {
				//System.out.println(m.group());
				if(!(MacStr.contains(m.group()))) {
					MacStr.add(m.group());
				}
			}
			Collections.sort(MacStr);
			return MacStr;
		}
		
		//以正则表达式的方式获取所有流表项中的IP集合，非重复
		public void getIP() {
			Pattern p = Pattern.compile("\\d\\d\\d.\\d\\d\\d.\\d\\d\\d.\\d\\d\\d");
			Matcher m = p.matcher(jtl);
			while(m.find()) {
				System.out.println(m.group());
			}
		}
		
		//获取具有最大
		public String getMACwithBiggetByte() {
			List<String> MacStr = new ArrayList<>();
			MacStr = this.getMAC(true);
			long max = 0;
			String maxStr = "";
			for(String s : MacStr) {
				//System.out.println(s);
				System.out.println(s+"="+this.getAcc(s));
				if(this.getAcc(s)> max) {
					max = this.getAcc(s);
					maxStr = s;
				}
			}
			return maxStr;
		}
		
		/*
		 * 获取所有的匹配字段，返回一个匹配字段的包含IP的匹配字段队列
		 * */
		

}
