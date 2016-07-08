package duoc.motocicletas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import duoc.clases.ConexionWS;
import duoc.clases.Evento;

public class VerEventosActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private llamadaAsyncWS mostrarPuntos = null;
    private LatLng coordenadasClick;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_eventos);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Toolbar myToolbar = (Toolbar) findViewById(R.id.barra_superior);

        mostrarPuntos = new llamadaAsyncWS();
        mostrarPuntos.execute((Void) null);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items_menu, menu);
        return true;
    }
    */

    public void clickEnMapa(LatLng posicion) {
        Intent nuevoActivity = new Intent(VerEventosActivity.this, AgregarEventosActivity.class);
        Bundle opcionesVista = new Bundle();

        opcionesVista.putDouble("latitud", posicion.latitude);
        opcionesVista.putDouble("longitud", posicion.longitude);

        //Pasamos todos los parametros al nuevo activity
        nuevoActivity.putExtras(opcionesVista);
        startActivity(nuevoActivity);
    }

    public void marcarPuntos(ArrayList<Evento> eventos) {
        LatLng posicionEnMapa;
        LatLng primeraPosicion = new LatLng(0, 0);
        String titulo;
        String descripcion;
        MarkerOptions opcionesPunto = new MarkerOptions();
        ;

        int puntos = 0;
        for (Evento evento : eventos) {
            String[] coordenadas = evento.getDireccion().split(",");
            if (coordenadas.length > 1) {
                double latitud = Double.parseDouble(coordenadas[0].trim());
                double longitud = Double.parseDouble(coordenadas[1].trim());
                posicionEnMapa = new LatLng(latitud, longitud);
                if (puntos == 0) {
                    primeraPosicion = new LatLng(latitud, longitud);
                }
                titulo = evento.getTitulo();
                descripcion = evento.getDetalle();

                opcionesPunto = new MarkerOptions();
                opcionesPunto.position(posicionEnMapa);
                opcionesPunto.title(titulo);
                opcionesPunto.snippet(descripcion);
                opcionesPunto.icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador_mapa));
                mMap.addMarker(opcionesPunto);
                puntos++;
            }
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(primeraPosicion, 10));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng posicionClick) {
                DialogoAceptar dialogo = new DialogoAceptar();
                coordenadasClick = posicionClick;
                dialogo.show(getFragmentManager(), "Dialogo_de_algo");
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });
    }

    private class claseHerencia extends AppCompatActivity {
        public void setToolbar() {

        }
    }

    private class llamadaAsyncWS extends AsyncTask<Void, Void, Void> {
        public ArrayList<Evento> eventos = new ArrayList<Evento>();

        private ConexionWS conexionWS = new ConexionWS();
        private String metodo = "ObtieneTodosLosEventos";

        public llamadaAsyncWS() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            this.conexionWS.configurar(this.metodo);
            this.eventos = this.conexionWS.traerTodos();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
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
