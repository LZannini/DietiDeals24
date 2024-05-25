package com.example.dietideals24;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;


public class ProfiloActivity extends AppCompatActivity {

    private ImageView menuButton;
    private EditText emailEditText;
    private EditText bioEditText;
    private EditText webSiteEditText;
    private EditText countryEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);

        menuButton = findViewById(R.id.icona_menu);
        emailEditText = findViewById(R.id.email);
        bioEditText = findViewById(R.id.shortBio);
        webSiteEditText = findViewById(R.id.sito);
        countryEditText = findViewById(R.id.paese);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenuProfilo(v);
            }
        });
    }

    private void openMenuProfilo(View view) {
        PopupMenu popup = new PopupMenu(this, view);

        popup.getMenuInflater().inflate(R.menu.menu_profilo, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_edit_profile:
                        emailEditText.setEnabled(true);
                        bioEditText.setEnabled(true);
                        webSiteEditText.setEnabled(true);
                        countryEditText.setEnabled(true);
                        return true;
                    case R.id.action_change_password:
                        openActivityModificaPassword();
                        return true;
                    case R.id.action_switch_account:
                        openActivitySceltaAccount();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();;
    }

    private void openActivityModificaPassword() {
        Intent intentR = new Intent(this, ModificaPasswordActivity.class);
        startActivity(intentR);
    }

    private void openActivitySceltaAccount() {
        Intent intentR = new Intent(this, SceltaAccountActivity.class);
        startActivity(intentR);
    }
}