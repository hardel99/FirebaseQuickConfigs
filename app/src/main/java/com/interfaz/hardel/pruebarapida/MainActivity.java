package com.interfaz.hardel.pruebarapida;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private GoogleSignInClient googleClient;
    private EditText email, pass;
    private Button signin, login, googleSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.email_display);
        pass = findViewById(R.id.password);

        signin = findViewById(R.id.signin);
        login = findViewById(R.id.login);
        googleSignIn = findViewById(R.id.googleSign);

        auth = FirebaseAuth.getInstance();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == signin) {
                    register();
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == login) {
                    login();
                }
            }
        });

        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == googleSignIn) {
                    signIn();
                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleClient = GoogleSignIn.getClient(this, gso);
    }

    public void register() {
        String mail = email.getText().toString();
        String password = pass.getText().toString();

        if(TextUtils.isEmpty(mail) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please complete the field to make an action first", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                try {
                    if(task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "WUUUUU, si se pudo registrar", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                    } else{
                        Toast.makeText(MainActivity.this, "por bocon me va mal :(", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void login() {
        String mail = email.getText().toString();
        String password = pass.getText().toString();

        if(TextUtils.isEmpty(mail) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please complete the field to make an action first", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    currentUser = auth.getCurrentUser();
                    finish();
                    startActivity(new Intent(MainActivity.this, Profile.class));
                } else {
                    Toast.makeText(MainActivity.this, "couldn't login", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //Desde aqui son puras cosas del google nada mas xD

    private void signIn() {
        Intent signInIntent = googleClient.getSignInIntent();
        startActivityForResult(signInIntent, 101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(MainActivity.this, "Google malo >:(", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    currentUser = auth.getCurrentUser();
                    finish();
                    startActivity(new Intent(MainActivity.this, Profile.class));
                } else {
                    Toast.makeText(MainActivity.this, "Google malo, no se deja >:(", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
