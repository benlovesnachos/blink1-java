

package thingm.blink1;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Blink1 
{
  //java.nio.ByteBuffer hidDevicePtr;
  //long hidDevicePtr; // FIXME: unused currently, but should be

  static {
    System.loadLibrary("Blink1");     // Load the library
  }  


  public static void usage() { 
    println(""+
"Usage: Blink1 <cmd> [options]\n" +
            "");
  }

  /**
   * Simple command-line demonstration
   */
  public static void main(String args[]) {
    
    if( args.length == 0 ) {
      usage();
    }
    
    // 
    Blink1 blink1 = new Blink1();

    int count = blink1.getCount();

    System.out.println("found "+count+ " devices");
    System.out.println("device paths:");
    String paths[] = blink1.getDevicePaths();
    String serials[] = blink1.getDeviceSerials();
    for( int i=0; i<paths.length; i++ ) { 
      System.out.println( i + ": "+ serials[i] + " : " + paths[i]);
    }

    Random rand = new Random();
    for( int i=0; i<5; i++ ) {
      int r = rand.nextInt() & 0xFF;
      int g = rand.nextInt() & 0xFF;
      int b = rand.nextInt() & 0xFF;
      
      int id = (count==0) ? 0 : rand.nextInt() & (count-1);
      
      System.out.print("setting id: "+id+" to color "+r+","+g+","+b+"   ");

      int rc = blink1.openById( id );
      if( rc == -1 ) { 
        System.out.print("couldn't open "+id+" ");
      }

      rc = blink1.setRGB( r,g,b );
      blink1.close();
      
      if( rc == -1 ) { 
        System.out.println("error detected");
      }
      else { 
        System.out.println();
      }

      blink1.pause( 250 );
    }

  }

  //--------------------------------------------------------------------------

  /**
   * Constructor.  
   * Searches for plugged in blink(1) devices and populates internal caches
   */
  public Blink1() 
  {
    enumerate();
  }


  /**
   * (re)Enumerate the bus and return a count of blink(1) device found
   * @returns blink1_command response code, -1 == fail 
   */
  public native int enumerate();

 /**
   *
   */
  public native int getCount();

  /**
   * Return the list of blink(1) device paths found by enumerate
   */
  public native String[] getDevicePaths();

  /**
   * Return the list of blink(1) device serials found by enumerate
   */
  public native String[] getDeviceSerials();


  /**
   * Open blink(1) device.  
   * Stores open device id statically in native lib.
   * Only one device can be open at once.
   *
   * @returns blink1_command response code, -1 == fail 
   */
  public native int open();

  /**
   * Close blink(1) device
   */
  public native void close();  

  /**
   * Open blink(1) device by USB path, may be different for each insertion
   * @returns blink1_command response code, -1 == fail 
   *
   */
  public native int openByPath( String devicepath );
  
  /**
   * Open blink(1) device by blink(1) serial number
   * @returns blink1_command response code, -1 == fail 
   */
  public native int openBySerial( String serialnumber );
  
  /**
   * Open blink(1) device by blink(1) numerical id (0-getCount())
   * Id list is ordered by serial number.
   * @returns blink1_command response code, -1 == fail 
   */
  public native int openById( int id );
  
  /**
   * FIXME: this does not work
   * Do a transaction with the Blink1
   * length of both byte arrays determines amount of data sent or received
   * @param cmd the blink1 command code
   * @param buf_send is byte array of command to send, may be null
   * @param buf_recv is byte array of any receive data, may be null
   * @returns blink1_command response code, -1 == fail 
   */
  public native synchronized int command(int cmd, 
                                         byte[] buf_send, 
                                         byte[] buf_recv);

  /**
   * Set blink(1) RGB color immediately
   * @param r red component 0-255
   * @param g green component 0-255
   * @param b blue component 0-255
   * @returns blink1_command response code, -1 == fail 
   */
  public native synchronized int setRGB(int r, int g, int b);

  /**
   * Fade blink(1) to RGB color over fadeMillis milliseconds.
   * @param fadeMillis milliseconds to take to get to color
   * @param r red component 0-255
   * @param g green component 0-255
   * @param b blue component 0-255
   * @returns blink1_command response code, -1 == fail 
   */
  public native synchronized int fadeToRGB(int fadeMillis, int r, int g, int b);
  
  /**
   * Write a blink(1) light pattern entry
   * @param fadeMillis milliseconds to take to get to color
   * @param r red component 0-255
   * @param g green component 0-255
   * @param b blue component 0-255
   * @param pos entry position 0-patt_max
   * @returns blink1_command response code, -1 == fail 
   */
  public native synchronized int writePatternLine(int fadeMillis, 
                                                  int r, int g, int b,
                                                  int pos);
  /**
   * @param play  true to play, false to stop
   * @param pos   starting position to play from, 0 = start
   */
  public native synchronized int play( boolean play, int pos);

  /**
   * @param on true = turn on serverdown mode, false = turn it off
   * @param millis milliseconds until light pattern plays if not updated 
   */
  public native synchronized int serverdown( boolean on, int millis);

 

  //-------------------------------------------------------------------------
  // Utilty Class methods
  //-------------------------------------------------------------------------

  /**
   * one attempt at a degamma curve
   * //FIXME: this is now in blink1-lib
   */
  static final public int log2lin( int n )  
  {
    //return  (int)(1.0* (n * 0.707 ));  // 1/sqrt(2)
    return (((1<<(n/32))-1) + ((1<<(n/32))*((n%32)+1)+15)/32);
  }

  /**
   * Utility: A simple delay
   */
  static final public void pause( int millis ) {
      try { Thread.sleep(millis); } catch(Exception e) { }
  }

  static final public void println(String s) { 
    System.out.println(s);
  }
  static final public void print(String s) { 
    System.out.print(s);
  }

}