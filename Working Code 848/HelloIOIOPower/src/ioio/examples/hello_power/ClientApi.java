package ioio.examples.hello_power;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

public class ClientApi {

	Socket echoSocket = null;
    PrintWriter out = null;
    BufferedReader in = null;
    String userInput;
    
    Socket connect(int socketnum)
    {
    	 try {
	            echoSocket = new Socket("192.168.2.9",socketnum);
	            out = new PrintWriter(echoSocket.getOutputStream(), true);
	            in = new BufferedReader(new InputStreamReader(
	                                        echoSocket.getInputStream()));
	        } catch (UnknownHostException e) {
	            System.err.println("Don't know about host: localhost");
	            Log.i("Connect","No Connect");
	            return null;
	        } catch (IOException e) {
	            System.err.println("Couldn't get I/O for "
	                               + "the connection to: taranis.");
	            Log.i("Connect","No Connect");
	            return null;
	        }
    	
	        	return echoSocket;
    	
    }
    
	String sendReceive(String input)
	{
	   String c = null;
		userInput = input;
	   

		if (userInput!= null) {
	
			out.println(userInput);
		    try {
				c = in.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.i("Connect", "Can't read from server");
				return null;
			}
		    return c;
		}
		
		else
			return null;
		
	  }
    
   
    void close()
    {
    	out.close();
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			echoSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
}
