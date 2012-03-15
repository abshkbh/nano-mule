package ioio.examples.hello_power;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.IOIOFactory;
import ioio.lib.api.Uart;
import ioio.lib.api.Uart.Parity;
import ioio.lib.api.Uart.StopBits;
import ioio.lib.api.exception.ConnectionLostException;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * This is the main activity of the HelloIOIOPower example application.
 * 
 * It displays a toggle button on the screen, which enables control of the
 * on-board LED, as well as a text message that shows whether the IOIO is
 * connected.
 * 
 * Compared to the HelloIOIO example, this example does not use the
 * AbstractIOIOActivity utility class, thus has finer control of thread creation
 * and IOIO-connection process. For a simpler use cases, see the HelloIOIO
 * example.
 */
public class MainActivity extends Activity {
	/** The text displayed at the top of the page. */
	private TextView title_;
	/** The toggle button used to control the LED. */
	private ToggleButton button_;
	private Button getdata_;
	private Button connect_;
	private TextView text_;
	private TextView text2_;
	/** The thread that interacts with the IOIO. */
	private IOIOThread ioio_thread_;
	AESEncrypt e ;
	ClientApi client;
	Socket s = null;
/*	private IOIO ioio_;
	private boolean abort_ = false;
	private Uart uart;
	private InputStream in;
	private OutputStream out;
	byte[] inBuffer = new byte[16]; */
	
	
	/**
	 * Called when the activity is first created. Here we normally initialize
	 * our GUI.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		title_ = (TextView) findViewById(R.id.title);
		button_ = (ToggleButton) findViewById(R.id.button);
		text_ =   (TextView)findViewById(R.id.rxText);
		getdata_ = (Button)findViewById(R.id.ToggleButton2);
        connect_ = (Button)findViewById(R.id.button3);
	    text2_ = (TextView)findViewById(R.id.text2);
	    client = new ClientApi();
        e = new AESEncrypt();
	
	}

	/**
	 * Called when the application is resumed (also when first started). Here is
	 * where we'll create our IOIO thread.
	 */
	@Override
	protected void onResume() {
		super.onResume();
		ioio_thread_ = new IOIOThread();
		ioio_thread_.start();
	}

	/**
	 * Called when the application is paused. We want to disconnect with the
	 * IOIO at this point, as the user is no longer interacting with our
	 * application.
	 */
	@Override
	protected void onPause() {
		super.onPause();
		ioio_thread_.abort();
		try {
			ioio_thread_.join();
		} catch (InterruptedException e) {
		}
	}

	
	 
	/**
	 * This is the thread that does the IOIO interaction.
	 * 
	 * It first creates a IOIO instance and wait for a connection to be
	 * established. Then it starts doing the main work of opening the LED pin
	 * and constantly updating it to match the toggle button's state.
	 * 
	 * Whenever a connection drops, it tries to reconnect, unless this is a
	 * result of abort().
	 */
	class IOIOThread extends Thread {
		private IOIO ioio_;
		private boolean abort_ = false;
		private Uart uart;
		private InputStream in;
		private OutputStream out;
	    byte[] inBuffer = new byte[16];
	    byte[] cipher = new byte[1];
		
		/** Thread body. */
		@Override
		public void run() {
			super.run();
			while (true) {
				synchronized (this) {
					if (abort_) {
						break;
					}
					ioio_ = IOIOFactory.create();
				}
				try {
					int i = 0;
					setText(R.string.wait_ioio);
					ioio_.waitForConnect();
					setText(R.string.ioio_connected);
					uart = ioio_.openUart(4,3, 115200,Parity.NONE,StopBits.ONE);
					in = uart.getInputStream();
					out = uart.getOutputStream();
					DigitalOutput led = ioio_.openDigitalOutput(0, true);
				while (true) {
					//	out.write(65);
					// sleep(10);
					   
					
					    if(getdata_.isPressed())
					    	in.read(inBuffer, 0, 1);
					   
					    setrxText(Byte.toString(inBuffer[i]));
					     sleep(20);
				    cipher = e.encrypt(Byte.toString(inBuffer[0]));
					    if (cipher==null)
					    	settext2Text("NULL");
					    else
					    	settext2Text(e.asHex(cipher));
					  
					    
					    
					    
					    
					    
					    //	i++;
					//	i=i%31;
					//	sleep(100);
					//	sleep(100);
					//	in.read(inBuffer, 0, 1);
					//	setrxText(Byte.toString(inBuffer[0]));
						
			
              //  int availableBytes =in.available(); 
                //       if (availableBytes > 0) { 
                  //  	   byte[] readBuffer = new byte[32]; 
                    //       in.read(readBuffer, 0, availableBytes); 
                    //     char[] Temp = (new String(readBuffer, 0, availableBytes)).toCharArray(); 
                      //   String Temp2 = new String(Temp); 
                      //   setrxText("Here");
                        // setrxText(Temp2);				
	
					//in.read(inBuffer,0,1);
					
					
		//}
	
				
				
				
				
				
				
			}
				} catch (ConnectionLostException e) {
				} catch (Exception e) {
					Log.e("HelloIOIOPower", "Unexpected exception caught", e);
					ioio_.disconnect();
					break;
				} finally {
					try {
						ioio_.waitForDisconnect();
					} catch (InterruptedException e) {
					}
				}
			}
		}

		/**
		 * Abort the connection.
		 * 
		 * This is a little tricky synchronization-wise: we need to be handle
		 * the case of abortion happening before the IOIO instance is created or
		 * during its creation.
		 */
		synchronized public void abort() {
			abort_ = true;
			if (ioio_ != null) {
				ioio_.disconnect();
			}
		}

		/**
		 * Set the text line on top of the screen. 
		 * @param id The string ID of the message to present.
		 */
		private void setText(final int id) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					title_.setText(getString(id));
				}
			});
	}
	
	
		
		
		private void setrxText(final String s) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					text_.setText(s);
				}
			});
		}
	}



	private void settext2Text(final String s) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				text2_.setText(s);
			}
		});
}













}