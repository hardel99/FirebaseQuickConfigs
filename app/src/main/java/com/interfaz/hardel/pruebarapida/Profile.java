package com.interfaz.hardel.pruebarapida;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private TextView mail, uID;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mail = findViewById(R.id.email_display);
        uID = findViewById(R.id.uid);
        logout = findViewById(R.id.logout);
        user = auth.getInstance().getCurrentUser();

        if (user != null) {
            String email = user.getEmail();
            String Uid = user.getDisplayName();

            mail.setText(email);
            uID.setText(Uid);
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user!= null) {
                    auth.signOut();
                }
            }
        });
    }
}
