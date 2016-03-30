import java.net.*;
import java.io.*;

class SockClient3 {
    public static void main (String args[]) throws Exception {
        Socket          sock = null;
        OutputStream    out = null;
        InputStream     in = null;
	
        char i2 = ' ';
        int i1 = 0, i3 = 0;

        if (args.length != 2) {
        	System.out.println("USAGE: java SockClient3 <id> int1 or java SockClient <id> <r/R>");
        	System.exit(1);
        }
        
        try {        		
          	i1 = Integer.parseInt(args[0]);
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
            out.write(i3);
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
