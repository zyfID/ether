package Frame.second;

import java.util.List;

import javax.swing.JFrame;



/**
 * @author jusang
 *　 批量配置界面的JFrame类
 */
public class BatchFrame extends JFrame {
	
	public BatchFrame(){
		
	}
	
	public BatchFrame(List<String> l) {
		this.setBounds(150, 150, 600, 700);
		this.setVisible(true);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.add(new SubBatchPanel(l));
	}
	
	
	
}
