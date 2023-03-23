package com.example.languagetranslator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

public class MainActivity extends AppCompatActivity {
    private Spinner fromSpinner,toSpinner;
    private TextInputEditText textInputEditText;
    private MaterialButton materialButton;
    private TextView textView;
    String [] fromLanguages = {"From","English","Hindi","Sanskrit","Bengali","Marathi","Tamil","Telugu","Belarusian","Bulgarian","Czech","Afrikaans"};
    String [] toLanguages = {"to","English","Hindi","Sanskrit","Bengali","Marathi","Tamil","Telugu","Belarusian","Bulgarian","Czech","Afrikaans"};
    int fromLanguageCode=0,toLanguageCode=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fromSpinner = findViewById(R.id.spinnerFrom);
        toSpinner = findViewById(R.id.spinnerTo);
        textInputEditText = findViewById(R.id.inputText);
        materialButton = findViewById(R.id.materialButton);
        textView = findViewById(R.id.text);
        ArrayAdapter fromAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,fromLanguages);
        fromSpinner.setAdapter(fromAdapter);
        ArrayAdapter toAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,toLanguages);
        toSpinner.setAdapter(toAdapter);
        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                fromLanguageCode = getLanguageCode(fromLanguages[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                toLanguageCode = getLanguageCode(toLanguages[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textInputEditText.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this,"Write a text first",Toast.LENGTH_LONG).show();
                } else if(fromLanguageCode == 0){
                    Toast.makeText(MainActivity.this,"Select source language first",Toast.LENGTH_LONG).show();
                } else if(toLanguageCode == 0){
                    Toast.makeText(MainActivity.this,"Select target language first",Toast.LENGTH_LONG).show();
                } else {
                    textView.setText("");
                     translateText(fromLanguageCode,toLanguageCode,textInputEditText.getText().toString());
                }

            }
        });
    }
    private void translateText(int fromLanguageCode,int toLanguageCode,String source){
        textView.setText("Downloading Model...");
        FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                .setSourceLanguage(fromLanguageCode)
                .setTargetLanguage(toLanguageCode)
                .build();
        FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
        FirebaseModelDownloadConditions firebaseModelDownloadConditions = new FirebaseModelDownloadConditions.Builder().build();
        translator.downloadModelIfNeeded(firebaseModelDownloadConditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                textView.setText("Translating");
                translator.translate(source).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        textView.setText(s);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,"Failed to translate",Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Failed to download model or check your network connection",Toast.LENGTH_LONG).show();
            }
        });
    }
    private int getLanguageCode(String language){
        int languageCode;
        switch (language){
            case "English":
                languageCode = FirebaseTranslateLanguage.EN;
                break;
            case "Hindi":
                languageCode = FirebaseTranslateLanguage.HI;
                break;
            case "Sanskrit":
                languageCode = FirebaseTranslateLanguage.SK;
                break;
            case "Bengali":
                languageCode = FirebaseTranslateLanguage.BN;
                break;
            case "Marathi":
                languageCode = FirebaseTranslateLanguage.MR;
                break;
            case "Tamil":
                languageCode = FirebaseTranslateLanguage.TA;
                break;
            case "Telugu":
                languageCode = FirebaseTranslateLanguage.TL;
                break;
            case "Belarusian":
                languageCode = FirebaseTranslateLanguage.BE;
                break;
            case "Bulgarian":
                languageCode = FirebaseTranslateLanguage.BG;
                break;
            case "Czech":
                languageCode = FirebaseTranslateLanguage.CS;
                break;
            case "Afrikaans":
                languageCode = FirebaseTranslateLanguage.AF;
                break;
            default:
                languageCode = 0;
        }
        return languageCode;
    }
}