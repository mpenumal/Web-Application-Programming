import java.net.*;
import java.io.*;

class SockClient2 {
    public static void main (String args[]) throws Exception {
        Socket          sock = null;
        OutputStream    out = null;
        InputStream     in = null;
	
        char i1 = ' ';
        int i2 = 0;

        if (args.length != 1) {
        	System.out.println("USAGE: java SockClient2 int1 or java SockClient <r/R>");
        	System.exit(1);
        }
        
        if (args[0].equals("r") || args[0].equals("R")) {
        	i1 = args[0].charAt(0);	
        }
        else {
        	try {        		
              	i2 = Integer.parseInt(args[0]);
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

            out.write(i1);
            out.write(i2);
            int result = in.read();
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
