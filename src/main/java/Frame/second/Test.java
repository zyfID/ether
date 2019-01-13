package Frame.second;

public class Test {
	public static void main(String[] args) {
		String switchName ="0000009027f0358a";
	   	long z;
	   	z = Long.parseLong(switchName, 16);
    	//z = Integer.parseInt(switchName,16);
    	switchName = Long.toString(z);
    	System.err.println(switchName);
	}
}
