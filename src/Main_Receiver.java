import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;


/**
 * Run this to activate the receiver node
 * @author Sai Tarun Sathyan(ss4005)
 *
 */
public class Main_Receiver 
{
	 public static void main(String[] args) throws IOException, InterruptedException 
	 {
		 
		 byte roverId = Byte.parseByte("101");//default rover ID
	            if(args.length!=0)
	            {
	            	roverId = Byte.parseByte(args[0]);
	            }
 	            	           
    			
		 	while(true)
		 	{
		 		DatagramSocket socket = new DatagramSocket(3000);
		 		 //<<<Receiving Syn Packet>>>
		 		System.out.println("\nReady to Receive");
		 		System.out.println("---------------------");
		 		byte[] buffer = new byte[40];
	            DatagramPacket Packet = new DatagramPacket(buffer, buffer.length);
	            socket.receive(Packet);
		 		String s= (new String(Packet.getData())).trim();
		 		String input[] = s.trim().split(" ");
		 		
		 		// <<<Sending SynAck>>>
		 		System.out.println("Received Input\nSending  Ack...");
		 		buffer=new byte[40];
		 		buffer= "0".getBytes();
		 		DatagramPacket DpSend =new DatagramPacket(buffer,buffer.length,Packet.getAddress(), 3001);
		 		socket.send(DpSend);
		 		
		 		if(input[0].equals("1"))
	            {
		 			System.out.println("-------------------------------------------------------");
		 			System.out.println("|Incoming Text File : "+input[1]);
		 			System.out.println("|Source : "+Packet.getAddress());
		 			System.out.println("-------------------------------------------------------");
		 			new Rover(roverId,input[1]);
		 			TimeUnit.SECONDS.sleep(12);//waiting for file transfer to end before moving on
		 			socket.close();
	            }
		 		else if(input[0].equals("2"))
	            {
		 			System.out.println("-------------------------------------------------------");
		 			System.out.println("|Incoming Image File : "+input[1]);
		 			System.out.println("|Source : "+Packet.getAddress());
		 			System.out.println("-------------------------------------------------------");
		 			new Rover(roverId,input[1]);
		 			TimeUnit.SECONDS.sleep(12);//waiting for file transfer to end before moving on
		 			socket.close();
	            }
		 		else if(input[0].equals("3"))
		 		{	
		 			System.out.println("-------------------------------------------------------");
		 			System.out.println("|Received Command : "+ s.substring(1,s.length()));
		 			System.out.println("|Source : "+Packet.getAddress());
		 			System.out.println("-------------------------------------------------------");
		 			TimeUnit.SECONDS.sleep(12);//waiting for file transfer to end before moving on
		 			socket.close();
		 		}
		 		else if(input[0].equals("4"))
		 		{	
		 			System.out.println("-------------------------------------------------------");
		 			System.out.println("|Transmission Terminated : ");
		 			System.out.println("|Source : "+Packet.getAddress());
		 			System.out.println("-------------------------------------------------------");
		 			//TimeUnit.SECONDS.sleep(12);//waiting for file transfer to end before moving on
		 			socket.close();
		 			System.exit(0);
		 			break;
		 		}
		 		else
		 		{
		 			socket.close();
		 			continue;
		 		}
	 
		 	}
	    }

}
