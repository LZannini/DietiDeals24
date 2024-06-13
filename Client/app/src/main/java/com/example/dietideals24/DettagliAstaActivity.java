package com.example.dietideals24;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dietideals24.dto.AstaDTO;

public class DettagliAstaActivity extends AppCompatActivity {

    private EditText etTitle, etDescription, etOffer;
    private TextView tvCategoryValue, tvTypeValue, tvCreatorValue, tvPriceValue, tvDecrementValue, tvTimerValue;
    private Button btnSubmitOffer;
    private ImageButton btnBack;
    private AstaDTO asta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettagli_asta);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        asta = (AstaDTO) getIntent().getSerializableExtra("asta");

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

        tvCategoryValue.setText(asta.getCategoria().toString());
        tvTypeValue.setText("");
        tvCreatorValue.setText("");
        tvPriceValue.setText("");
        tvDecrementValue.setText("");
        tvTimerValue.setText("");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSubmitOffer.setOnClickListener(v -> {
            String offer = etOffer.getText().toString();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

