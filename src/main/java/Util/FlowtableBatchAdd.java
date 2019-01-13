package Util;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

import Demo.Utils;
import blockchainUtil.ChangeStringToHex;

//批量添加
public class FlowtableBatchAdd {
	private static SplitResponesByDpid srb = new SplitResponesByDpid();
	
	public static void writeFile(String str) {
		File temFile = new File("E:\\apache-jmeter-4.0\\bin\\FlowentryAddTem.jmx");
		File newFile = new File("E:\\apache-jmeter-4.0\\bin\\FlowentryAddNewFile.jmx");
		if(newFile.exists()) {
			newFile.delete();
		}
		try {
			String temStr = srb.readFile(temFile);
			temStr = temStr.replaceAll("asdfasdfasdfasdf", str);
			srb.writeToFile(temStr, newFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//下发流表项，区块链无关
	public static void runBatchAddBat() {
		File temBat = new File("E:\\apache-jmeter-4.0\\bin\\bat\\FlowentryAdd.bat");
		String s ="cmd /c start /b " + temBat.getPath();
		new BatUtil(s);	
	}

	//区块链有关下发流表项
	public static void runBatchAddBat(String policy) {
		//将policy转换为16进制
		policy = "0x"+ChangeStringToHex.toHexString(policy);
		
		//运行之前先通过区块链进行交易的存储
		//读取本地文件中的账号和密码
		//构建Transaction对象，from, null, gasPrice, gasLimist, to[0], bi, data
		File accountFile = new File("E:/apache-jmeter-4.0/bin/db/srcAccount.txt");
		File pwdFile = new File("E:/apache-jmeter-4.0/bin/db/srcPassword.txt");;
		//from
		String account = null;
		//password
		String pwd = null;
		try {
			account = srb.readFile(accountFile);
			//去掉最后一个回车符
			account = account.substring(0, account.length()-1);
			//pwd = srb.readFile(pwdFile);
			pwd="123456";
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//to
		String dstAccount="0x61cfda20566310a2ad8197a77e5c3098a453267a";
		//data = policy
		//金币数量
		BigInteger bi = new BigInteger("1000");;
		BigInteger gasLimist = new BigInteger("210000");
		BigInteger gasPrice = new BigInteger("2000000");
		org.web3j.protocol.core.methods.request.Transaction transaction = new 
				//org.web3j.protocol.core.methods.request.Transaction(account, null, gasPrice, gasLimist, dstAccount, bi, policy);
				org.web3j.protocol.core.methods.request.Transaction(dstAccount, null, gasPrice, gasLimist, dstAccount, bi, policy);
		//调试错误hex string has length 42, want 40 for common.Address
		//System.out.println(account);
		System.out.println(dstAccount);
		System.out.println(gasPrice);
		System.out.println(gasLimist);
		System.out.println(dstAccount);
		System.out.println(bi);
		System.out.println(policy);
		

		try {
			String res = Utils.sendTransaction(transaction, pwd);
			if(res.startsWith("0x")){
				//成功，即进行策略的配置
				File temBat = new File("E:\\apache-jmeter-4.0\\bin\\bat\\FlowentryAdd.bat");
				String s ="cmd /c start /b " + temBat.getPath();
				new BatUtil(s);	
			}
			else{
				//否则，打印具体的错误5
				System.out.println(res);

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
