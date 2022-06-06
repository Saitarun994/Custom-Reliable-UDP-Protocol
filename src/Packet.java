import java.util.Arrays;

/**
 * This class acts as the data structure for the entire packet 
 * that is going to be transported
 * @author Sai Tarun Sathyan(ss4005)
 *
 */
public class Packet 
{
    Header header;
    byte[] data;
    public static final int max = 1024;

    
    /**
     * Constructor used to create the packet
     * @param Header:	A header data structure containing info about the packet
     * @param data:	The payload to be transported
     * @throws Exception
     */
    public Packet(Header Header, byte[] data) throws Exception 
    {
        this.header = Header;
        if (data.length > max - Header.size) //Making sure packet size is below 1024 bytes
        {
            throw new Exception("Packet size too large");
        }
        else
        {
        	this.data = data;
        }
    }

    
    /**
     * Constructor to initialize packet without assigning payload
     * @param Header
     */
    public Packet(Header Header) 
    {
        this.header = Header;
        this.data = new byte[0];
    }


    /**
     * Constructor to create packet using an array of bytes
     * @param packet
     */
    public Packet(byte[] packet) 
    {
        byte[] headerByteArray = Arrays.copyOfRange(packet, 0, Header.size);
        this.header = new Header(headerByteArray);
        this.data = Arrays.copyOfRange(packet, Header.size, packet.length);
    }

    //getter for data
    public byte[] getData() 
    {
        return data;
    }
    //getter for header
    public Header getHeader() 
    {
        return header;
    }
    
    /**
     * Convert the entire packet into an array of bytes before sending it
     * @return
     */
    public byte[] get_byteArray() 
    {
        byte[] packet = new byte[Header.size + data.length];
        byte[] header = this.header.get_byteArray();

        for (int i=0; i < header.length; i++) 
        {
            packet[i] = header[i];
        }

        for (int i=0; i < data.length; i++) 
        {
            packet[i + header.length] = data[i];
        }

        return packet;
    }
}
