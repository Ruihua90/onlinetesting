package network_connections;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

import connections.commands_generate;
import connections.ip_router;
import connections.testexamples.shell_connect.MyUserInfo;





/**
 * thinking:
 * 1.Could I istall the ssh protocol on the system?
 * 2.Does it command just some file system with the command implementation files?
 * 
 * */

public class simple_network_connect {

	
	JSch jsch;
	/*
	 * connecting information
	 * */
	String host;//host location
	String user;//user name
	String password;//user password
	int port;//23 or 22 ?Is it depending on the protocol. So, if useing port 22, then actually connection is ssh?
	//here also need userinfo
	UserInfo ui;
	
	
	/*
	 * real steps of connecting:
	 * 1. session connect --- the user logins
	 * 2. channel connect --- send command & get system out information
	 * hint: 
	 * i. command need "\n" in the command to emulate the shell operations
	 * ii. login information can be sys out
	 * iii. sys out information is in the func channel.getInputStream
	 * iv. channel's other methods must be able to be digged out many other functions!
	 * */
	Session session;
	Channel channel;
	
	//ip router---to select --- in theory, it can be got by the command "show ip router"
	/*
	 * the structure of ip router: hashmap with some other information iterms
	 * initial the ip router
	 * update the ip router
	 * use the ip router to provide ip address in the command dial.(It needs restructure.)
	 * 
	 * */
	
	
	String command;
	InputStream in;//command results
	
	
	public simple_network_connect(){
		
		jsch = new JSch();
		command = "";
	}
	
