import java.io.IOException;
import java.net.*;


/**
 * This class is used to send files from one rover to another
 * @author Sai Tarun Sathyan(ss4005)
 *
 */
public class Sender extends Thread 
{
    DatagramSocket socket;
    Rover rover;
    String destIP;
    byte[] fileData;
    InetAddress destAddr;

    
    /**
     * Constructor
     * @param rover : rover class object
     * @param destinationRouterIP
     * @param fileContent
     */
    public Sender(Rover rover, String destIP, byte[] fileData) 
    {
        this.rover = rover;
        this.destIP = destIP;
        this.fileData = fileData;

        try 
        {
            this.destAddr = InetAddress.getByName(this.destIP);
        } 
        catch (UnknownHostException e) 
        {
            System.out.println("SENDER: Unable to resolve IP: " + this.destIP);
            e.printStackTrace();
        }

        try 
        {
            this.socket = new DatagramSocket(rover.roverConfig.send_port);
        }
        catch (SocketException e)
        {
            System.out.println("SENDER: Unable to create socket.");
            e.printStackTrace();
        }
    }

    /**
     * Run method for the thread to keep sending packets
     */
    @Override
    public void run() 
    {
        long seqNum = 0;
        long ackNum = 0;
        boolean isLast = false;
        int maxDataLength = Packet.max - Header.size;
        
        
        //<<<Sending data and waiting for acks>>>
        for (int i = 0; i < this.fileData.length; i += maxDataLength) 
        {
            seqNum += 1;
            seqNum = seqNum % 256;
            
            
            if (i + maxDataLength >= fileData.length)//checking if this packet is the final one
                isLast = true;

            
            byte[] data = new byte[maxDataLength];
            if (isLast) 
            {
                for (int j = 0; j < fileData.length - i; j++) 
                {
                    data[j] = fileData[i + j];
                }
            }
            else 
            {
                for (int j = 0; j < maxDataLength; j++) 
                {
                    data[j] = fileData[i + j];
                }
            }

            //<<<Joining the packet with appropriate Header>>>
            Header Header = new Header(rover.roverConfig.addr, seqNum, isLast, (byte)1);
            Packet packet = null;
            try 
            {
                packet = new Packet(Header, data);
            }
            catch (Exception e) 
            {
                e.printStackTrace();
            }
            
            byte[] packetBytes = packet.get_byteArray();
            DatagramPacket sendDatagramPacket = new DatagramPacket(packetBytes, 
            		packetBytes.length, destAddr, rover.roverConfig.receive_port);

            try 
            {
                System.out.println("Sending packet with seq. #" + seqNum);
                socket.send(sendDatagramPacket);
            } 
            catch (IOException e) 
            {
                System.out.println("SENDER: Unable to send!");
                e.printStackTrace();
            }

            boolean ackReceived = false;

            // <<< Expecting ACK>>>
            while (!ackReceived) 
            {
                byte[] buffer = new byte[8];
                DatagramPacket ackPacket = new DatagramPacket(buffer, buffer.length);

                try 
                {
                    socket.setSoTimeout(2000);
                    socket.receive(ackPacket);
                    Header ackHeader = new Header(buffer);

                    ackNum = ackHeader.getSeq();

                    
                    if (ackNum == seqNum) // Ack checks out
                    {
                        System.out.println("Got ACK for Sequence no: " + seqNum);
                        ackReceived = true;
                        break;
                    }
                }
                catch (SocketTimeoutException e) 
                {
                    System.err.println("Didn't Receive ACK for Sequence No: " + seqNum);
                    System.out.println("Resending Packet - Sequence No: "+ seqNum);
                    try 
                    {
                        socket.send(sendDatagramPacket);
                    } 
                    catch (IOException ioException) 
                    {
                        ioException.printStackTrace();
                    }
                }
                catch (IOException e) 
                {
                    e.printStackTrace();
                }
            }
            
            
        }
        System.out.println("----------------------------------------");
        System.out.println("|File Transfer Successfull");
        System.out.println("|Wait 10s Before Next Trasnfer");
        System.out.println("----------------------------------------");
        socket.close();
    }
}
