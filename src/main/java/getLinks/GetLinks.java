package getLinks;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import Util.FileUtil;

public class GetLinks {
	
	
	public  List<String> dpLabel = new ArrayList<>();
	
	public static void print() {}
	
	public static String getLast(StringBuilder sb) {
		String s = new String(sb);
		s = s.substring(s.length()-1, s.length());
		return s;
	}
	
	//本方法获取的是链表里的内容是各个接口，形如“s1-eth2”   转换成   交换机编号+端口编号  这样容易生成流表
	public  List<String> getIFs(String start,String end){
		//1.生成邻接矩阵

		List<String> dpid =  null;
		
		try {
			dpid = FileUtil.fileReaderToListLine("E:\\apache-jmeter-4.0\\bin\\db\\dpid.txt");
		} catch (IOException e) {//读取交换机ID
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] sArr = FileUtil.changeListToArray(dpid);//改成字符数组
		List<String> al = new ArrayList<String>();
		for(String str:sArr) {
			if(str != null) {
				al.add(str);
			}	
		}
		sArr = new String[al.size()];
		for(int i=0;i<sArr.length;i++) {
			sArr[i] = al.get(i);
		}
		
		int index = -1;
		
		for(int i=0;i<sArr.length;i++) {
			if(sArr[i].equals(start)) {
				index =i;
				break;
			}//记录开始IP的下标。
		}
		
		String[][] NearSwitch = new String[al.size()][al.size()];
	

		for(String ss:al) {
			System.out.println("输出al"+ss);
		}
		//(1) 发请求 获得交换机信息
		LinkOfSwitch los = new LinkOfSwitch();

		
		//生成二维LinkClass数组  
		LinkClass[][] lcArr = new LinkClass[al.size()][];
		for(int i=0;i<al.size();i++) {
			//los.writeJmx(al.get(i));              //0000000000000001
			//los.writeBatAndRun(al.get(i));
			
			
			//获取编号为al.get(i)的交换机的响应信息
			String response = los.readJtl(al.get(i));//筛选得到字符串

			System.out.println("res->"+response);
			String pat = "s\\d+-eth\\d";
			Pattern p = Pattern.compile(pat);
			Matcher m = p.matcher(response);
			if(m.find()) {//find()对字符串进行匹配,匹配到的字符串可以在任何位置. 
				//System.out.println(m.group(0));
				String find = m.group(0);//group()返回匹配到的子字符串 端口号
				String[] sp = find.split("-");
				//System.out.println(sp[0]);
				dpLabel.add(sp[0]);
			}

			List<String> alStr = new ArrayList<String>();
			
			//提取交换机的响应信息中关键信息，存储到链表中
			alStr = los.getLinkList(response);
			//打印resp链表中的信息
			for (String string : alStr) {
				System.out.println("输出alStr"+string);
			}
			lcArr[i]  = new LinkClass[alStr.size()];
			//LinkClass[] lc = new LinkClass[alStr.size()];			
			for(int j=0;j<alStr.size();j++) {
				//lc[j] = new LinkClass(alStr.get(j));
				lcArr[i][j] = new LinkClass(alStr.get(j),true);
				System.out.println("输出alStr.get(j)"+alStr.get(j));
				NearSwitch[i][j] =
						lcArr[i][j].getDstSwitch();
				System.out.println("输出NearSwitch[i][j]： "+NearSwitch[i][j]);
			}
		}
		
		System.out.println("打印节点标号");
		for (String string : dpLabel) {
			System.out.println(" "+string);
		}
		System.out.println("节点标号打印结束");

		for(int i=0;i<lcArr.length;i++) {
			for(int j=0;j<lcArr[i].length;j++) {
/*				System.out.println(lcArr[i][j].getDstPort());
				System.out.println(lcArr[i][j].getDstSwitch());
				System.out.println(lcArr[i][j].getSrcPort());*/
			//	System.out.println("dstName"+lcArr[i][j].getDstPName());
			//	System.out.println("srcName"+lcArr[i][j].getSrcPName());
			}
		}
		
		//2.生成邻接节点矩阵
		int[][] NearDist= new int[NearSwitch.length][NearSwitch.length];

		for(int i=0;i<NearSwitch.length;i++) {
			for(int j=0;j<NearSwitch.length;j++) {		
				if(i==j) {
					NearDist[i][j] = 0;
				}else {
					NearDist[i][j] = 1000;
				}
				
			}
		}
		
		for(int i=0;i<NearSwitch.length;i++) {
			for(int j=0;j<NearSwitch.length;j++) {		
				for(int k=0;k<NearSwitch.length;k++) {
					if(NearSwitch[i][j] ==null) {
						continue;
					}
					else if (NearSwitch[i][j].equals( sArr[k])) {
						NearDist[i][k] = 1;
					}	
				}
			}
		}
		
		for(int i=0;i<NearDist.length;i++) {
			for(int j=0;j<NearDist[i].length;j++) {
					System.out.print( " "+   NearDist[i][j]);
			}
			System.out.println();
		}
		
		int vstart = index;
		
		String[] links = Dijkstra.dijkstra(vstart,NearDist);
		//3.生成链路拓扑
		
		String[][] prenode = new String[NearDist.length][2];
 		for(int i=0;i<NearDist.length;i++) {
			String[] sp = links[i].split("->");
			prenode[i][0] = sp[0];
			prenode[i][1] = sp[1];
		}
 		List<String> LinkDes =  new ArrayList<>();
		LinkDes = Dijkstra.find("v"+vstart,links);
		
		for(int i=0;i<LinkDes.size();i++) {
			for(int j=0;j<al.size();j++) {
				if(LinkDes.get(i).contains(i+"")) {
					LinkDes.get(i).replaceAll(i+"", al.get(i));
				}
			}
		}
		
		
		//创建虚拟节点集合，并排序（因为
		List<String> vpot = new ArrayList<>();
		
		for(String stem: LinkDes) {
			Matcher m = Pattern.compile("v\\d+").matcher(stem);
			vpot.remove(stem);
			while(m.find()) {
				if(!(vpot.contains(m.group()))){
					vpot.add(m.group());
				}
			}
		}
		Collections.sort(vpot);
		
		for(String s:LinkDes) {
			System.out.println("输出LinkDes"+s);
		}
		
		for(int i=LinkDes.size();i>0;i--) {
			String s = LinkDes.get(i-1);
			LinkDes.remove(s);
			for(int j=0;j<vpot.size();j++) {
				//System.out.println(s);
				s = s.replaceAll(vpot.get(j), sArr[j]);
				
			}
			LinkDes.add(s);
		}
		
		for(String s:LinkDes) {//��ӡ��·����
			System.out.println("第二次LinkDes"+s);
		}
		
		//#根据其实节点寻找交换机

	//	String end = "0000000000000007";

		
		String sta = "v"+index;

		String route = "";

		boolean flag =false;
		for(String s: LinkDes) {
			if(s.endsWith(end)) {
				route = s;
				
				flag = true;
				break;	
			}
		}
		System.out.println(flag?"true":"false");
		//#根据链路信息获得具体的端口，根据本交换机与下一交换机的信息，来确定交换机间的端口
		/*首先获得当前交换机，然后和sArr字符串比对，获得对应的序号。根据序号找到lcArr中下一交换机所对应的网卡名
		 *  
		 * */
		String[] routes = route.split("->");
		List<String> routeIf = new ArrayList<>();
		
		System.out.println("��ʼ��ӡ");
		
		for(int i=0;i<routes.length-1;i++) {
			for(int j=0;j<sArr.length;j++) {
				if(sArr[j].equals(routes[i])) {
					 for(int k=0;k<lcArr[j].length;k++) {
						 if((lcArr[j][k].getDstSwitch()).equals(routes[i+1])) {
							 routeIf.add(lcArr[j][k].getSrcPName());
							 routeIf.add(lcArr[j][k].getDstPName());
							 break;
						 }
						 else { 
							 continue; 
						 }
					 }
				}
				else {
					continue;
				}
			}
		}
/*				for(String s:routeIf) {
					System.out.println(s);
				}*/
		return routeIf;
	}
	
/************************************************************/	
	
	
	
	
	public  List<String> getIFsWithDpidAndIF(String start,String end){
		//1.生成邻接矩阵
		List<String> dpid =  null;
		try {
			dpid = FileUtil.fileReaderToListLine("E:\\apache-jmeter-4.0\\bin\\db\\dpid.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] sArr = FileUtil.changeListToArray(dpid);
		List<String> al = new ArrayList<String>();
		for(String str:sArr) {
			if(str != null) {
				al.add(str);
			}	
		}
		sArr = new String[al.size()];
		for(int i=0;i<sArr.length;i++) {
			sArr[i] = al.get(i);
		}
		
		int index = -1;
		
		for(int i=0;i<sArr.length;i++) {
			if(sArr[i].equals(start)) {
				index =i;
				break;
			}
		}
		
		String[][] NearSwitch = new String[al.size()][al.size()];
	

		for(String ss:al) {
			System.out.println("输出a1:"+ss);
		}
		//(1) 发请求 获得交换机信息
		LinkOfSwitch los = new LinkOfSwitch();

		
		//生成二维LinkClass数组 
		LinkClass[][] lcArr = new LinkClass[al.size()][];
		for(int i=0;i<al.size();i++) {
			los.writeJmx(al.get(i));              //0000000000000001
			los.writeBatAndRun(al.get(i));
			
			
			//获取编号为al.get(i)的交换机的响应信息
			String response = los.readJtl(al.get(i));
			List<String> alStr = new ArrayList<String>();
			
			//提取交换机的响应信息中关键信息，存储到链表中
			alStr = los.getLinkList(response);
			lcArr[i]  = new LinkClass[alStr.size()];
			//LinkClass[] lc = new LinkClass[alStr.size()];			
			for(int j=0;j<alStr.size();j++) {
				//lc[j] = new LinkClass(alStr.get(j));
				lcArr[i][j] = new LinkClass(alStr.get(j),true);
				System.out.println("输出alStr.get(j)："+alStr.get(j));
				NearSwitch[i][j] =
						lcArr[i][j].getDstSwitch();
				System.out.println("输出NearSwitch[i][j]"+NearSwitch[i][j]);
			}
		}

		for(int i=0;i<lcArr.length;i++) {
			for(int j=0;j<lcArr[i].length;j++) {

			}
		}
		
		//2.生成邻接节点矩阵
		int[][] NearDist= new int[NearSwitch.length][NearSwitch.length];

		for(int i=0;i<NearSwitch.length;i++) {
			for(int j=0;j<NearSwitch.length;j++) {		
				if(i==j) {
					NearDist[i][j] = 0;
				}else {
					NearDist[i][j] = 1000;
				}
				
			}
		}
		
		for(int i=0;i<NearSwitch.length;i++) {
			for(int j=0;j<NearSwitch.length;j++) {		
				for(int k=0;k<NearSwitch.length;k++) {
					if(NearSwitch[i][j] ==null) {
						continue;
					}
					else if (NearSwitch[i][j].equals( sArr[k])) {
						NearDist[i][k] = 1;
					}	
				}
			}
		}
		
		for(int i=0;i<NearDist.length;i++) {
			for(int j=0;j<NearDist[i].length;j++) {
					System.out.print( "  生成邻接节点矩阵NearDist[i][j]是: "+   NearDist[i][j]);
			}
			System.out.println();
		}
		
		int vstart = index;
		String[] links = Dijkstra.dijkstra(vstart,NearDist);
		//3.生成链路拓扑
		String[][] prenode = new String[NearDist.length][2];
 		for(int i=0;i<NearDist.length;i++) {
			String[] sp = links[i].split("->");
			prenode[i][0] = sp[0];
			prenode[i][1] = sp[1];
		}
 		List<String> LinkDes =  new ArrayList<>();
		LinkDes = Dijkstra.find("v"+vstart,links);
		
		for(int i=0;i<LinkDes.size();i++) {
			for(int j=0;j<al.size();j++) {
				if(LinkDes.get(i).contains(i+"")) {
					LinkDes.get(i).replaceAll(i+"", al.get(i));
				}
			}
		}
		
		
		//��������ڵ㼯�ϣ���������Ϊ
		List<String> vpot = new ArrayList<>();
		
		for(String stem: LinkDes) {
			Matcher m = Pattern.compile("v\\d+").matcher(stem);
			vpot.remove(stem);
			while(m.find()) {
				if(!(vpot.contains(m.group()))){
					vpot.add(m.group());
				}
			}
		}
		Collections.sort(vpot);
		
		for(String s:LinkDes) {
			System.out.println("输出LinkDes："+s);
		}
		
		for(int i=LinkDes.size();i>0;i--) {
			String s = LinkDes.get(i-1);
			LinkDes.remove(s);
			for(int j=0;j<vpot.size();j++) {
				//System.out.println(s);
				s = s.replaceAll(vpot.get(j), sArr[j]);
				
			}
			LinkDes.add(s);
		}
		
		for(String s:LinkDes) {//��ӡ��·����
			System.out.println("第二次输出LinkDes"+s);
		}
		
		//#������ʵ�ڵ�Ѱ�ҽ�����

	//	String end = "0000000000000007";

		
		String sta = "v"+index;

		String route = "";

		boolean flag =false;
		for(String s: LinkDes) {
			if(s.endsWith(end)) {
				route = s;
				
				flag = true;
				break;	
			}
		}
		System.out.println(flag?"true":"false");
		//#根据链路信息获得具体的端口，根据本交换机与下一交换机的信息，来确定交换机间的端口
		/*首先获得当前交换机，然后和sArr字符串比对，获得对应的序号。根据序号找到lcArr中下一交换机所对应的网卡名
		 *  
		 * */
		String[] routes = route.split("->");
		List<String> routeIf = new ArrayList<>();
		
		System.out.println("��ʼ��ӡ");
		
		for(int i=0;i<routes.length-1;i++) {
			for(int j=0;j<sArr.length;j++) {
				if(sArr[j].equals(routes[i])) {
					 for(int k=0;k<lcArr[j].length;k++) {
						 if((lcArr[j][k].getDstSwitch()).equals(routes[i+1])) {
							/* routeIf.add(lcArr[j][k].getSrcPName());
							 routeIf.add(lcArr[j][k].getDstPName());*/
							 routeIf.add(lcArr[j][k].getSrcSwitch()+","+lcArr[j][k].getSrcPort());
							 routeIf.add(lcArr[j][k].getDstSwitch()+","+lcArr[j][k].getDstPort());
							 
							 break;
						 }
						 else { 
							 continue; 
						 }
					 }
				}
				else {
					continue;
				}
			}
		}
		

		return routeIf;
	}
	
	
	public static void main(String[] args) throws IOException {

		List<String> al = new GetLinks().getIFs("0000000000000005", "0000000000000004");
		for(String s:al) {
			System.out.println("打印返回结果"+s);
		}
		
		
	
	}
	
@Test
	public void fun(){
		List<String> list = getIFs("0000000000000005", "0000000000000004");
		for (String string : list) {
			System.out.println(string);
		}
	}

}
