package network_connections;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;


/**
 * Using hashmap, only when searching the ip address's other information, will be effective.
 * 
 * */
public class ip_router_multiconnect {

	
	/*
	 * ip list(devices¡£¡£¡£)
	 * */
	class tuple_log{
		String hostaddr = null;
		String keywords = null;
		public tuple_log(String host, String keyword){
			hostaddr = host;
			keywords = keyword;
		}
	}
	
	private HashMap<String, tuple_log> log_information;
	
	private HashMap<String, HashMap<String, information_cell>> devices;
	
	public HashMap<String, HashMap<String, information_cell>> get_devices(){
		return devices;
	}
	public HashMap<String, tuple_log> get_loginformation(){
		return log_information;
	}
	public void set_devices(){
		
	}
	
	/*
	 * 
	 * */
	public void read_infomation_from_file(String filename){
		
		File file = new File(filename);
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			String line = "";
			
			while( ( line = br.readLine()) != null){
				String[] args = line.split("#");
				//0: ip address, 1:log name, 2:log keyword
				
				devices.put(args[0], new HashMap<String, information_cell>());
				
				if( args.length == 3){
					log_information.put(args[0], new tuple_log(args[1], args[2]));
				}
				else {
					log_information.put(args[0], new tuple_log(args[1], ""));
				}
				
				
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	public void initial_log_information(){
		log_information = null;
		log_information = new HashMap<String, tuple_log>();
	}
	public void construct_log_list(){
		log_information.put("192.168.202.213", new tuple_log("admin", "admin"));
		log_information.put("192.168.202.145", new tuple_log("admin", ""));
		log_information.put("192.168.203.168", new tuple_log("admin", ""));
		log_information.put("192.168.203.206", new tuple_log("admin", ""));
	}
	
	
	public void initial_devices(){
		devices = null;
		devices = new HashMap<String, HashMap<String, information_cell>>();
	}
	public boolean construct_ip_router(){//need the return infomation from commands
		
		devices.put("192.168.202.213", new HashMap<String, information_cell>());
		devices.put("192.168.202.145", new HashMap<String, information_cell>());
		//devices.put("192.168.203.168", new HashMap<String, information_cell>());
		devices.put("192.168.203.206", new HashMap<String, information_cell>());
		
		
		return true;
	}
	
	private Random rd1 = new Random();//##################for good random result!
	
	public String get_one_random_device(){
		
		//System.out.println("s1");
		
		int all_devices_num = devices.size();
		
		//System.out.println("s2 : " + all_devices_num);
		
		
		int p = rd1.nextInt(all_devices_num);
		
		//System.out.println("s3 : " + p);
		
		if( p < 0){
			p = p * ( -1 );
		}
		
		p = p % all_devices_num;
		
		//System.out.println("s4 : " + p);
		
		Set<String> keyset = devices.keySet();
		Iterator it = keyset.iterator();
		String targetip = null;
		while( p >= 0){
			targetip = (String)it.next();
			p --;
		}
		
		return targetip;
	}
	
	public void establish_new_connections(String source, String target, String callid){
		
		if( devices.get(source) == null){
			
			information_cell temp_cell = new information_cell();
			
			temp_cell.set_callID(callid);
			temp_cell.connect();
			
			HashMap<String, information_cell> temp_t = new HashMap<String, information_cell>();
			temp_t.put(target, temp_cell);
			
			devices.put(source, temp_t);
			
		}
		else if( devices.get(source).get(target) == null){
			
			information_cell temp_cell = new information_cell();
			
			temp_cell.set_callID(callid);
			temp_cell.connect();
			
			devices.get(source).put(target, temp_cell);
			
		}
		else {
			
			devices.get(source).get(target).set_callID(callid);
			devices.get(source).get(target).connect();
		}
		
	}
	public void disconnect_current_connections( String source, String target){
		
		devices.get(source).get(target).set_callID("");
		devices.get(source).get(target).disconnect();
		
	}
	
	public boolean isconnecting(String source, String target){//use private, some menber may can't be used directly!#####
		
		if( this.devices.get(source) == null){
			return false;
		}
		else if( this.devices.get(source).get(target) == null){
			return false;
		}
		else {
			
			if( this.devices.get(source).get(target).get_status()){//status true means connect
				return true;
			}
			else {
				return false;
			}
			
		}
		
	}
	public boolean noconnectionleft(){
		
		Set<String> available_address = this.devices.keySet();
		
		for( Iterator it = available_address.iterator(); it.hasNext();){
			String s = (String)it.next();
			
			for( Iterator it_in = available_address.iterator(); it_in.hasNext();){
				String s_in = (String)it_in.next();
				
				if( s.equals(s_in)){
				}
				else if( this.devices.get(s) == null){
					//System.out.println("###");
					return false;
				}
				else if( this.devices.get(s).get(s_in) == null){
					//System.out.println("!!!!!!");
					return false;
				}
				else {//########## because of the logic, must remove the same source and target!!!
					if( this.devices.get(s).get(s_in).get_status()){
					}
					else {
						//System.out.println("^^^^^^");
						return false;
					}
				}
				
			}
			
		}
		
		return true;
	}
	
	public boolean onesource_connect_alltargets(String sourceipaddress){
		
		Set<String> available_address = this.devices.keySet();
		
		for( Iterator it_in = available_address.iterator(); it_in.hasNext();){
			String targetipaddress = (String)it_in.next();
				
				if( sourceipaddress.equals(targetipaddress)){
				}
				else if( this.devices.get(sourceipaddress) == null){//that's situation needs consideration
					System.out.println("###");
					return false;
				}
				else if( this.devices.get(sourceipaddress).get(targetipaddress) == null){//that's situation needs consideration
					System.out.println("!!!!!!");
					return false;
				}
				else {//########## because of the logic, must remove the same source and target!!!
					if( this.devices.get(sourceipaddress).get(targetipaddress).get_status()){
					}
					else {
						System.out.println("^^^^^^");
						return false;
					}
				}
				
			}
			
		
		
		return true;
	}
	public boolean onetarget_connected_by_allsource(String targetipaddress){
		
		Set<String> available_address = this.devices.keySet();
		
		for( Iterator it_in = available_address.iterator(); it_in.hasNext();){
			String sourceipaddress = (String)it_in.next();
				
				if( sourceipaddress.equals(targetipaddress)){
				}
				else if( this.devices.get(sourceipaddress) == null){//that's situation needs consideration
					System.out.println("###");
					return false;
				}
				else if( this.devices.get(sourceipaddress).get(targetipaddress) == null){//that's situation needs consideration
					System.out.println("!!!!!!");
					return false;
				}
				else {//########## because of the logic, must remove the same source and target!!!
					if( this.devices.get(sourceipaddress).get(targetipaddress).get_status()){
					}
					else {
						System.out.println("^^^^^^");
						return false;
					}
				}
				
			}
			
		
		
		return true;
	}
	
	
	public boolean allbuiltconnections_disconnected(){//make no meaning as like noconnection left!
		
		Set<String> available_address = this.devices.keySet();
		
		for( Iterator it = available_address.iterator(); it.hasNext();){
			String s = (String)it.next();
			
			for( Iterator it_in = available_address.iterator(); it_in.hasNext();){
				String s_in = (String)it_in.next();
				
				if( s.equals(s_in)){
				}
				else if( this.devices.get(s) == null){
					//System.out.println("###");
					//return false;
				}
				else if( this.devices.get(s).get(s_in) == null){
					//System.out.println("!!!!!!");
					//return false;
				}
				else {//########## because of the logic, must remove the same source and target!!!
					if( this.devices.get(s).get(s_in).get_status()){
						return false;
					}
					else {
						//System.out.println("^^^^^^");
						//return false;
					}
				}
				
			}
			
		}
		
		return true;
	}
	
	
	
	public String get_callid(){
		
		Set<String> available_address = this.devices.keySet();
		
		for( Iterator it = available_address.iterator(); it.hasNext();){
			String s = (String)it.next();
			
			for( Iterator it_in = available_address.iterator(); it_in.hasNext();){
				String s_in = (String)it_in.next();
				
				if( isconnecting(s,s_in)){
					/*
					String ss = this.devices.get(s).get(s_in).get_callID();
					this.disconnect_current_connections(s, s_in);
					System.out.println("###" + this.devices.get(s).get(s_in).get_callID() + "," + this.devices.get(s).get(s_in).get_status());
					return ss;
					*/
					return this.devices.get(s).get(s_in).get_callID() + "#" + s + "#" + s_in;
				}
				else if( s.equals(s_in)){
				}
				else if( this.devices.get(s) == null){
				}
				else if( this.devices.get(s).get(s_in) == null){
				}
				else {
					if( this.devices.get(s).get(s_in).get_status()){
						/*
						String ss = this.devices.get(s).get(s_in).get_callID();
						this.disconnect_current_connections(s, s_in);
						System.out.println("###" + this.devices.get(s).get(s_in).get_callID() + "," + this.devices.get(s).get(s_in).get_status());
						return ss;
						*/
						return this.devices.get(s).get(s_in).get_callID() + "#" + s + "#" + s_in;
					}
					else {
					}
				}
			}
		}
		return "all disconnected";
	}
	public String get_callid(String source, String target){
		if( isconnecting(source, target)){
			return this.devices.get(source).get(target).get_callID();
		}
		else if( source.equals(target)){
		}
		else{			
		}
		return "disconnected";
	}
	
	
	
	
	public HashMap<String, Integer> get_one_device_with_some_conditions_information(){
		return null;
	}
	public HashMap<String, Integer> get_one_random_device_information(){
		
		return null;
	}
	
	public boolean add_one_device(String key_device, String key_para, information_cell para){
		
		HashMap<String, information_cell> temp = new HashMap<String, information_cell>();
		temp.put(key_para, para);
		devices.put(key_device, temp);
		
		return true;
	}
	public boolean remove_one_device(String key_device){

		
		return true;
	}
	public boolean modify_one_device(){
		
		
		return true;
	}
	
	
	
	
	
	
	public static void main(String[] args){
		
		
		/*
		ip_router_multiconnect a = new ip_router_multiconnect();
		a.initial_devices();
		a.construct_ip_router();
		
		String s = a.get_one_random_device();
		
		System.out.println(s);
		*/
		
		ip_router_multiconnect a = new ip_router_multiconnect();
		a.initial_devices();
		a.construct_ip_router();
		
		a.establish_new_connections("1", "2", "0");
		
		
		
		System.out.println(a.get_devices().get("1").get("2").get_status() + ", " + a.get_devices().get("1").get("2").get_callID());
		
		a.disconnect_current_connections("1", "2");
		
		System.out.println(a.get_devices().get("1").get("2").get_status() + ", " + a.get_devices().get("1").get("2").get_callID());

		a.initial_devices();
		a.initial_log_information();
		
		a.read_infomation_from_file("list.cfg");
		
		
	}
}
