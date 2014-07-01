package network_connections;

import connections.commands_generate;

public class one_call_others {

	
	public static void main(String[] args){
		
		ip_router_multiconnect b = new ip_router_multiconnect();
		b.initial_devices();
		
		b.initial_log_information();
		b.read_infomation_from_file("list.cfg");
		
		simple_network_connect a = new simple_network_connect();
		
		String source_ip_string = b.get_one_random_device();//get a random source ip address
		
		System.out.println("source ip:" + source_ip_string);
		
		String target_ip_string;
		
		target_ip_string = b.get_one_random_device();
		System.out.println("target ip:" + target_ip_string);
		
		//while( target_ip_string.equals("192.168.202.213")){
		//	target_ip_string = b.get_one_random_device();
		//}
		
		while( source_ip_string.equals(target_ip_string)){
			target_ip_string = b.get_one_random_device();
			System.out.println("target ip:" + target_ip_string);
		}
		
		
		a.login_by_session(source_ip_string, b.get_loginformation().get(source_ip_string).hostaddr, b.get_loginformation().get(source_ip_string).keywords);
		
		String xcom;
		String returncallid;
		
		xcom = (new com_generate_multiconnect()).dial_commamd_generate(target_ip_string);
		
		a.send_commands_by_channel(xcom);
		returncallid = a.sysout("dial", 5);
		
		simple_network_connect bbb = new simple_network_connect();
		bbb.login_by_session(target_ip_string, b.get_loginformation().get(target_ip_string).hostaddr, b.get_loginformation().get(target_ip_string).keywords);
		xcom = (new com_generate_multiconnect()).call_accept_command_generate("");
		bbb.send_commands_by_channel(xcom);
		bbb.sysout("", 5);
		
		
		b.establish_new_connections(source_ip_string, target_ip_string, returncallid);
		
		
		while( !b.onesource_connect_alltargets(source_ip_string)){
		
			do{
			
				System.out.println("source ip:" + source_ip_string);
			
				target_ip_string = b.get_one_random_device();
				System.out.println("target ip:" + target_ip_string);
			
				while( source_ip_string.equals(target_ip_string)){
					target_ip_string = b.get_one_random_device();
					System.out.println("target ip:" + target_ip_string);
				}
			
			
			}while(b.isconnecting(source_ip_string, target_ip_string));
		
			simple_network_connect aa = new simple_network_connect();
			aa.login_by_session(source_ip_string, b.get_loginformation().get(source_ip_string).hostaddr, b.get_loginformation().get(source_ip_string).keywords);
			xcom = (new com_generate_multiconnect()).dial_commamd_generate(target_ip_string);
			aa.send_commands_by_channel(xcom);
			returncallid = aa.sysout("dial", 5);
		
			simple_network_connect bb = new simple_network_connect();
			bb.login_by_session(target_ip_string, b.get_loginformation().get(target_ip_string).hostaddr, b.get_loginformation().get(target_ip_string).keywords);
			xcom = (new com_generate_multiconnect()).call_accept_command_generate("");
			bb.send_commands_by_channel(xcom);
			bb.sysout("", 5);
		
			b.establish_new_connections(source_ip_string, target_ip_string, returncallid);
		
		//System.out.println("#######Test000000:" + b.onesource_connect_alltargets(source_ip_string));
		}
		
		do{
			
			if( b.noconnectionleft()){
				break;
			}
			
			source_ip_string = b.get_one_random_device();
			
			System.out.println("source ip:" + source_ip_string);
			
			target_ip_string = b.get_one_random_device();
			
			while( source_ip_string.equals(target_ip_string)){
				target_ip_string = b.get_one_random_device();
			}
		
			
			
		
		}while(b.isconnecting(source_ip_string, target_ip_string));
		
		//System.out.println("no left connecttion : " + b.noconnectionleft());
		
		String dis_id = b.get_callid();
		
		while( !dis_id.equals("all disconnected")){
			
			String[] ars = dis_id.split("#");
			
			simple_network_connect aaa = new simple_network_connect();
			aaa.login_by_session(ars[1], b.get_loginformation().get(ars[1]).hostaddr, b.get_loginformation().get(ars[1]).keywords);
			
			aaa.send_commands_by_channel(new commands_generate().disconnect_command_generate(ars[0]));
			
			b.disconnect_current_connections(ars[1], ars[2]);
			
			aaa.sysout();
			
			dis_id = b.get_callid();
		}
		
		
		//System.out.println("#######Test1111111:" + b.onesource_connect_alltargets(target_ip_string));
		System.out.println("###END");
		
	}
}
