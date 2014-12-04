package com.example.kevinlutgert.fooi;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView.OnEditorActionListener;

import java.text.DecimalFormat;


public class MainActivity extends Activity implements OnEditorActionListener, OnClickListener {
    private EditText rekeningBedragEditText;
    private TextView procentTextView;
    private Button procentPlusButton;
    private Button procentMinButton;
    private TextView fooiTextView;
    private TextView totaalTextView;
    private double fooiPercentage = 15.0;
    private SharedPreferences bewaardeWaardes;
    private RadioButton radioBoptie1;
    private RadioButton radioBoptie2;
    private RadioButton radioBoptie3;

    private String rekeningBedragString = "";
    private int fooiPercentageInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        bewaardeWaardes = getSharedPreferences("SavedValues", MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rekeningBedragEditText = (EditText) findViewById(R.id.rekeningBedragEditText);
        procentTextView = (TextView) findViewById(R.id.ProcentView);
        procentPlusButton = (Button) findViewById(R.id.procent_PlusButton);
        procentMinButton = (Button) findViewById(R.id.procent_MinButton);
        fooiTextView = (TextView) findViewById(R.id.ProcentTotaal);
        totaalTextView = (TextView) findViewById(R.id.Totaal);
        radioBoptie1  =(RadioButton) findViewById(R.id.optie1RadioButton);
        radioBoptie2 = (RadioButton) findViewById(R.id.optie2RadioButton);
        radioBoptie3 = (RadioButton) findViewById(R.id.optie3RadioButton);

        rekeningBedragEditText.setOnEditorActionListener(this);
        procentMinButton.setOnClickListener(this);
        procentPlusButton.setOnClickListener(this);

    }

    void setChecked()
    radioBoption1.setChecked(true);

boolean isChecked()
    if (optie2RadioButton.isChecked) {
        //Doe iets
    }
    @Override
    public void onPause() {
        // bewaar de variabelen
        SharedPreferences.Editor editor = bewaardeWaardes.edit();
        editor.putString("rekeningBedragString", rekeningBedragString);
        fooiPercentageInt = (int) fooiPercentage;
        editor.putInt("fooiPercentage", fooiPercentageInt);
        editor.commit();

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        // ken de waardes toe aan de variabelen
        rekeningBedragString = bewaardeWaardes.getString("rekeningBedragString", "");
        fooiPercentage = (double) bewaardeWaardes.getInt("fooiPercentage", 15);

        // zorg dat het bedrag in het scherm wordt vertoond
        rekeningBedragEditText.setText(rekeningBedragString);

        // berekenEnToon
        berekenEnToon();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.procent_MinButton:
                fooiPercentage--;
                berekenEnToon();
                break;
            case R.id.procent_PlusButton:
                fooiPercentage++;
                berekenEnToon();
                break;
        }

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            berekenEnToon();
        }
        return false;
    }


    private void berekenEnToon() {
        String rekeningBedragS = rekeningBedragEditText.getText().toString();
        double rekeningBedrag;
        if (rekeningBedragS.equals("")) {
            rekeningBedrag = 0.0;
        } else {
            rekeningBedrag = Double.parseDouble(rekeningBedragS);
        }

        //Bereken fooi en totaal//

        double fooibedrag = rekeningBedrag * (fooiPercentage / 100);
        double totaalbedrag = rekeningBedrag + fooibedrag;

        DecimalFormat df = new DecimalFormat("€ ###,###.00");
        fooiTextView.setText(df.format(fooibedrag));
        totaalTextView.setText(df.format(totaalbedrag));
        procentTextView.setText("" + (int) fooiPercentage + "%");
    }



}


