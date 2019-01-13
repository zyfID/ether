package blockchainUtil;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import Demo.Utils;

//发送交易工具类
public class SendTransaction {
	
	//发送交易函数
	public static void transaction(String from,String to,BigInteger gasPrice,BigInteger gasLimist,BigInteger bi,String data,String password){
		org.web3j.protocol.core.methods.request.Transaction transaction = new 
				org.web3j.protocol.core.methods.request.Transaction(from, null, gasPrice, gasLimist, to, bi, data);
		try {
			String flag =  Utils.sendTransaction(transaction,password);
			//jLabel14.setText(flag);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
