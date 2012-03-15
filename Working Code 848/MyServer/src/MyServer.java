import java.net.*;
import java.io.*;



public class MyServer {

	public static void main(String[] args) throws IOException {

		AESDecrypt dec = new AESDecrypt();
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(3000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444.");
            System.exit(1);
        }

        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
				new InputStreamReader(
				clientSocket.getInputStream()));
        String inputLine;
        //outputLine;
    //    KnockKnockProtocol kkp = new KnockKnockProtocol();

      //  outputLine = kkp.processInput(null);
       // out.println(outputLine);

        while ((inputLine = in.readLine()) != null) {
            // outputLine = kkp.processInput(inputLine);
            // out.println(outputLine);
             
        	byte[] d = new byte[inputLine.length()];
        	if (inputLine.equals("Bye"))
                break;
             	
        	    d = dec.toByte(inputLine);
        	    System.out.println("Rceived :"+dec.asHex(d));
        	    d = dec.decrypt(d);
        	    System.out.println("Decrypted :"+dec.asHex(d));
             	out.println(dec.asHex(d));
        //	out.println(inputLine);
            
        }
        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
    }
	
	
	
	
}
