package arduino;
import java.io.*;

public class SerialClass {

    public static String sendCommand(String command) {
	StringBuffer output = new StringBuffer();
 
	Process p;
	try {
	    p = Runtime.getRuntime().exec("arduino-serial -q -b 9600 -p /dev/ttyUSB0 -S " + command + " -r");
	    p.waitFor();
	    BufferedReader reader = 
		new BufferedReader(new InputStreamReader(p.getInputStream()));
 
	    String line = "";			
	    while ((line = reader.readLine())!= null) {
		output.append(line + "\n");
	    }
 
	} catch (Exception e) {
	    e.printStackTrace();
	}
 
	return output.toString();
    } 

    public static void ouvrirCasier(int id) throws Exception{
	String output = sendCommand("o"+id);
	if (output.equals("KO")){
		throw new Exception("Erreur avec l'arduino lors de l'ouverture du casier");
	}
    }

    public static void fermerCasier(int id) throws Exception{
	String output = sendCommand("f"+id);
	if (output.equals("KO")){
		throw new Exception("Erreur avec l'arduino lors de la fermeture du casier");
	}
    }    

    public static int peserCasier(int id) throws Exception{
	String output = sendCommand("c"+id);
	if (output.equals("KO")){
		throw new Exception("Erreur avec l'arduino lors du pesage du casier du casier");
	}
	return Integer.parseInt(output.trim());
    }
}
