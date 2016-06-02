package duoc.clases;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class ConexionWS{
    private String contenedor = "Activity";
    private String NAMESPACE = "http://suarpe.com/";
    private String URL="http://201.236.131.203:8091/ServicioClientes.asmx?op=";
    private String METHOD_NAME;
    private String SOAP_ACTION = "http://suarpe.com/";
    private SoapObject request = null;

    public ConexionWS(){
    }

    //configuramos las variables para llamar al ws, según el nombre del método
    public void configurar(String metodo){
        this.METHOD_NAME = metodo;
        this.URL = this.URL + metodo;
        this.SOAP_ACTION += metodo;
        this.request = new SoapObject(this.NAMESPACE, this.METHOD_NAME);
    }

    //enviamos el request, para asignar los atributos necesarios al sw
    public SoapObject getRequest(){
        return this.request;
    }

    //Llamada simple, sólo retorna el primer parametro del request,
    //preferible sólo para llamadas de guardado que retorne un solo valor.
    public String llamarSimple(){
        //por defecto 0 -> accion no realizada
        String respuesta = "0";
        try{
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setOutputSoapObject(this.request);

            HttpTransportSE transporte = new HttpTransportSE(URL);
            //transporte.debug = true;
            transporte.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
            transporte.call(this.SOAP_ACTION, envelope);

            SoapObject response = (SoapObject) envelope.bodyIn;
            if(response != null){
                respuesta = response.getProperty(0).toString();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return respuesta;
    }
}
