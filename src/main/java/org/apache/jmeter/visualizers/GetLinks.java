package org.apache.jmeter.visualizers;

import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;
import javax.swing.JPanel;




public class GetLinks {
	
	public static void print() {}
	
	public static String getLast(StringBuilder sb) {
		String s = new String(sb);
		s = s.substring(s.length()-1, s.length());
		return s;
	}
	
	public static List<String> getIFs(String start,String end){
		//1.生成邻接矩阵
		
		

		
				//String[] sArr={};
				//FileUtil fu = new FileUtil();
				List<String> dpid =  null;
				try {
					dpid = FileUtil.fileReaderToListLine("C:\\apache-jmeter-4.0\\bin\\dpid.txt");
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
					System.out.println(ss);
				}
				//(1) 发请求 获得交换机信息
				LinkOfSwitch los = new LinkOfSwitch();

				
				//生成二维LinkClass数组 
				LinkClass[][] lcArr = new LinkClass[al.size()][];
				for(int i=0;i<al.size();i++) {
					los.writeJmx(al.get(i));              //0000000000000001
					los.writeBatAndRun(al.get(i));
					String response = los.readJtl(al.get(i));
					List<String> alStr = new ArrayList<String>();	
					alStr = los.getLinkList(response);
					lcArr[i]  = new LinkClass[alStr.size()];
					//LinkClass[] lc = new LinkClass[alStr.size()];			
					for(int j=0;j<alStr.size();j++) {
						//lc[j] = new LinkClass(alStr.get(j));
						lcArr[i][j] = new LinkClass(alStr.get(j),true);
						System.out.println(alStr.get(j));
						NearSwitch[i][j] =
								lcArr[i][j].getDstSwitch();
						//System.out.println(lc[j]);
					}
				}

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
							//System.out.print( "   "+   NearDist[i][j]);
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
					System.out.println(s);
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
				
				for(String s:LinkDes) {//打印链路队列
					System.out.println(s);
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
				
				System.out.println("开始打印");
				
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

	
	
	
	public static void main(String[] args) throws IOException {
		
		List<String> al = 		GetLinks.getIFs("0000000000000004", "0000000000000007");
		for(String s:al) {
			System.out.println(s);
		}
	/*	//1.生成邻接矩阵
		//String[] sArr={};
		FileUtil fu = new FileUtil();
		List<String> dpid = fu.fileReaderToListLine("C:\\apache-jmeter-4.0\\bin\\dpid.txt");
		String[] sArr = fu.changeListToArray(dpid);
		//String[] sArr = {"0000000000000001","0000000000000002","0000000000000003","0000000000000004","0000000000000005","0000000000000006","0000000000000007"}; 
		
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
		String[][] NearSwitch = new String[al.size()][al.size()];
	

		for(String ss:al) {
			System.out.println(ss);
		}
		//(1) 发请求 获得交换机信息
		//File oldJtl = new File("C:\\apache-jmeter-4.0\\bin\\getTopo.jtl");
		LinkOfSwitch los = new LinkOfSwitch();
		
											//这是一开始成功的，为了尝试进行二维数组的生成，故隐藏了这一部分
											for(int i=0;i<al.size();i++) {
												los.writeJmx(al.get(i));              //0000000000000001
												los.writeBatAndRun(al.get(i));
												String response = los.readJtl(al.get(i));
												List<String> alStr = new ArrayList<String>();	
												alStr = los.getLinkList(response);
												LinkClass[] lc = new LinkClass[alStr.size()];			
												for(int j=0;j<alStr.size();j++) {
													lc[j] = new LinkClass(alStr.get(j));
													System.out.println(alStr.get(j));
													NearSwitch[i][j] =
															lc[j].getDstSwitch();
													//System.out.println(lc[j]);
												}
											}
		
		//生成二维LinkClass数组 
		LinkClass[][] lcArr = new LinkClass[al.size()][];
		for(int i=0;i<al.size();i++) {
			los.writeJmx(al.get(i));              //0000000000000001
			los.writeBatAndRun(al.get(i));
			String response = los.readJtl(al.get(i));
			List<String> alStr = new ArrayList<String>();	
			alStr = los.getLinkList(response);
			lcArr[i]  = new LinkClass[alStr.size()];
			//LinkClass[] lc = new LinkClass[alStr.size()];			
			for(int j=0;j<alStr.size();j++) {
				//lc[j] = new LinkClass(alStr.get(j));
				lcArr[i][j] = new LinkClass(alStr.get(j),true);
				//System.out.println(alStr.get(j));
				NearSwitch[i][j] =
						lcArr[i][j].getDstSwitch();
				//System.out.println(lc[j]);
			}
		}

		for(int i=0;i<lcArr.length;i++) {
			for(int j=0;j<lcArr[i].length;j++) {
												System.out.println(lcArr[i][j].getDstPort());
												System.out.println(lcArr[i][j].getDstSwitch());
												System.out.println(lcArr[i][j].getSrcPort());
				System.out.println("dstName"+lcArr[i][j].getDstPName());
				System.out.println("srcName"+lcArr[i][j].getSrcPName());
			}
		}
		
										for(int i=0;i<NearSwitch.length;i++) {
											for(int j=0;j<NearSwitch[i].length;j++) {
												//if(NearSwitch[i][j]!=null) {
													System.out.print(NearSwitch[i][j]+"");
												//}
												//else {
												//	continue;
												//}
											}
											System.out.println();
										}
		//2.生成邻接节点矩阵
		int[][] NearDist= new int[NearSwitch.length][NearSwitch.length];
										for(int i=0;i<NearSwitch.length;i++) {
											for(int j=0;j<NearSwitch.length;j++) {
												if(i==j) {
													NearDist[i][j] = 0;
												}
												else if(NearSwitch[i][j] != null) {
													NearDist[i][j] = 1;
												}
												else {
													NearDist[i][j] = 1000;
												}
											}
										}
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
					System.out.print( "   "+   NearDist[i][j]);
			}
			System.out.println();
		}
		
		int vstart = 1;
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
			System.out.println(s);
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
		
		

		
									for(String s: vpot) {//打印节点队列
										System.out.print  (s  +"  ");
									}
									System.out.println("  ");
									
									for(String s:sArr) {//打印交换机队列
										System.out.print(s+ "    ");
									}
									
		for(String s:LinkDes) {//打印链路队列
			System.out.println(s);
		}
		
									//#根据其实节点寻找交换机
									String sta = "0000000000000001";
		String end = "0000000000000007";
		String sta = "v1";

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
		首先获得当前交换机，然后和sArr字符串比对，获得对应的序号。根据序号找到lcArr中下一交换机所对应的网卡名
		 *  
		 * 
		String[] routes = route.split("->");
		List<String> routeIf = new ArrayList<>();
		
		System.out.println("开始打印");
		
		for(int i=0;i<routes.length-1;i++) {
			for(int j=0;j<sArr.length;j++) {
				if(sArr[j].equals(routes[i])) {
					 for(int k=0;k<lcArr[j].length;k++) {
						 if((lcArr[j][k].getDstSwitch()).equals(routes[i+1])) {
							 routeIf.add(lcArr[j][k].getDstPort());
							 routeIf.add(lcArr[j][k].getSrcPort());
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
		for(String s:routeIf) {
			System.out.println(s);
		}*/
	}


}
