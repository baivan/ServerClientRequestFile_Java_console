
package serverrequestandrecievefile;

/**
 *
 * @author Profesori
 */
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public final static int SOCKET_PORT = 12345;
    public final static String FILE_TO_SEND = "test.html";
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String fileName="";
        ObjectInputStream is = null;
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        ServerSocket servsock = null;
        Socket sock = null;
        
        try{
            servsock = new ServerSocket (SOCKET_PORT);
            while(true){
                System.out.println("Waiting....");
                try{
                    sock = servsock.accept();
                    is = new ObjectInputStream(sock.getInputStream());
                    System.out.println("Accepted connection: "+sock);
                    
                    //get client's file name
                    try{
                        
                    fileName = (String)is.readObject();
                    System.out.println("\nClient requested "+fileName);
                    }
                    catch(ClassNotFoundException classNotFoundException)
			  {
				  System.out.println("\nUnknown object type recieved");
				 
			  }
                    //send file
                    
                    File file = new File(fileName);
                    byte [] byteArray = new byte [(int)file.length()];
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    bis.read(byteArray,0,byteArray.length);
                    os = sock.getOutputStream();
                    System.out.println("Sending "+FILE_TO_SEND+"("+byteArray.length+"bytes)");
                    os.write(byteArray, 0,byteArray.length);
                    os.flush();
                    System.out.println("Done. ");
                }
                catch (IOException ex){
                    System.out.println(ex.getMessage());
                }
                finally {
                    if (bis!=null) {
                        bis.close();
                    }
                    if(os!=null) {
                        os.close();
                    }
                    if(sock!=null) {
                        sock.close();
                    }
                }
            }
        }
        finally{
            if (servsock!=null) {
                servsock.close();
            }
        }
    }
}
