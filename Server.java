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
			
			String nick,contactName,message;
			
			Map<String,String> contacts = new HashMap<String,String>();
			
			SendPackage received_package;
			
			while(true){
				Socket socket=server.accept();
				
				ObjectInputStream data_package= new ObjectInputStream(socket.getInputStream());
				
				received_package=(SendPackage) data_package.readObject();
				
				message=received_package.getMessage();
				
				nick=received_package.getNick();
				
				contactName=received_package.getContactName();
				
				if (message != null){
					
					textarea.append("\n" + nick + ": " + message + " / for :"+ contactName + " " + contacts.get(contactName));
				}
				
				else {
					
					contacts.put(received_package.getNick(), received_package.getClientIp());
					
					contacts.forEach((k,v) -> textarea.append("\n User: " + k + " / IP: " + v));
				}
				
				message=null;
				
				socket.close();
				
				try{
					Socket socket2 = new Socket(received_package.getClientIp(),9090);
					
					SendPackage data = new SendPackage();
					
					data.setContacts(contacts);
					
					ObjectOutputStream data_package2 = new ObjectOutputStream(socket2.getOutputStream());
					
					data_package2.writeObject(data);
					
					socket2.close();
					
				}  catch (java.net.UnknownHostException e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
					
				}
			
				
			}
			
		} catch(IOException e){
			
			e.printStackTrace();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}