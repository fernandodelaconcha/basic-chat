package test;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class Server {
	
	public static void main(String[] args){
		
		ServerFrame myframe = new ServerFrame();
		
		myframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

}
//----------------------------------END CONSTRUCTOR----------------------------------------------
class ServerFrame extends JFrame implements Runnable{

	private static final long serialVersionUID = 1L;
	
	public ServerFrame(){
		
		setBounds(900,300,250,350);
		
		JPanel mypanel = new JPanel();
		
		mypanel.setLayout(new BorderLayout());
		
		textarea = new JTextArea();
		
		mypanel.add(textarea,BorderLayout.CENTER);
		
		add(mypanel);
		
		setVisible(true);
		
		Thread mythread = new Thread(this);
		
		mythread.start();
		
	}
	private JTextArea textarea;
	
//------------------------------END GRAPHIC INTERFACE--------------------------------------------

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			@SuppressWarnings("resource")
			ServerSocket server = new ServerSocket(1234);
			
			String nick,ip,message;
			
			Map<String,String> contacts = new HashMap<String,String>();
			
			SendPackage received_package;
			
			while(true){
				Socket socket=server.accept();
				
				ObjectInputStream data_package= new ObjectInputStream(socket.getInputStream());
				
				received_package=(SendPackage) data_package.readObject();
				
				message=received_package.getMessage();
				
				nick=received_package.getNick();
				
				ip=received_package.getContactIp();
				
				if (message != null){
					
					textarea.append("\n" + nick + ": " + message + " for "+ ip);
				}
				
				else {
					
					contacts.put(received_package.getNick(), received_package.getClientIp());
					
					contacts.forEach((k,v) -> textarea.append("\n User: " + k + " / IP: " + v));
				}
				
				message=null;
				
				socket.close();
				
			}
			
		} catch(IOException e){
			
			e.printStackTrace();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}