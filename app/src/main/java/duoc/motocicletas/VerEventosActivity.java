package duoc.motocicletas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import duoc.clases.ConexionWS;
import duoc.clases.Evento;

public class VerEventosActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private llamadaAsyncWS mostrarPuntos = null;
    private LatLng coordenadasClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_eventos);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mostrarPuntos = new llamadaAsyncWS();
        mostrarPuntos.execute((Void) null);
    }

    public void clickEnMapa(LatLng posicion){
        Intent nuevoActivity = new Intent(VerEventosActivity.this, AgregarEventosActivity.class);
        Bundle opcionesVista = new Bundle();

        opcionesVista.putDouble("latitud", posicion.latitude);
        opcionesVista.putDouble("longitud", posicion.longitude);

        //Pasamos todos los parametros al nuevo activity
        nuevoActivity.putExtras(opcionesVista);
        startActivity(nuevoActivity);
    }

    public void marcarPuntos(ArrayList<Evento> eventos){
        LatLng posicionEnMapa;
        LatLng primeraPosicion = new LatLng(0,0);
        String titulo;
        String descripcion;
        MarkerOptions opcionesPunto = new MarkerOptions();;

        int puntos = 0;
        for(Evento evento : eventos){
            opcionesPunto = new MarkerOptions();
            String[] coordenadas = evento.getDireccion().split(",");
            if(coordenadas.length > 1){
                double latitud = Double.parseDouble(coordenadas[0].trim());
                double longitud = Double.parseDouble(coordenadas[1].trim());
                posicionEnMapa = new LatLng(latitud, longitud);
                if(puntos == 0){
                    primeraPosicion = new LatLng(latitud, longitud);
                }
                titulo = evento.getTitulo();
                descripcion = evento.getDetalle();

                opcionesPunto = new MarkerOptions();
                opcionesPunto.position(posicionEnMapa);
                opcionesPunto.title(titulo);
                opcionesPunto.snippet(descripcion);
                mMap.addMarker(opcionesPunto);
                puntos++;
            }
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(primeraPosicion, 15));
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng posicionClick) {
                DialogoAceptar dialogo = new DialogoAceptar();
                coordenadasClick = posicionClick;
                dialogo.show(getFragmentManager(),"Dialogo_de_algo");
            }
        });
    }

    private class llamadaAsyncWS extends AsyncTask<Void, Void, Void> {
        public ArrayList<Evento> eventos = new ArrayList<Evento>();

        private ConexionWS conexionWS = new ConexionWS();
        private String metodo = "ObtieneTodosLosEventos";

        public llamadaAsyncWS(){
        }

        @Override
        protected Void doInBackground(Void... params) {
            this.conexionWS.configurar(this.metodo);
            this.eventos = this.conexionWS.traerTodos();
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            marcarPuntos(this.eventos);
            Log.i("", "onPostExecute");
        }
    }

    public class DialogoAceptar extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("¿Desea crear un evento aquí?")
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            clickEnMapa(coordenadasClick);
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}
