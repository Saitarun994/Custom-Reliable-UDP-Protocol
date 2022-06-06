import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;


/**
 * Activates the receiver sequence based on the type 
 * of file incoming
 * @author Sai Tarun Sathyan(ss4005)
 *
 */
public class Receiver extends Thread 
{
    Rover rover;
    DatagramSocket socket;
    String substr;
    File incomingFile;
    FileOutputStream outToFile;
    

    public Receiver(Rover rover) 
    {
        this.rover = rover;
        try 
        {
            this.socket = new DatagramSocket(rover.roverConfig.receive_port);
        } 
        catch (SocketException e) 
        {
            System.err.print("Socket Failure  \n");
            e.printStackTrace();
        }

        try 
        {	
        	//<<<Creating the Appropriate File>>>
        	incomingFile =new File ("R_"+rover.filename);
        	substr = (rover.filename).substring((rover.filename.length()) - 3);
            Files.deleteIfExists(incomingFile.toPath());
            incomingFile.createNewFile();
            System.out.println("File Object Created...");
            if(substr.equals("png")||substr.equals("jpg"))
            {
            	outToFile = new FileOutputStream(incomingFile);
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run() 
    {	
    	System.out.println("Ready to receive files...");
       
    	//<<<Receiving packets and sending acks>>>
    	while (true) 
        {
            DatagramPacket receiveDatagramPacket = new DatagramPacket(
            		new byte[Packet.max], Packet.max);
            try 
            {
                socket.receive(receiveDatagramPacket);
            } 
            catch (IOException e) 
            {
                System.out.println("RECEIVER: Unable to receive.");
                e.printStackTrace();
            }

            byte[] data = receiveDatagramPacket.getData();
            Packet Packet = new Packet(data);// used to make packet from byte array
            processPacket(Packet, receiveDatagramPacket.getAddress());

            if (Packet.getHeader().isLast()) 
            {
            	System.out.println("--------------------------------------------");
                System.out.println("|Saved the file ");
                System.out.println("|Wait for 10s before next transfer");
                System.out.println("--------------------------------------------");
				socket.close();
                break;
            }
        }
    }


    /**
     * Used to analyze the packet and send an Ack to the sender
     * @param packet: packet containing the data to be saved
     * @param senderAddr: IP address of the packet sender
     */
    public void processPacket(Packet packet, InetAddress senderAddr) 
    {
        byte[] data = packet.getData();
        Header header = packet.getHeader();
        String message = (new String(data)).trim();
        System.out.println("Received Sequence no: " + header.getSeq());
        
      //<<<Writing the received bytes into a file>>>
        
        if(substr.equals("png")||substr.equals("jpg"))
        {
            try
            {	
				outToFile.write(data);
				if (packet.getHeader().isLast()) 
	            {
	                outToFile.close();
	            }
			} 
            catch (FileNotFoundException e) 
            {
				e.printStackTrace();
			} 
            catch (IOException e)
            {
				e.printStackTrace();
			}
        }
        
        if(substr.equals("txt"))
        {
        	try(FileWriter fw = new FileWriter(incomingFile, true);
        				BufferedWriter bw = new BufferedWriter(fw);
        						PrintWriter out = new PrintWriter(bw))
        	{	
        		out.print(message);	
        	} 
        	catch (Exception e) 
        	{
        		e.printStackTrace();
        	}
        }
        sendAck(senderAddr, header.getSeq(), header.isLast());
    }

    /**
     *Function to send an ack to the sender
     */
    public void sendAck(InetAddress destIP, long ackNo, boolean isLast) 
    {
        Header header = new Header(rover.roverConfig.addr, ackNo, isLast, (byte) 1);
        byte[] ackPacket = new Packet(header).get_byteArray();

        
        try 
        {
            DatagramPacket datagramPacket = new DatagramPacket(ackPacket, 
            		ackPacket.length, destIP, rover.roverConfig.send_port);
            this.socket.send(datagramPacket);
            System.out.println("Ack Sent for Sequence no: " + ackNo);
        } 
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        } 
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
