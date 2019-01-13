package org.apache.jmeter.visualizers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dijkstra {
	 
    private static int N = 1000;
  private static int[][] Graph = {
            { 0, 1, 5, N, N, N, N, N, N },
            { 1, 0, 3, 7, 5, N, N, N, N },
            { 5, 3, 0, N, 1, 7, N, N, N },
            { N, 7, N, 0, 2, N, 3, N, N },
            { N, 5, 1, 2, 0, 3, 6, 9, N },
            { N, N, 7, N, 3, 0, N, 5, N },
            { N, N, N, 3, 6, N, 0, 2, 7 },
            { N, N, N, N, 9, 5, 2, 0, 4 },
            { N, N, N, N, N, N, 7, 4, 0 } };
    
/*    private static int[][] Graph = {
            { 0, 1, 1, N, N, N, N },
            { 1, 0, N, 1, 1, N, N },
            { 1, N, 0, N, N, 1, 1},
            { N, 1, N, 0, N, N, N },
            { N, 1, N, N, 0, N, N },
            { N, N, 1, N, N, 0, N},
            { N, N, 1, N, N, N, 0 }
        };*/
    public static void main(String[] args) {
    	String[][] str = {{"v0","v0"},{"v1","v1"},{"v1","v2"},{"v2","v4"},{"v4","v3"},{"v4","v5"},{"v3","v6"},{"v6","v7"},{"v7","v8"}};
        int vs = 3;
        String vss = "v"+vs;
    	find(vss,dijkstra(vs, Graph));
    }
 
    /**
     * Dijkstra最短路径。
     * 即图中"节点vs"到其它各个节点的最短路径。
     * @param vs 起始节点
     * @param Graph 图
     */
    public static String[] dijkstra(int vs, int[][] Graph) {
        int NUM = Graph[0].length;
        
        // 前驱节点数组
        int[] prenode = new int[NUM];
        
        // 最短距离数组
        int[] mindist = new int[NUM];
        // 该节点是否已经找到最短路径
        boolean[] find = new boolean[NUM];
         
        int vnear = 0;
         
        for (int i = 0; i < mindist.length; i++) {
            prenode[i] = i;
            mindist[i] = Graph[vs][i];
            find[i] = false;
        }
 
        find[vs] = true;
 
        for (int v = 1; v < Graph.length; v++) {
 
            // 每次循环求得距离vs最近的节点vnear和最短距离min
            int min = N;
            for (int j = 0; j < Graph.length; j++) {
                if (!find[j] && mindist[j] < min) {
                    min = mindist[j];
                    vnear = j;
                }
            }
            find[vnear] = true;
 
            // 根据vnear修正vs到其他所有节点的前驱节点及距离
            for (int k = 0; k < Graph.length; k++) {
                if (!find[k] && (min + Graph[vnear][k]) < mindist[k]) {
                    prenode[k] = vnear;
                    mindist[k] = min + Graph[vnear][k];
                }
            }
            
        }
         
        for (int i = 0; i < NUM; i++) {
         //  System.out.println("v" + vs + "...v" + prenode[i] + "->v" + i + ", s=" + mindist[i]);
        }
        String[] str = new String[prenode.length] ;
        String[] result = new String[prenode.length] ;
        List<String> al = new ArrayList<>();
        
        for(int i = 0; i < NUM; i++) {
        	//str[i] = "v"+prenode[i] + "->v" + i;
        	str[i] = "s=" + mindist[i] +  "av" + prenode[i] + "->v" + i   ;
        	al.add(str[i]);
        }
        Collections.sort(al);

        for(int i=0;i<al.size();i++) {
        	String sss = al.get(i);
        	if(sss.contains("a")) {
        		String[] sp = sss.split("a");
        		
        		result[i] = sp[1];
        	}
        	else {
        		break;
        	}
        }
        for(int j=0;j<result.length;j++) {
        	//System.out.println(result[j]);
        }
        return result;
    }
    
    
    //根据最终节点和前一节点，来获得完整的链路
    /*
     * 
     * 如果最终节点的前一节点未出现在队列中，则直接与起始节点相连，否则就找到末尾是该终点节点的起始节点的 字符串，并将该节点串进去
     * 
     * */
    public static List<String> find (String vs ,String[] str) {
    	List<String> al = new ArrayList<>();
    	boolean isContain = false;
    	for(int i=0;i<str.length;i++) {
    		String[] sp = str[i].split("->");
    		for(int j=0;j<al.size();j++) {	
    			if((al.get(j).contains(sp[0]))) {
    				isContain = true;
    			}
    		}
    		if(isContain == false) {
    			al.add(new String(vs+"->"+sp[1]));
    		}
    		else {
    			for(int k=0;k<al.size();k++) {
					if(al.get(k).endsWith(sp[0])) {
						al.add(al.get(k)+"->"+sp[1]);
					}
				}
    		}
    		
    		isContain = false;
    	}
      	for(String s:al) {
    		//System.out.println(s);
    	}    		
    	return al;   	
    }
}