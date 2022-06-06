import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


/**
 * Run this to activate the sender node
 * @author Sai Tarun Sathyan(ss4005)
 *
 */
public class Main_Sender 
{
    public static void main(String[] args) throws IOException, InterruptedException 
    {		
    		String destinationRoverIP = "172.29.224.1";
    		byte roverId = Byte.parseByte("102");
    		if(args.length==2)
    		{
    			roverId = Byte.parseByte(args[0]);
    			destinationRoverIP = args[1];
    			
    		}
    		
    		//<<<Initializing variables>>>
    		byte buff[]= new byte[40];	// stores incoming acks
        	String input="0";
        	Scanner sc = new Scanner(System.in);
        	
        	
    		
    
    		//<<<<<Main input loop>>>>>
    		while(!(input.equals("4")))
        	{	
    			DatagramSocket socket = new DatagramSocket();
        		DatagramSocket receiveSocket = new DatagramSocket(3001);
        		System.out.println("Select An Option To Send :-");
        		System.out.println("-----------------------------------");
        		System.out.println("1.Send txt file");
        		System.out.println("2.Send img file");
        		System.out.println("3.Send command");
        		System.out.println("4.exit");
        		System.out.print("Enter Option:   ");
        		input = sc.nextLine();
   
        		
        		if(input.equals("1"))
        		{	
        			String txtfilename = "big_text_file(5mb).txt";
        			String userChoice="1 "+txtfilename;
        			//<<<sending syn and waiting for ack>>>
        			byte[] data = userChoice.getBytes();
        			while(true)
        			{	
        				DatagramPacket DpSend =new DatagramPacket(data, data.length,InetAddress.getByName(destinationRoverIP), 3000);
        				socket.send(DpSend);
        				DatagramPacket ack = new DatagramPacket(buff,buff.length);
        				System.out.println("Waiting for Syn Ack");
        				try
        				{	receiveSocket.setSoTimeout(3000);
        					receiveSocket.receive(ack);
        				}
        				catch(Exception e)
        				{
        					System.out.println("Sending Input once more...");
        					String s= (new String(ack.getData())).trim();
        					//System.out.println(s);
        					if(s.equals("0"))
        					{
        						System.out.println("Received Syn Ack");
        						break;
        					}
        				}
        			}
        			socket.close();
        			receiveSocket.close();
        			new Rover(roverId, txtfilename, destinationRoverIP);
        			TimeUnit.SECONDS.sleep(12);//waiting for file transfer to end before moving on
        			
        		}
        		
        		
        		else if(input.equals("2"))
        		{
        			String txtfilename = "Snake_River_(5mb).jpg";
        			String userChoice="2 "+txtfilename;
        			//<<<sending syn and waiting for ack>>>
        			byte[] data = userChoice.getBytes();
        			while(true)
        			{	
        				DatagramPacket DpSend =new DatagramPacket(data, data.length,InetAddress.getByName(destinationRoverIP), 3000);
        				socket.send(DpSend);
        				DatagramPacket ack = new DatagramPacket(buff,buff.length);
        				System.out.println("Waiting for Syn Ack");
        				try
        				{	receiveSocket.setSoTimeout(3000);
        					receiveSocket.receive(ack);
        				}
        				catch(Exception e)
        				{
        					System.out.println("Sending Input once more...");
        					String s= (new String(ack.getData())).trim();
        					//System.out.println(s);
        					if(s.equals("0"))
        					{
        						System.out.println("Received Syn Ack");
        						break;
        					}
        				}
        			}
        			socket.close();
        			receiveSocket.close();
        			new Rover(roverId, txtfilename, destinationRoverIP);
        			TimeUnit.SECONDS.sleep(12);//waiting for file transfer to end before moving on
        		}
        		
        		
        		else if(input.equals("3"))
        		{
        			System.out.print("Enter Command : ");	
        			String cmd = sc.nextLine();
        			String userChoice="3 "+cmd;
        			
        			//<<<sending syn and waiting for ack>>>
        			byte[] data = userChoice.getBytes();
        			while(true)
        			{	
        				DatagramPacket DpSend =new DatagramPacket(data, data.length,InetAddress.getByName(destinationRoverIP), 3000);
        				socket.send(DpSend);
        				DatagramPacket ack = new DatagramPacket(buff,buff.length);
        				System.out.println("waiting for input ack");
        				try
        				{	receiveSocket.setSoTimeout(3000);
        					receiveSocket.receive(ack);
        				}
        				catch(Exception e)
        				{
        					System.out.println("Sending input once more...");}
        					String s= (new String(ack.getData())).trim();
        					//System.out.println(s);
        					if(s.equals("0"))
        					{
        						break;
        					}	
        			}
        			socket.close();
        			receiveSocket.close();
        			System.out.println("Command sent successfully");
        			TimeUnit.SECONDS.sleep(12);//waiting for file transfer to end before moving on
        		}
        		
        		
        		else if(input.equals("4"))
        		{
        			System.out.println("Exiting...");
        			String userChoice="4";
        			
        			//<<<sending syn and waiting for ack>>>
        			byte[] data = userChoice.getBytes();
        			while(true)
        			{	
        				DatagramPacket DpSend =new DatagramPacket(data, data.length,InetAddress.getByName(destinationRoverIP), 3000);
        				socket.send(DpSend);
        				DatagramPacket ack = new DatagramPacket(buff,buff.length);
        				System.out.println("waiting for input ack");
        				try
        				{	receiveSocket.setSoTimeout(3000);
        					receiveSocket.receive(ack);
        				}
        				catch(Exception e)
        				{
        					System.out.println("Sending input once more...");}
        					String s= (new String(ack.getData())).trim();
        					//System.out.println(s);
        					if(s.equals("0"))
        					{
        						break;
        					}	
        			}
        			
        			System.out.println("Connection terminated");
        			socket.close();
        			receiveSocket.close();
        			System.exit(0);
        		}
        		
        		else
        		{
        			System.out.println(input+" Is Incorrect , Try Again");
        			continue;
        		}
        		socket.close();
        		receiveSocket.close();
        		System.out.println("end of loop");
        	}
        	
        	sc.close();

    }
}
