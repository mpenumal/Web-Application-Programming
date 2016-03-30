import java.net.*;
import java.io.*;
import java.nio.*;

class SockClient4 {
    public static void main (String args[]) throws Exception {
        Socket sock = null;
        OutputStream out = null;
        InputStream in = null;
        char i2 = ' ';
        int i1 = 0, i3 = 0;
        byte[] i4 = new byte[4];
        byte[] i5 = new byte[4];
        byte[] i6 = new byte[4];

        if (args.length != 2) {
        	System.out.println("USAGE: java SockClient4 <id> int1 or java SockClient <id> <r/R>");
        	System.exit(1);
        }
        
        try {        		
          	i1 = Integer.parseInt(args[0]);
          	i4 = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(i1).array();
          	
        }
        catch (NumberFormatException nfe) {
           	System.out.println("Command line args must be integers");
           	System.exit(2);
        }
        
        if (args[1].equals("r") || args[1].equals("R")) {
        	i2 = args[1].charAt(0);
        }
        else {
        	try {        		
              	i3 = Integer.parseInt(args[1]);
              	i5 = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(i3).array();
            }
            catch (NumberFormatException nfe) {
               	System.out.println("Command line args must be integers");
               	System.exit(2);
            }
       }

        try {
            sock = new Socket("localhost", 8888);
            out = sock.getOutputStream();
            in = sock.getInputStream();

            out.write(i4);
            out.write(i2);
            out.write(i5);
            in.read(i6);
            int result = ByteBuffer.wrap(i6).order(ByteOrder.LITTLE_ENDIAN).getInt();
            System.out.println("Result is " + result);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (out != null)  out.close();
            if (in != null)   in.close();
            if (sock != null) sock.close();
        }
    }
}
