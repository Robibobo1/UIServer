package stream;

import ch.hevs.synd.sin.network.server.Transaction;
import ch.hevs.synd.sin.sensor.MeasurementType;
import ch.hevs.synd.sin.sensor.Sensor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ConnectionHandler1 {

    Socket socket;
    Sensor sV;
    Sensor sI;

    ConnectionHandler1(Socket s,Sensor v,Sensor i)
    {
        socket = s;
        sV = v;
        sI = i;
    }

    boolean process() throws IOException {
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();
        Transaction t = new Transaction(in,out,sV,sI);
        return t.processTransaction();
    }
    public static void main(String[] args) throws IOException {
        Sensor sU = new Sensor(MeasurementType.Voltage,0);
        Sensor sI = new Sensor(MeasurementType.Current,180);
        UIServer1 mySocket = new UIServer1(1502);
        while(true)
        {
            ConnectionHandler1 client1 = new ConnectionHandler1(mySocket.accept(),sU,sI);
            while(client1.process());
        }
    }
}
