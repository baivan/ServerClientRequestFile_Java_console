
package serverrequestandrecievefile;

/**
 *
 * @author Profesori
 */
import java.io.ObjectInputStream;
import java.util.Scanner;
import java.io.ObjectOutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Client {
    public final static int SOCKET_PORT = 12345;
    public final static String SERVER = "127.0.0.1";
   public final static String FILE_TO_RECIEVE = "test2";
    public final static int FILE_SIZE = 6022386;
    
    public static void main(String [] args) throws IOException
    {
        int bytesRead;
        int current=0;
        String fileName="";
        Scanner input = new Scanner(System.in);
        ObjectOutputStream os =null;
        FileOutputStream fos =null;
        BufferedOutputStream bos = null;
        Socket sock = null;
        try
        {
            sock = new Socket(SERVER,SOCKET_PORT);
            os= new ObjectOutputStream(sock.getOutputStream());
           System.out.println("Connecting...");
           System.out.println("\nPlease type the file you would like to get from the server");
           fileName = input.nextLine();
           //
           //send the file name to the server
           os.writeObject(fileName);
           //recieve file
           byte [] byteArray = new byte[FILE_SIZE];
           InputStream is = sock.getInputStream();
           fos = new FileOutputStream(FILE_TO_RECIEVE);
           bos = new BufferedOutputStream(fos);
           bytesRead = is.read(byteArray, 0,byteArray.length);
           current = bytesRead;
           
           do 
           {
               bytesRead = is.read(byteArray,current, (byteArray.length-current));
               if(bytesRead >=0)
               {
                   current+=bytesRead;
               }
           }while(bytesRead>-1);
           
           bos.write(byteArray,0,current);
           bos.flush();
           
          System.out.println("File "+FILE_TO_RECIEVE
                  +" Downloaded ("+current+ " bytes read)");
          
           
        }
        finally{
            if(fos!=null){fos.close();}
            if(bos!=null){bos.close();}
            if(sock!=null){sock.close();}
        }
    }
    
}
