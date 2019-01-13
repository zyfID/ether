package Util;


//用一个类封装流表项信息
public class FlowentryClass {
	private final static int split = 11;
	public static final int dimension = 10;
	private String actions;
	private String idle_timeout;
	private String cookie;
	private String packet_count;
	private String hard_timeout;
	private String byte_count;
	private String duration_nsec;
	private String priority;
	private String duration_sec;
	private String table_id;
	private String match;
	
	public FlowentryClass() {
		
	}
	
	public FlowentryClass(String res) {
		String[] sArray =  new String[dimension];
		sArray = res.split(",",split);
		actions = sArray[0].substring(sArray[0].indexOf("["));
		
		for(int i=0;i<sArray.length;i++) {
			
			if(sArray[i].contains("idle_timeout")) {
				String[] subS = sArray[i].split(":");
				idle_timeout = subS[1];
			
			}
			if(sArray[i].contains("cookie")) {
				String[] subS = sArray[i].split(":");
				cookie = subS[1];
			}
			if(sArray[i].contains("packet_count")) {
				String[] subS = sArray[i].split(":");
				packet_count = subS[1];
			}
			if(sArray[i].contains("hard_timeout")) {
				String[] subS = sArray[i].split(":");
				hard_timeout = subS[1];
			}
			if(sArray[i].contains("byte_count")) {
				String[] subS = sArray[i].split(":");
				byte_count = subS[1];
			}
			if(sArray[i].contains("duration_nsec")) {
				String[] subS = sArray[i].split(":");
				duration_nsec = subS[1];
			}
			if(sArray[i].contains("priority")) {
				String[] subS = sArray[i].split(":");
				priority = subS[1];
			}
			if(sArray[i].contains("duration_sec")) {
				String[] subS = sArray[i].split(":");
				duration_sec = subS[1];
			}
			if(sArray[i].contains("table_id")) {
				String[] subS = sArray[i].split(":");
				table_id = subS[1];
			}
			
			if(sArray[i].contains("match")) {
				String[] subS = sArray[i].split(":",2);
				match = subS[1];
/*				try {
					match = match.substring((match.indexOf("{"))+1);
					match =match.substring(0, match.indexOf("}"));
				}catch(StringIndexOutOfBoundsException e){
					System.out.println("�ַ��������쳣");
				}*/
			}
			
		}
	}
	
	public FlowentryClass(String actions, String idle_timeout, String cookie, String packet_count, String hard_timeout,
			String byte_count, String duration_nsec, String priority, String duration_sec, String table_id,
			String match) {
		super();
		this.actions = actions;
		this.idle_timeout = idle_timeout;
		this.cookie = cookie;
		this.packet_count = packet_count;
		this.hard_timeout = hard_timeout;
		this.byte_count = byte_count;
		this.duration_nsec = duration_nsec;
		this.priority = priority;
		this.duration_sec = duration_sec;
		this.table_id = table_id;
		this.match = match;
	}

	public String getActions() {
		return actions;
	}

	public void setActions(String actions) {
		this.actions = actions;
	}

	public String getIdle_timeout() {
		return idle_timeout;
	}

	public void setIdle_timeout(String idle_timeout) {
		this.idle_timeout = idle_timeout;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public String getPacket_count() {
		return packet_count;
	}

	public void setPacket_count(String packet_count) {
		this.packet_count = packet_count;
	}

	public String getHard_timeout() {
		return hard_timeout;
	}

	public void setHard_timeout(String hard_timeout) {
		this.hard_timeout = hard_timeout;
	}

	public String getByte_count() {
		return byte_count;
	}

	public void setByte_count(String byte_count) {
		this.byte_count = byte_count;
	}

	public String getDuration_nsec() {
		return duration_nsec;
	}

	public void setDuration_nsec(String duration_nsec) {
		this.duration_nsec = duration_nsec;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getDuration_sec() {
		return duration_sec;
	}

	public void setDuration_sec(String duration_sec) {
		this.duration_sec = duration_sec;
	}

	public String getTable_id() {
		return table_id;
	}

	public void setTable_id(String table_id) {
		this.table_id = table_id;
	}

	public String getMatch() {
		return match;
	}

	public void setMatch(String match) {
		this.match = match;
	}
	
	
	public static void main(String[] args) {
		FlowentryClass fc = new FlowentryClass("[\"OUTPUT:NORMAL\"], \"idle_timeout\": 0, \"cookie\": 0, \"packet_count\": 0, \"hard_timeout\": 0, \"byte_count\": 0, \"duration_nsec\": 744000000, \"priority\": 32768, \"duration_sec\": 28, \"table_id\": 0, \"match\": {}}]} ");
		System.out.println(fc.actions);
		System.out.println("idle_time="+fc.getByte_count());
		System.out.println("priority="+fc.getPriority());
		System.out.println("duration="+fc.getDuration_nsec());
		System.out.println("cookie="+fc.getCookie());
		System.out.println("packet="+fc.getPacket_count());
		System.out.println("hard_timeout="+fc.getHard_timeout());
		System.out.println("byte_count="+fc.byte_count);
		System.out.println("table="+fc.getTable_id());
		System.out.println("match="+fc.getMatch());
	}
}
