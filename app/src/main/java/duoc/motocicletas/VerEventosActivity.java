package duoc.motocicletas;

import android.content.Intent;
import android.nfc.Tag;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_eventos);

        Button btnIngresarEvento = (Button) findViewById(R.id.btnIngresarEvento);
        btnIngresarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarFormulario();
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void cargarFormulario(){
        Intent i = new Intent(VerEventosActivity.this, AgregarEventosActivity.class);
        startActivity(i);
        finish();
    }

    private void clickEnMapa(LatLng posicion){
        Intent nuevoActivity = new Intent(VerEventosActivity.this, AgregarEventosActivity.class);
        Bundle opcionesVista = new Bundle();

        opcionesVista.putDouble("latitud", posicion.latitude);
        opcionesVista.putDouble("longitud", posicion.longitude);

        //Pasamos todos los parametros al nuevo activity
        nuevoActivity.putExtras(opcionesVista);
        startActivity(nuevoActivity);
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
                clickEnMapa(posicionClick);
            }
        });

        ConexionWS conexionWS = new ConexionWS();
        conexionWS.configurar("ObtieneTodosLosEventos");
        ArrayList<Evento> eventos = conexionWS.traerTodos();

        //Antes del loop
        LatLng posicionEnMapa;
        LatLng primeraPosicion = new LatLng(0,0);
        String titulo;
        String descripcion;
        MarkerOptions opcionesPunto;

        //Log.d("myTag", String.valueOf(eventos.size()));
        int puntos = 0;
        for(Evento evento : eventos){
            opcionesPunto = new MarkerOptions();
            String[] coordenadas = evento.getDireccion().split(",");
            if(coordenadas.length < 1){
                double latitud = Double.parseDouble(coordenadas[0].trim());
                double longitud = Double.parseDouble(coordenadas[1].trim());

                posicionEnMapa = new LatLng(latitud, longitud);
                if(puntos == 0){
                    primeraPosicion = new LatLng(latitud, longitud);
                }
                titulo = evento.getTitulo();
                descripcion = evento.getDetalle();

                opcionesPunto.position(posicionEnMapa);
                opcionesPunto.title(titulo);
                opcionesPunto.snippet(descripcion);
                mMap.addMarker(opcionesPunto);
                puntos++;
            }
        }

        opcionesPunto = new MarkerOptions();
        posicionEnMapa = new LatLng(-33.462048, -70.609318);
        primeraPosicion = new LatLng(-33.462048, -70.609318);
        titulo = "Estadio Nacional";
        descripcion = "Estadio del campeon";

        opcionesPunto.position(posicionEnMapa);
        opcionesPunto.title(titulo);
        opcionesPunto.snippet(descripcion);
        mMap.addMarker(opcionesPunto);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(primeraPosicion));
    }
}
