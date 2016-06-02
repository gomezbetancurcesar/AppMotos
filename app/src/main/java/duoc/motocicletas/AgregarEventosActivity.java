package duoc.motocicletas;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_evento);

        this.txtTituloView = (EditText) findViewById(R.id.txtTitulo);
        this.txtDireccionView = (EditText) findViewById(R.id.txtDireccion);
        this.txtFechaCreacionView = (DatePicker) findViewById(R.id.txtFechaCreacion);
        this.txtHoraInicioView = (TimePicker) findViewById(R.id.txtHoraInicio);
        this.txtDetalleView = (EditText) findViewById(R.id.txtDetalle);
        this.btnIngresarEventoView = (Button) findViewById(R.id.btnIngresarEvento);


    }

    public class AgregarEventoTask extends AsyncTask<Void, Void, Boolean>{
        private ConexionWS conexionWS = new ConexionWS();
        private String txtTitulo;
        private String txtDireccion;
        private String txtFechaInicio;
        private String txtHoraInicio;
        private String txtDetalle;

        public AgregarEventoTask(String titulo, String direccion, String fechaInicio, String horaInicio, String detalle){
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
            if(respuesta.equals("1")){
                insertado = true;
            }

            return insertado;
        }

        @Override
        protected void onPostExecute(Boolean insertado){
            agregarEventoTask = null;
            if(insertado){

            }else{

            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
