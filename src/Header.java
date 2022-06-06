/**
 * This class acts as the data structure for 
 *  the packet header
 * @author Sai Tarun Sathyan(ss4005)
 */
public class Header 
{
	public final static int size = 8;// size of the entire header
    String sourceIp;
    boolean isLast;
    long seq;
    byte destRoverID;
 
    /**
     *  Header Constructor
     * @param sourceIP: The IP of the sender
     * @param seq : The sequence number of the current packet
     * @param isLast : Tells us know if current packet is the last one
     * @param destinationRouterId : The IP of the receiver 
     */
    public Header(String sourceIp, long seq, boolean isLast, byte destId) 
    {
        this.sourceIp = sourceIp;
        this.seq = seq;
        this.isLast = isLast;
        this.destRoverID = destId;
    }


    /**
     * Header constructor using a byte array as input
     * @param byteArray
     */
    public Header(byte[] byteArray) 
    {
        this.sourceIp = byteArray[0] + "." + byteArray[1] + "." + byteArray[2] + "." + byteArray[3];
        this.seq = (byteArray[4] & 0xff << 8) + (byteArray[5] & 0xff);
        this.isLast = (byteArray[6] == 1) ? true : false;
        this.destRoverID = byteArray[7];
    }

    //getter for source ip
    public String getSourceIp() 
    {
        return sourceIp;
    }

    //getter for sequence number
    public long getSeq() 
    {
        return seq;
    }

    //getter for isLast flag
    public boolean isLast() 
    {
        return isLast;
    }
    
    
    /**
     * Function to Convert the entire header into an array of bytes before sending it
     * @return
     */
    public byte[] get_byteArray() 
    {
        byte[] header = new byte[size];
        String[] sourceIp = this.sourceIp.split("\\.");
        header[0] = Byte.parseByte(sourceIp[0]);
        header[1] = Byte.parseByte(sourceIp[1]);
        header[2] = Byte.parseByte(sourceIp[2]);
        header[3] = Byte.parseByte(sourceIp[3]);
        header[4] = (byte)(this.seq >> 8);
        header[5] = (byte) this.seq;
        header[6] = this.isLast ? (byte)1 : (byte)0;
        header[7] = this.destRoverID;
        return header;
    }


}
