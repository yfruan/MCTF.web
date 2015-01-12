package protocol;

import java.io.Serializable;

/**
 * 
 * @author Yifan Ruan (ry222ad@student.lnu.se)
 */
public class ConnectPayload implements Serializable{
	private static final long serialVersionUID = 1L;
	 
	private int flag;                   
	private int verifyNum;       // for security purpose          
	
	public ConnectPayload(int flag,int verifyNum){
		this.flag=flag;
		this.verifyNum=verifyNum;
	}

	public int getFlag() {
		return flag;
	}

	public int getVerifyNum() {
		return verifyNum;
	}

}
