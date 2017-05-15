package test;

import java.awt.event.*;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

public class Client {
	
	public static void main(String[] args){
		
		ClientFrame myframe = new ClientFrame();
		
		myframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

}
//----------------------------------END MAIN----------------------------------------------
class ClientFrame extends JFrame{

	private static final long serialVersionUID = 1L;

	public ClientFrame(){
		
		setBounds(600,200,250,350);
		
		ClientPanel mypanel = new ClientPanel();
		
		add(mypanel);
		
		setVisible(true);
		
	}
	
}

class ClientPanel extends JPanel implements Runnable{

	private static final long serialVersionUID = 1L;
	
	private String username;

	public String getUsername() {
		return username;
	}

	public ClientPanel(){
		
		username = JOptionPane.showInputDialog("Enter your nickname");
		
		JLabel nickfield = new JLabel("User: " + username + " Online:");
		
		add(nickfield);
		
		contacts = new JComboBox<String>();
		
		add (contacts);
		
		chatbox = new JTextArea(12,20);
		
		add(chatbox);
		
		field1 = new JTextField(20);
		
		mybutton = new JButton("Send");
		
		add(field1);
		
		SendText myevent = new SendText();
		
		mybutton.addActionListener(myevent);
		
		add(mybutton);
		
		Thread mythread = new Thread(this);
		
		mythread.start();
	}
	
	private JTextArea chatbox;
	
	private JTextField field1;
	
	private JButton mybutton;
	
	private JComboBox<String> contacts;
	
	private Map<String,String> contactsReceived;
	
	public void run() {
		
		mybutton.doClick(200);
		
		try {
			@SuppressWarnings("resource")
			ServerSocket server = new ServerSocket(9090);
			
			SendPackage received_package;
			
			while(true){
				Socket socket=server.accept();
				
				ObjectInputStream data_package= new ObjectInputStream(socket.getInputStream());
				
				received_package=(SendPackage) data_package.readObject();
				
				contactsReceived=received_package.getContacts();
				
				contactsReceived.forEach((k,v) -> contacts.addItem(k));
				
				socket.close();
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	class SendText implements ActionListener{
		
		public void actionPerformed(ActionEvent arg0) {
			
			try{
				Socket socket = new Socket("192.168.1.110",1234);
				
				SendPackage data = new SendPackage();

				InetAddress myaddress = InetAddress.getLocalHost();
				
				data.setClientIp(myaddress.getHostAddress());
				
				data.setNick(username);
				
				try {
					data.setContactName(contacts.getSelectedItem().toString());
				} catch (NullPointerException e) {
					
				}
				
				if (!field1.getText().equals("")) {
					
					data.setMessage(field1.getText());
				
					chatbox.append("\n" + username + " : " + field1.getText().trim());
				
					field1.setText("");
					
				}
				
				ObjectOutputStream data_package = new ObjectOutputStream(socket.getOutputStream());
				
				data_package.writeObject(data);
				
				socket.close();
				
			}  catch (java.net.UnknownHostException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
				
			}
		}
	}
}

class SendPackage implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String clientIp,contactName,nick,message;
	
	public Map<String, String> getContacts() {
		return contacts;
	}

	public void setContacts(Map<String, String> contacts) {
		this.contacts = contacts;
	}

	private Map<String,String> contacts = new HashMap<String,String>();
	
	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String ip) {
		this.clientIp = ip;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}