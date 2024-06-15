package com.example.dietideals24;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dietideals24.dto.AstaDTO;
import com.example.dietideals24.dto.UtenteDTO;

import java.io.Serializable;
import java.util.List;

public class DettagliAstaActivity extends AppCompatActivity {

    private EditText etTitle, etDescription, etOffer;
    private TextView tvCategoryValue, tvTypeValue, tvCreatorValue, tvPriceValue, tvDecrementValue, tvTimerValue;
    private Button btnSubmitOffer;
    private ImageView ivFoto;
    private ImageButton btnBack;
    private Bitmap bitmap;
    private AstaDTO asta;
    private List<AstaDTO> listaAste;
    private String criterioRicerca;
    private UtenteDTO utente_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettagli_asta);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        asta = (AstaDTO) getIntent().getSerializableExtra("asta");
        listaAste = (List<AstaDTO>) getIntent().getSerializableExtra("listaAste");
        if (getIntent().getStringExtra("criterioRicerca") != null)
            criterioRicerca = getIntent().getStringExtra("criterioRicerca");
        utente_home = (UtenteDTO) getIntent().getSerializableExtra("utente");

        ivFoto = findViewById(R.id.imageView_Foto);
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        tvCategoryValue = findViewById(R.id.tvCategoryValue);
        tvTypeValue = findViewById(R.id.tvTypeValue);
        tvCreatorValue = findViewById(R.id.tvCreatorValue);
        tvPriceValue = findViewById(R.id.tvPriceValue);
        tvDecrementValue = findViewById(R.id.tvDecrementValue);
        tvTimerValue = findViewById(R.id.tvTimerValue);
        etOffer = findViewById(R.id.etOffer);
        btnSubmitOffer = findViewById(R.id.btnSubmitOffer);
        btnBack = findViewById(R.id.back_button);

        byte[] fotoBytes = asta.getFoto();
        if (fotoBytes != null) {
            bitmap = BitmapFactory.decodeByteArray(fotoBytes, 0, fotoBytes.length);
            ivFoto.setImageBitmap(bitmap);
        }

        etTitle.setText(asta.getNome());
        etDescription.setText(asta.getDescrizione());
        tvCategoryValue.setText(asta.getCategoria().toString());
        tvTypeValue.setText("");
        tvCreatorValue.setText("");
        tvPriceValue.setText("");
        tvDecrementValue.setText("");
        tvTimerValue.setText("");

        adjustEditTextWidth(etTitle);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityRisultatiRicerca();
                finish();
            }
        });

        btnSubmitOffer.setOnClickListener(v -> {
            String offer = etOffer.getText().toString();
        });
    }

    private void openActivityRisultatiRicerca() {
        Intent intent = new Intent(this, RisultatiRicercaActivity.class);
        intent.putExtra("listaAste", (Serializable) listaAste);
        intent.putExtra("criterioRicerca", criterioRicerca);
        intent.putExtra("utente", utente_home);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        openActivityRisultatiRicerca();
        finish();
    }

    private void adjustEditTextWidth(EditText editText) {
        String text = editText.getText().toString();
        Paint paint = editText.getPaint();
        float textWidth = paint.measureText(text);

        int padding = editText.getPaddingLeft() + editText.getPaddingRight();
        editText.setWidth((int) (textWidth + padding));
    }
}

