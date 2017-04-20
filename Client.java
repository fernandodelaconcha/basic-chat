package basic.chat;

import java.awt.event.*;
import java.io.*;
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

class ClientPanel extends JPanel{

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
	}
	
	private JTextArea chatbox;
	
	private JTextField field1;
	
	private JButton mybutton;
	
	private JComboBox<String> contacts;
	
	class SendText implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			try{
				Socket socket1 = new Socket("192.168.1.110",9999);
				
				SendPackage data = new SendPackage();
				
				data.setNick(username);
				
				data.setIp("192.168.1.110");
				
				data.setMessage(field1.getText());
				
				ObjectOutputStream data_package = new ObjectOutputStream(socket1.getOutputStream());
				
				data_package.writeObject(data);
				
				socket1.close();
				
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
	
	private String ip,nick,message;
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
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


		
	