	public boolean login_by_session(){
		
		host = "192.168.203.168";//c20: 192.168.202.145; sx20: 192.168.203.168; c60: 192.168.203.206;
		user = "admin";
		password = "";
		port = 22;
		
		ui = new MyUserInfo(){
			public void showMessage(String message){
		          
		        }
		        public boolean promptYesNo(String message){
		        	
		        	return true;
		        }
		};
		
		try {
			session = jsch.getSession(user, host, port);
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		session.setPassword(password);
		session.setUserInfo(ui);
		
		try {
			session.connect(30000);
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	
public boolean login_by_session(String hostaddr, String username, String passwordstr){
		
		host = hostaddr;//c20: 192.168.202.145; sx20: 192.168.203.168; c60: 192.168.203.206;
		user = username;
		password = passwordstr;
		port = 22;
		
		ui = new MyUserInfo(){
			public void showMessage(String message){
		          
		        }
		        public boolean promptYesNo(String message){
		        	
		        	return true;
		        }
		};
		
		try {
			session = jsch.getSession(user, host, port);
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		session.setPassword(password);
		session.setUserInfo(ui);
		
		try {
			session.connect(30000);
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	
	public void send_commands_by_channel(){
		
		try {
			
			channel = session.openChannel("shell");
			
			
			command = "xcom dial\n";
			InputStream i;
			i = new ByteArrayInputStream(command.getBytes());
			channel.setInputStream(i);
			
			in=channel.getInputStream();//??
			
			channel.connect(30000);
			
			
			
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void send_commands_by_channel(String xcom){
		
		try {
			
			channel = session.openChannel("shell");
			
			
			command = xcom;
			InputStream i;
			i = new ByteArrayInputStream(command.getBytes());
			channel.setInputStream(i);
			
			in=channel.getInputStream();//??
			
			channel.connect(30000);
			
			
			
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void sysout(){
		
		byte[] tmp=new byte[1024];
		
		int count = 5;
		
		String s = null; 
		
		try {
		
	      while(true){
	    	  if( count < 0) break;
	        
			while(in.available()>0){
				int ii=in.read(tmp, 0, 1024);
				if(ii<0)break;
				s = new String(tmp, 0, ii);
				System.out.print(s);
			}
			
	        if(channel.isClosed()){
	        	System.out.println("exit-status: "+ channel.getExitStatus());
	        	break;
	        }
	        
	        try{Thread.sleep(1000);}catch(Exception ee){}
	        
	        count --;
	        
	      }
	      
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
	}
	
	public String selectID( String outinfo){
		
		String re = null;
		
		/*
		String[] args = outinfo.split("\\*r");
		//System.out.println("r2: " + args[2]);
		
		String[] args1 = args[2].split("\n");
		//System.out.println("args1[1]: " + args1[1]);
		
		String[] args2 = args1[1].split("CallId: ");
		
		//String[] s = args2[1].("\\d*");
		
		//System.out.println("test: "+ s[0] + "," + s[2] + "***");
		
		re = "CallId:" + args2[1];
		
		*/
		String regex = "CallId: \\d*";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(outinfo);
		matcher.find();
		String s = matcher.group();
		System.out.println("test pattern: " + s);
		
		re = s;
		
		return re;
	}
	
	public String sysout( String type){
		
		byte[] tmp=new byte[1024];
		
		int count = 10;//wait 5s!
		
		String s = null;
		String s1 = null;
		
		try {
		
	      while(true){
	    	  if( count < 0) break;
	        
			while(in.available()>0){
				int ii=in.read(tmp, 0, 1024);
				if(ii<0)break;
				s = new String(tmp, 0, ii);
				System.out.print(s + "000\n");
				
				if( type.equals("dial")){
					
					s1 = selectID(s);
					
				}
			}
			
	        if(channel.isClosed()){
	        	System.out.println("exit-status: "+ channel.getExitStatus());
	        	break;
	        }
	        
	        try{Thread.sleep(1000);}catch(Exception ee){}
	        
	        count --;
	        
	      }
	      
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return s1;
	      
	}
	
	public String sysout( String type, int time_sec){
		
		byte[] tmp=new byte[1024];
		
		int count = time_sec;//wait 5s!
		
		String s = null;
		String s1 = null;
		
		try {
		
	      while(true){
	    	  if( count < 0) break;
	        
			while(in.available()>0){
				int ii=in.read(tmp, 0, 1024);
				if(ii<0)break;
				s = new String(tmp, 0, ii);
				System.out.print(s + "000\n");
				
				if( type.equals("dial")){
					
					s1 = selectID(s);
					
				}
			}
			
	        if(channel.isClosed()){
	        	System.out.println("exit-status: "+ channel.getExitStatus());
	        	break;
	        }
	        
	        try{Thread.sleep(1000);}catch(Exception ee){}
	        
	        count --;
	        
	      }
	      
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return s1;
	      
	}
	
	
	
	public static void main(String[] args){
		
		ip_router_multiconnect b = new ip_router_multiconnect();
		b.initial_devices();
		//b.construct_ip_router();
		
		b.initial_log_information();
		//b.construct_log_list();
		b.read_infomation_from_file("list.cfg");
		
		
		simple_network_connect a = new simple_network_connect();
		
		
		
		/*
		
		a.login_by_session("192.168.203.168", "admin", "");
		
		String ss = b.get_one_random_device();
		
		
		String s = (new com_generate_multiconnect()).dial_commamd_generate(ss);//dial command generate has randomly chosen one ip
		
		a.send_commands_by_channel(s);
		String dis_com = a.sysout("dial");
		
		b.establish_new_connections("192.168.203.168", ss, dis_com);
		
		//a.send_commands_by_channel(new commands_generate().disconnect_command_generate(dis_com));
		a.send_commands_by_channel(new commands_generate().disconnect_command_generate(b.get_devices().get("192.168.203.168").get(ss).get_callID()));
		a.sysout();
		
		b.disconnect_current_connections("192.168.203.168", ss);
		
		
		
		a.login_by_session("192.168.202.145", "admin", "");
		
		ss = b.get_one_random_device();
		
		
		s = (new com_generate_multiconnect()).dial_commamd_generate(ss);//dial command generate has randomly chosen one ip
		
		a.send_commands_by_channel(s);
		dis_com = a.sysout("dial");
		
		b.establish_new_connections("192.168.202.145", ss, dis_com);
		
		//a.send_commands_by_channel(new commands_generate().disconnect_command_generate(dis_com));
		a.send_commands_by_channel(new commands_generate().disconnect_command_generate(b.get_devices().get("192.168.202.145").get(ss).get_callID()));
		a.sysout();
		
		b.disconnect_current_connections("192.168.202.145", ss);
		
		*/
		
		
		String source_ip_string = b.get_one_random_device();
		
		
		while( !source_ip_string.equals("192.168.203.206")){
			source_ip_string = b.get_one_random_device();
		}
		
		
		System.out.println("source ip:" + source_ip_string);
		
		String target_ip_string;
		
		target_ip_string = b.get_one_random_device();
		System.out.println("target ip:" + target_ip_string);
		
		while( !target_ip_string.equals("192.168.202.213")){
			target_ip_string = b.get_one_random_device();
		}
		
		while( source_ip_string.equals(target_ip_string)){
			target_ip_string = b.get_one_random_device();
			System.out.println("target ip:" + target_ip_string);
		}
		
		/*
		if(b.isconnecting(source_ip_string, source_ip_string))
			System.out.println("##############");
		else
			System.out.println("^^^^^^^^^^^^^^");
		*/
		
		
		
		a.login_by_session(source_ip_string, b.get_loginformation().get(source_ip_string).hostaddr, b.get_loginformation().get(source_ip_string).keywords);
		
		String xcom;
		String returncallid;
		
		xcom = (new com_generate_multiconnect()).dial_commamd_generate(target_ip_string);
		
		a.send_commands_by_channel(xcom);
		returncallid = a.sysout("dial", 5);
		
		b.establish_new_connections(source_ip_string, target_ip_string, returncallid);
		
		//System.out.println("###Connect1:" + source_ip_string + ", " + target_ip_string + "; status: " + b.get_devices().get(source_ip_string).get(target_ip_string).get_status());
		//xcom = (new com_generate_multiconnect()).disconnect_command_generate(b.get_devices().get(source_ip_string).get(target_ip_string).get_callID());
		
		//a.send_commands_by_channel(xcom);
		//a.sysout("", 10);
		
		//b.disconnect_current_connections(source_ip_string, target_ip_string);
		
		
		
		do{
		
			source_ip_string = b.get_one_random_device();
		/*
		while( source_ip_string.equals("192.168.202.213")){
			source_ip_string = b.get_one_random_device();
		}
		*/
			while( !source_ip_string.equals("192.168.203.206")){
				source_ip_string = b.get_one_random_device();
			}
		
			System.out.println("source ip:" + source_ip_string);
		
		
			target_ip_string = b.get_one_random_device();
			System.out.println("target ip:" + target_ip_string);
		
			while( !target_ip_string.equals("192.168.202.145")){
				target_ip_string = b.get_one_random_device();
			}
		
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
		//System.out.println("###Connect1:" + source_ip_string + ", " + target_ip_string + "; status: " + b.get_devices().get(source_ip_string).get(target_ip_string).get_status());
		
		do{
			
			//System.out
			if( b.noconnectionleft()){//just used to ensure could have an initial data
				System.out.println("WORKWORKWORK!!!!!!!!!!!");
				break;
			}
			
			source_ip_string = b.get_one_random_device();
			
			System.out.println("source ip:" + source_ip_string);
		
		
			target_ip_string = b.get_one_random_device();
			//System.out.println("target ip:" + target_ip_string);
		
		
			while( source_ip_string.equals(target_ip_string)){
				target_ip_string = b.get_one_random_device();
				//System.out.println("target ip:" + target_ip_string);
			}
		
			
			
		
		}while(b.isconnecting(source_ip_string, target_ip_string));
		
		System.out.println("no left connecttion : " + b.noconnectionleft());
		
		String dis_id = b.get_callid();
		
		while( !dis_id.equals("all disconnected")){
			
			String[] ars = dis_id.split("#");
			
			simple_network_connect aaa = new simple_network_connect();
			aaa.login_by_session(ars[1], b.get_loginformation().get(ars[1]).hostaddr, b.get_loginformation().get(ars[1]).keywords);
			
			aaa.send_commands_by_channel(new commands_generate().disconnect_command_generate(ars[0]));//delete info in the function get_callid_and_recorder_dieconnect
																									//because of not knowing the sourceip and targetip
			b.disconnect_current_connections(ars[1], ars[2]);
			
			aaa.sysout();
			
			dis_id = b.get_callid();
		}
		
		System.out.println("###END");
		
		
		//s = (new com_generate_multiconnect()).dial_commamd_generate(b);//dial command generate has randomly chosen one ip
		
		//a.send_commands_by_channel(s);
		//dis_com = a.sysout("dial");
		
		
		//a.send_commands_by_channel(new commands_generate().disconnect_command_generate(dis_com));
		//a.sysout();
		
		
		
		
	}
}
