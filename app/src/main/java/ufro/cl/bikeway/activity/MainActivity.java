package ufro.cl.bikeway.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.activeandroid.ActiveAndroid;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import ufro.cl.bikeway.R;
import ufro.cl.bikeway.utlis.UserSessionManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient apiClient;
    private static final String TAG = "MainActivity";
    private UserSessionManager sessionManager;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onStart() {
        super.onStart();
        if (sessionManager != null && sessionManager.isUserLoggedIn()) {
            Intent home = new Intent(this, ListaRutas.class);
            startActivity(home);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        sessionManager = new UserSessionManager(getApplicationContext());
        if (sessionManager.isUserLoggedIn()) {
            Log.w(TAG, "Usuario logeado");
        }

        findViewById(R.id.sign_in_button).setOnClickListener(this);
        ActiveAndroid.initialize(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
                startActivityForResult(intent, RC_SIGN_IN);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                String name = result.getSignInAccount().getDisplayName();
                String email = result.getSignInAccount().getEmail();

                this.sessionManager.createSession(name, email);
                Intent homeIntent = new Intent(this, ListaRutas.class);
                startActivity(homeIntent);
            }
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "Error en la conexión");
    }
}
