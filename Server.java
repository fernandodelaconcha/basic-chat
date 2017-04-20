package basic.chat;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

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
			ServerSocket server = new ServerSocket(9999);
			
			String nick,ip,message;
			
			SendPackage received_package;
			
			while(true){
				Socket socket1=server.accept();
				
				ObjectInputStream data_package= new ObjectInputStream(socket1.getInputStream());
				
				received_package=(SendPackage) data_package.readObject();
				
				nick=received_package.getNick();
				
				message=received_package.getMessage();
				
				ip=received_package.getIp();
				
				textarea.append("\n" + nick + ": " + message + " " + ip);
				
				socket1.close();
				
			}
			
		} catch(IOException e){
			
			e.printStackTrace();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}