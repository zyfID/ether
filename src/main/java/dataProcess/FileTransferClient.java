package dataProcess;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;


public class FileTransferClient extends Socket {
    private static final String SERVER_IP = "10.20.216.223"; // 鏈嶅姟绔疘P  
    private static final int SERVER_PORT = 8899; // 鏈嶅姟绔鍙�  
  
    private Socket client;  
  
    private FileInputStream fis;  
  
    private DataOutputStream dos;  
  
    /** 
     * 鏋勯�犲嚱鏁�<br/> 
     * 涓庢湇鍔″櫒寤虹珛杩炴帴 
     * @throws Exception 
     */  
    public FileTransferClient() throws Exception {  
        super(SERVER_IP, SERVER_PORT);  
        this.client = this;  
        System.out.println("Cliect[port:" + client.getLocalPort() + "] 鎴愬姛杩炴帴鏈嶅姟绔�");  
    }  
  
    /** 
     * 鍚戞湇鍔＄浼犺緭鏂囦欢 
     * @throws Exception 
     */  
    public void sendFile() throws Exception {  
        try {  
           // File file = new File("/home/zero/Experiment/a.txt");  
        	File file = new File("/home/zero/Experiment/type.txt");
            if(file.exists()) {  
                fis = new FileInputStream(file);  
                dos = new DataOutputStream(client.getOutputStream());  
  
                // 鏂囦欢鍚嶅拰闀垮害  
                dos.writeUTF(file.getName());  
                dos.flush();  
                dos.writeLong(file.length());  
                dos.flush();  
  
                // 寮�濮嬩紶杈撴枃浠�  
                System.out.println("======== 寮�濮嬩紶杈撴枃浠� ========");  
                byte[] bytes = new byte[1024];  
                int length = 0;  
                long progress = 0;  
                while((length = fis.read(bytes, 0, bytes.length)) != -1) {  
                    dos.write(bytes, 0, length);  
                    dos.flush();  
                    progress += length;  
                    System.out.print("| " + (100*progress/file.length()) + "% |");  
                }  
                System.out.println();  
                System.out.println("======== 鏂囦欢浼犺緭鎴愬姛 ========");  
                file.delete();
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if(fis != null)  
                fis.close();  
            if(dos != null)  
                dos.close();  
            client.close();  
        }  
    }  
  
    /** 
     * 鍏ュ彛 
     * @param args 
     */  
    public static void main(String[] args) {  
        try {  
            FileTransferClient client = new FileTransferClient(); // 鍚姩瀹㈡埛绔繛鎺�  
            client.sendFile(); // 浼犺緭鏂囦欢  
            
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
}
