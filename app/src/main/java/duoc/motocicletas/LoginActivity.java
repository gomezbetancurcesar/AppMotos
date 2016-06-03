package duoc.motocicletas;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import duoc.clases.ConexionWS;

public class LoginActivity extends AppCompatActivity{
    private static final String TAG = "LoginActivity";
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText txtUsuarioView;
    private EditText txtPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        txtUsuarioView = (EditText) findViewById(R.id.txtUsuario);

        txtPasswordView = (EditText) findViewById(R.id.txtPassword);
        /*
        txtPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
*/
        Button btnIngresar = (Button) findViewById(R.id.btnIngresar);
        btnIngresar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        txtUsuarioView.setError(null);
        txtPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String usuario = txtUsuarioView.getText().toString();
        String password = txtPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (password.isEmpty()){
            txtPasswordView.setError("Debe ingresar una contraseña");
            focusView = txtPasswordView;
            cancel = true;
        }else if(!isPasswordValid(password)) {
            txtPasswordView.setError("Contraseña inválida");
            focusView = txtPasswordView;
            cancel = true;
        }

        // Check for a valid usuario address.
        if (usuario.isEmpty()) {
            txtUsuarioView.setError("Debe ingresar usuario");
            focusView = txtUsuarioView;
            cancel = true;
        }

        if (cancel) {
            //Error en el formulario
            focusView.requestFocus();
        } else {
            //Continuamos hacia la llamada del WS
            showProgress(true);
            mAuthTask = new UserLoginTask(usuario, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 2;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    //Se declara una tarea asincrona para la validacion del login
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        private final String txtUsuario;
        private final String txtPassword;

        private ConexionWS conexionWS = new ConexionWS();
        private String metodo = "LoginUsuario";

        UserLoginTask(String email, String password) {
            txtUsuario = email;
            txtPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean logeado = false;

            conexionWS.configurar(this.metodo);
            conexionWS.getRequest().addProperty("user", this.txtUsuario);
            conexionWS.getRequest().addProperty("password", this.txtPassword);
            String esUsuario = conexionWS.llamarSimple();

            if(esUsuario.equals("1")){
                logeado = true;
            }
            return logeado;
        }

        @Override
        protected void onPostExecute(final Boolean logeado) {
            mAuthTask = null;
            showProgress(false);
            if (logeado){
                Intent i = new Intent(LoginActivity.this, AgregarEventosActivity.class);
                startActivity(i);
                finish();
            }else{
                txtPasswordView.setError("Contraseña incorrecta");
                txtPasswordView.requestFocus();
                showProgress(false);
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}