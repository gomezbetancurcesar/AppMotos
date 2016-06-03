package duoc.motocicletas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EventosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        Button btnIrEventoView = (Button) findViewById(R.id.irAgregarEvento);
        btnIrEventoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent redireccionar = new Intent(EventosActivity.this, AgregarEventosActivity.class);
                startActivity(redireccionar);
            }
        });
    }

}
