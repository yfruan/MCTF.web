package protocol;

import java.io.Serializable;

public class ConnectPayload implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int flag;
	private int verifyNum;
	
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
