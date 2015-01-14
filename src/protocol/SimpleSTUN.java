package protocol;

import java.io.Serializable;

public class SimpleSTUN implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int flag;   // 
	private Object content=null;   // extra content to be sent
	
	public SimpleSTUN(int flag, Object content){
		this.flag=flag;
		this.content=content;
	}
	
	public int getFlag() {
		return flag;
	}
	
	public Object getContent(){
		return content;
	}
}
