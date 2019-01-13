package blockchainUtil;

import org.junit.Test;

public class ChangeStringToHex {
	
	//英文字符串转十六进制
	public static String toHexString(String s) 
	{ 
		String str=""; 
		for (int i=0;i<s.length();i++) 
		{ 
			int ch = (int)s.charAt(i); 
			String s4 = Integer.toHexString(ch); 
			str = str + s4; 
		} 
		return str; 
	} 
	
	 // 转化十六进制编码为字符串
    public static String toStringHex2(String s) {
       byte[] baKeyword = new byte[s.length() / 2];
       for (int i = 0; i < baKeyword.length; i++) {
        try {
         baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(
           i * 2, i * 2 + 2), 16));
        } catch (Exception e) {
         e.printStackTrace();
        }
       }
       try {
        s = new String(baKeyword, "utf-8");// UTF-16le:Not
       } catch (Exception e1) {
        e1.printStackTrace();
       }
       return s;
    }

	
    @Test
    public void fun(){
    	System.out.println(ChangeStringToHex.toHexString("ss"));
    }
	
	
}
