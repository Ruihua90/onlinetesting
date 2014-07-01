package network_connections;


public class com_generate_multiconnect {

	public String commands = "";
	
	public String begin = "xcom ";//and so on¡£¡£¡£
	
	public String ends = "\n";
	
	
	public String generate(ip_router_multiconnect a){
		
		commands = begin + "dial Number: " + a.get_one_random_device() + ends;
		
		
		return commands;
	}
	
	public String dial_commamd_generate(String a){
		
		commands = begin + "dial Number: " + a + ends;
		
		
		return commands;
	}
	
	public String disconnect_command_generate(String callid){
		commands = begin + "call disconnect " + callid + ends;
		return commands;
	}
	
	public String call_accept_command_generate(String callid){
		commands = begin + "call accept " + callid + ends;
		return commands;
	}
	
	public String call_join_command_generate(){
		commands = begin + "call join" + ends;
		return commands;
	}
	
	public String xstatus_callid_command_generate(){
		commands = "xsta call" + ends;
		return commands;
	}
	
	public static void main(){
		
	}
}
