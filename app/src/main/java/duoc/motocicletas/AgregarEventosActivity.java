package duoc.motocicletas;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import duoc.clases.ConexionWS;

public class AgregarEventosActivity extends AppCompatActivity {
    private static final String TAG = "Activity";
    private AgregarEventoTask agregarEventoTask = null;

    private EditText txtTituloView;
    private EditText txtDireccionView;
    private DatePicker txtFechaCreacionView;
    private TimePicker txtHoraInicioView;
    private EditText txtDetalleView;
    private Button btnIngresarEventoView;

    private String fechaSeleccionada;
    private String horaSeleccionada;
    private double latitud;
    private double longitud;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public AgregarEventosActivity() {


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extraInfo = getIntent().getExtras();
        latitud = extraInfo.getDouble("latitud");
        longitud = extraInfo.getDouble("longitud");

        setContentView(R.layout.activity_agregar_evento);

        this.txtTituloView = (EditText) findViewById(R.id.txtTitulo);
        this.txtDireccionView = (EditText) findViewById(R.id.txtDireccion);
        this.txtFechaCreacionView = (DatePicker) findViewById(R.id.txtFechaCreacion);
        this.txtHoraInicioView = (TimePicker) findViewById(R.id.txtHoraInicio);
        this.txtDetalleView = (EditText) findViewById(R.id.txtDetalle);
        this.btnIngresarEventoView = (Button) findViewById(R.id.btnIngresarEvento);

        this.txtDireccionView.setText(latitud + ", " + longitud);
        this.txtDireccionView.setVisibility(View.INVISIBLE);

        btnIngresarEventoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //redireccionar();
                validarDatos();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void validarDatos() {
        boolean error = false;
        View focusView = null;

        if (this.agregarEventoTask != null) {
            return;
        }

        this.txtTituloView.setError(null);
        this.txtDireccionView.setError(null);
        this.txtDetalleView.setError(null);
        //this.txtFechaCreacionView.setError(null);

        String titulo = this.txtTituloView.getText().toString();
        String direccion = this.txtDireccionView.getText().toString();
        String detalle = this.txtDetalleView.getText().toString();

        if (detalle.isEmpty()) {
            this.txtDetalleView.setError("Debe ingresar detalle");
            focusView = this.txtDetalleView;
            error = true;
        }
        if (direccion.isEmpty()) {
            this.txtDireccionView.setError("Debe ingresar dirección");
            focusView = this.txtDireccionView;
            error = true;
        }
        if (titulo.isEmpty()) {
            this.txtTituloView.setError("Debe ingresar titulo");
            focusView = this.txtTituloView;
            error = true;
        }

        if (error) {
            focusView.requestFocus();
        } else {
            formatearDatos();
        }
        //validar fecha y hora
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void formatearDatos() {
        if (this.agregarEventoTask != null) {
            return;
        }

        String titulo = txtTituloView.getText().toString();
        String direccion = txtDireccionView.getText().toString();
        String detalle = txtDetalleView.getText().toString();

        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
        int dia = txtFechaCreacionView.getDayOfMonth();
        int mes = txtFechaCreacionView.getMonth();
        int anno = txtFechaCreacionView.getYear();
        GregorianCalendar fecha = new GregorianCalendar(anno, mes, dia);
        formato.setCalendar(fecha);
        String fechaInicio = formato.format(fecha.getTime());

        formato = new SimpleDateFormat("HH:mm");
        Integer hora = txtHoraInicioView.getCurrentHour();
        Integer minuto = txtHoraInicioView.getCurrentMinute();
        fecha = new GregorianCalendar(0, 0, 0, hora, minuto);
        formato.setCalendar(fecha);
        String horaInicio = formato.format(fecha.getTime());

        this.agregarEventoTask = new AgregarEventoTask(titulo, direccion, fechaInicio, horaInicio, detalle);
        this.agregarEventoTask.execute((Void) null);
    }

    public void redireccionar() {
        Intent intent = new Intent(AgregarEventosActivity.this, VerEventosActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "AgregarEventos Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://duoc.motocicletas/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "AgregarEventos Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://duoc.motocicletas/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public class AgregarEventoTask extends AsyncTask<Void, Void, Boolean> {
        private ConexionWS conexionWS = new ConexionWS();
        private String txtTitulo;
        private String txtDireccion;
        private String txtFechaInicio;
        private String txtHoraInicio;
        private String txtDetalle;

        public AgregarEventoTask(String titulo, String direccion, String fechaInicio, String horaInicio, String detalle) {
            this.txtTitulo = titulo;
            this.txtDireccion = direccion;
            this.txtFechaInicio = fechaInicio;
            this.txtHoraInicio = horaInicio;
            this.txtDetalle = detalle;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean insertado = false;
            conexionWS.configurar("CreaEvento");
            conexionWS.getRequest().addProperty("Titulo", this.txtTitulo);
            conexionWS.getRequest().addProperty("Detalle", this.txtDetalle);
            conexionWS.getRequest().addProperty("Direccion", this.txtDireccion);
            conexionWS.getRequest().addProperty("FechaInicion", this.txtFechaInicio);
            conexionWS.getRequest().addProperty("HrsInicion", this.txtHoraInicio);

            String respuesta = conexionWS.llamarSimple();
            if (respuesta.equals("1")) {
                insertado = true;
            }
            return insertado;
        }

        @Override
        protected void onPostExecute(Boolean insertado) {
            agregarEventoTask = null;
            if (insertado) {
                redireccionar();
            } else {
                txtTituloView.setError("Error al guardar");
                txtDetalleView.setError("Error al guardar");
                txtDireccionView.setError("Error al guardar");
                txtTituloView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
