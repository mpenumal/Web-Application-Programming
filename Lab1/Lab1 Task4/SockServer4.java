import java.net.*;
import java.io.*;
import java.util.*;
import java.nio.*;

class SockServer4 {
    public static void main (String args[]) throws Exception {
        ServerSocket    serv = null;
        InputStream in = null;
        OutputStream out = null;
        Socket sock = null;
        Map<Integer, Integer> totals = new HashMap<Integer, Integer>();
        int clientId = 0;
        byte[] i4 = new byte[4];
        byte[] i5 = new byte[4];
        byte[] i6 = new byte[4];
        
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
                
                in.read(i4);
                clientId = ByteBuffer.wrap(i4).order(ByteOrder.LITTLE_ENDIAN).getInt();
                
                char command = (char)in.read();
                
                in.read(i5);
                int x = ByteBuffer.wrap(i5).order(ByteOrder.LITTLE_ENDIAN).getInt();
                int result = 0;
                
                if (command == 'r' || command == 'R') {
                	System.out.println("Server received reset");
                	totals.put(clientId, 0);
                }
                else {
                	System.out.println("Server received " + x + " for client " + clientId);
					Integer total = totals.get(clientId);
					if (total == null) {
						total = 0;
					}
					totals.put(clientId, total + x);
					result = totals.get(clientId);
					
                }
                i6 = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(result).array();
				out.write(i6);
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
