package com.example.final_project.Settings;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Urderlined extends AppCompatActivity {
    public void aesthetics_textView(TextView text_view, String text){//aesthetics_textView=estetica de textView
        SpannableString mitextoU = new SpannableString(text);
        mitextoU.setSpan(new UnderlineSpan(), 0, mitextoU.length(), 0);
        text_view.setText(mitextoU);
    }
}
