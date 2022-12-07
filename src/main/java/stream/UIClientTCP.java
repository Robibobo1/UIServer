package stream;

import ch.hevs.synd.sin.UIConstants;
import ch.hevs.utils.Utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketOptions;

public class UIClientTCP {
    int n;
    int z;
    String serverName;
    int portNumber;

    UIClientTCP(String newServerName,int newPortNumber)
    {
        n=z=1;
        serverName = newServerName;
        portNumber = newPortNumber;
    }

    enum DataType{
        Current,
        Voltage
    };


    void getData(int n ,int z,DataType type){
        String message = "";
        switch(type)
        {
            case Current:
                message = "GetI\r\n";
                break;
            case Voltage:
                message = "GetU\r\n";
        }

        byte[] txData = message.getBytes();
        try{
            Socket socket = new Socket(serverName,portNumber);
            OutputStream outStream = socket.getOutputStream();
            InputStream inStream = socket.getInputStream();
            for (int i = 0; i < n; i++)
            {
                Utility.writeLine(outStream,txData);
                byte[] rxData = Utility.readLine(inStream);
                String rxString = new String(rxData);
                System.out.println(rxString);
                Utility.waitSomeTime(z);
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Server can not be reached");
            System.exit(-1);
        }
    }


    public static void main(String[] args) {
    UIClientTCP MyTcpClient = new UIClientTCP("sdi.hevs.ch", UIConstants.UI_SERVER_PORT);
    MyTcpClient.getData(30,100,DataType.Voltage);
    }
}
