package network_connections;

public class information_cell {

	boolean status;
	String callID;
	
	
	public information_cell(){
		status = false;
		callID = "";
	}
	
	
	public boolean get_status(){
		return status;
	}
	private void set_status(boolean b){
		status = b;
	}
	public void connect(){
		set_status(true);
	}
	public void disconnect(){
		set_status(false);
	}
	
	public String get_callID(){
		return callID;
	}
	public void set_callID(String callid){
		callID = callid;
	}
	
	
	
	public static void main(String[] args){
		
		information_cell c = new information_cell();
		
	}
}
