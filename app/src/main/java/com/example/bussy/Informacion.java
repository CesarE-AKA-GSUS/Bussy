package com.example.bussy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Informacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);

        TextView textView = findViewById(R.id.txtQuienesSomos);
        textView.setText(getClickableSpan());
        textView.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
    }

    private SpannableString getClickableSpan() {

        String texto = "Quienes somos?";
        int cuentafinal;
        SpannableString spannableString = new SpannableString(texto);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // Acción al hacer clic
                Uri uri = Uri.parse("https://www.google.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        };
        cuentafinal= texto.length();
        spannableString.setSpan(clickableSpan, 0, cuentafinal, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}