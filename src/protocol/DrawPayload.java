package protocol;

import java.io.Serializable;

public class DrawPayload implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int flag;
	private String capacityClass;
	private Object capacity ;
	
	public DrawPayload(int flage, String capacityClass, Object capacity){
		this.flag=flage;
		this.capacityClass=capacityClass;
		this.capacity=capacity;
	}
	
	public int getFlag() {
		return flag;
	}
	
	public void setFlag(int flag) {
		this.flag = flag;
	}

	public Object getCapacity() {
		return capacity;
	}

	public String getCapacityClass() {
		return capacityClass;
	}
	
}
