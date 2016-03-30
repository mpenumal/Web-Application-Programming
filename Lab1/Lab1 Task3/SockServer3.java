import java.net.*;
import java.io.*;
import java.util.*;

class SockServer3 {
    public static void main (String args[]) throws Exception {
        ServerSocket    serv = null;
        InputStream in = null;
        OutputStream out = null;
        Socket sock = null;
        Map<Integer, Integer> totals = new HashMap<Integer, Integer>();
        int clientId = 0;
        
        try {
            serv = new ServerSocket(8888);
        } 
        catch(Exception e) {
        	e.printStackTrace();
        }
        
        while (serv.isBound() && !serv.isClosed()) {
            System.out.println("Ready...");
            try {
                sock = serv.accept();
                in = sock.getInputStream();
                out = sock.getOutputStream();

                clientId = in.read();
                char command = (char)in.read();
                int x = in.read();
                if (command == 'r' || command == 'R') {
                	System.out.println("Server received reset");
                	totals.put(clientId, 0);
                	out.write(0);
                }
                else {
                	System.out.println("Server received " + x + " for client " + clientId);
					Integer total = totals.get(clientId);
					if (total == null) {
						total = 0;
					}
					totals.put(clientId, total + x);
					out.write(totals.get(clientId));
                }
                out.flush();
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
}
