import java.util.Arrays;
import java.util.List;
import java.util.Collections;


/**
 * Class used to configure and initialize new rovers
 * @author Sai Tarun Sathyan(ss4005)
 */
public final class RoverConfig 
{
    byte roverID;
    String addr;
    int receive_port = 1998;
    int send_port = 1999;
    List<String> AddressTemplate = Arrays.asList("10", "0", "x", "0");

    /**
     * Constructor
     * @param routerId
     */
    public RoverConfig(byte id) 
    {
        this.roverID = id;
        this.addr = getAddress();
    }

    /**
     * We replace the x with roverID to return the full address
     */
    public String getAddress () 
    {
        Collections.replaceAll(AddressTemplate, "x", Byte.toString(this.roverID));
        return String.join(".", AddressTemplate);
    }
}
