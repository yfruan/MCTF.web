package network.protocol;

import java.io.Serializable;

/**
 * Message payload
 * @author Yifan Ruan (ry222ad@student.lnu.se)
 */
public class Payload implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int flag;    
	private Object data;
		
	public Payload(int flage,Object data){
		this.flag=flage;
		this.data=data;
	}
	
	public int getFlag() {
		return flag;
	}
	
	public void setFlag(int flag) {
		this.flag = flag;
	}

	public Object getData() {
		return data;
	}
}
