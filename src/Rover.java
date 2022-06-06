import java.io.*;
import java.net.DatagramPacket;

/**
 *  Class used to initialize rover and initiate sender
 *  or receiver sequences
 * @author Sai Tarun Sathyan(ss4005)
 */
public class Rover 
{
    byte id;
    RoverConfig roverConfig;
    String filename;

    public Rover(byte roverId,String filename) 
    {
        this.id = roverId;
        this.roverConfig = new RoverConfig(roverId);
        this.filename=filename;
        Receiver r = new Receiver(this);
        r.start();
    }

    public Rover(byte roverId, String filename, String destinationIP) 
    {
        this.id = roverId;
        this.roverConfig = new RoverConfig(roverId);
        String substr = filename.substring(filename.length() - 3);
        //System.out.println(substr);
        if(substr.equals("png")|| substr.equals("jpg"))
        {
        	File f = new File(filename);
        	byte[] fileByteArray = readFileToByteArray(f);
        	Sender s = new Sender(this, destinationIP, fileByteArray);
        	s.start();	
        }
        else if(substr.equals("txt"))
        {
        	File file = new File(filename);
        
        try 
        {
            InputStream fileInputStream = new FileInputStream(file);
            byte[] buff = new byte[(int)file.length()];
            fileInputStream.read(buff);
            Sender send = new Sender(this, destinationIP, buff);
            send.start();
        }
        catch (FileNotFoundException e) 
        {
            System.out.println("File not found");
            e.printStackTrace();
       	} 
        catch (IOException e) 
       	{
            e.printStackTrace();
       	}
      }
    }
    
    
    /**
     * Funtion used to read an image file and store it in a byte array
     * @param file: the file you would convert to bytes
     * @return
     */
    private static byte[] readFileToByteArray(File f) 
    {
        FileInputStream fis = null;
        byte[] bArray = new byte[(int) f.length()];
       
        try 
        {
            fis = new FileInputStream(f);
            fis.read(bArray);
            fis.close();
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        return bArray;
    }
}
