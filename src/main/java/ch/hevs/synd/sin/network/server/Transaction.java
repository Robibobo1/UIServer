package ch.hevs.synd.sin.network.server;

import ch.hevs.synd.sin.sensor.MeasurementType;
import ch.hevs.synd.sin.sensor.Sensor;
import ch.hevs.utils.Utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;



public class Transaction {

    private final InputStream _in;
    private final OutputStream _out;

    private final Sensor _uSensor;
    private final Sensor _iSensor;


    /* **************************************************************************************************************** */
    /*                                                                                                                  */
    /* Constructors                                                                                                     */
    /*                                                                                                                  */
    /* **************************************************************************************************************** */
    /**
     * Simple constructor
     * @param in		The input stream to read from
     * @param out		The output stream to write to
     * @param uSensor	Pointer to the Voltage sensor
     * @param iSensor	Pointer to the Ampere sensor
     */
    public Transaction(InputStream in, OutputStream out, Sensor uSensor, Sensor iSensor) {
        _in = in;
        _out = out;
        _uSensor = uSensor;
        _iSensor = iSensor;
    }


    /* **************************************************************************************************************** */
    /*                                                                                                                  */
    /* Public methods                                                                                                   */
    /*                                                                                                                  */
    /* **************************************************************************************************************** */
    public boolean processTransaction() throws IOException {
        String rxData = new String(Utility.readLine(_in));
        if(rxData.equalsIgnoreCase("getu"))
        {
            double vValue = _uSensor.getMeasurement().getValue();
            _out.write(Double.toString(vValue).getBytes());
            _out.write('\n');
            return true;
        }
        else if(rxData.equalsIgnoreCase("geti"))
        {
            double iValue = _iSensor.getMeasurement().getValue();
            _out.write(Double.toString(iValue).getBytes());
            _out.write('\n');
            return true;
        }
        else if(rxData.equalsIgnoreCase("stop"))
        {
            _uSensor.shutdown();
            _iSensor.shutdown();
            return false;
        }
        else
        {
            _out.write("Not a valid command\n".getBytes());
            return true;
        }
    }


    /* **************************************************************************************************************** */
    /*                                                                                                                  */
    /* MAIN -- MAIN -- MAIN -- MAIN -- MAIN -- MAIN -- MAIN -- MAIN -- MAIN -- MAIN -- MAIN -- MAIN -- MAIN -- MAIN --  */
    /*                                                                                                                  */
    /* **************************************************************************************************************** */
    public static void main(String[] args) {
        Sensor sU = new Sensor(MeasurementType.Voltage,0);
        Sensor sI = new Sensor(MeasurementType.Current,10);
        try{

            InputStream iS = System.in;
            OutputStream oS = System.out;
            Transaction t = new Transaction(iS,oS,sU,sI);
            while(t.processTransaction());

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Server can not be reached");
            System.exit(-1);
        }
        System.exit(200);
    }
}
