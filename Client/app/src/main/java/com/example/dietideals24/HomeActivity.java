package com.example.dietideals24;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class HomeActivity extends AppCompatActivity {

    private LinearLayout buttonCrea;
    private LinearLayout buttonCerca;
    private LinearLayout buttonProfilo;
    private LinearLayout buttonDisconnetti;
    private AlertDialog.Builder builder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        buttonCrea = findViewById(R.id.button_crea);
        buttonCerca = findViewById(R.id.button_cerca);
        buttonProfilo = findViewById(R.id.button_profilo);
        buttonDisconnetti = findViewById(R.id.button_disconnetti);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        buttonCrea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityCreaAsta();
            }
        });

        buttonCerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityCercaAsta();
            }
        });

        buttonProfilo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityProfilo();
            }
        });

        buttonDisconnetti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setMessage("Sei sicuro di voler uscire?")
                        .setCancelable(true)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Context ctx = getApplicationContext();
                                PackageManager pm = ctx.getPackageManager();
                                Intent intent = pm.getLaunchIntentForPackage(ctx.getPackageName());
                                Intent mainIntent = Intent.makeRestartActivityTask(intent.getComponent());
                                ctx.startActivity(mainIntent);
                                Runtime.getRuntime().exit(0);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) { }
                        })
                        .show();
            }
        });
    }
    public void openActivityCreaAsta() {
        Intent intentR = new Intent(this, CreaAstaActivity.class);
        startActivity(intentR);
    }

    public void openActivityCercaAsta() {
        Intent intentR = new Intent(this, CercaAstaActivity.class);
        startActivity(intentR);
    }

    public void openActivityProfilo() {
        Intent intentR = new Intent(this, ProfiloActivity.class);
        startActivity(intentR);
    }

}