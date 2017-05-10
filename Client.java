package test;

import java.awt.event.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
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
	
	public void run() {
		
		mybutton.doClick(200);
		
	}
	
	class SendText implements ActionListener{
		
		public void actionPerformed(ActionEvent arg0) {
			
			try{
				Socket socket = new Socket("192.168.1.110",1234);
				
				SendPackage data = new SendPackage();

				InetAddress myaddress = InetAddress.getLocalHost();
				
				data.setClientIp(myaddress.getHostAddress());
				
				data.setNick(username);
				
				data.setContactIp("");
				
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
	
	private String clientIp,contactIp,nick,message;
	
	public String getContactIp() {
		return contactIp;
	}

	public void setContactIp(String contactIp) {
		this.contactIp = contactIp;
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