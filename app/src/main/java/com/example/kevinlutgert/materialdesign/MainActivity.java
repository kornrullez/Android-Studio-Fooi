package com.example.kevinlutgert.materialdesign;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView.OnEditorActionListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

import java.text.DecimalFormat;


public class MainActivity extends Activity implements OnEditorActionListener, OnCheckedChangeListener, OnSeekBarChangeListener {
    private EditText rekeningBedragEditText;
    private TextView procentTextView;
    private Button procentPlusButton;
    private Button procentMinButton;
    private TextView fooiTextView;
    private TextView totaalTextView;
    private double fooiPercentage = 15.0;
    private SharedPreferences bewaardeWaardes;
    private RadioGroup afrondRadioGroup;
    private SeekBar  seekbar;
    private String rekeningBedragString = "";
    private int fooiPercentageInt;

    private final int AFROND_GEEN = 0;
    private final int AFROND_FOOI = 1;
    private final int AFROND_TOTAAL = 2;
    private int afronding = AFROND_GEEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        bewaardeWaardes = getSharedPreferences("SavedValues", MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rekeningBedragEditText = (EditText) findViewById(R.id.rekeningBedragEditText);
        procentTextView = (TextView) findViewById(R.id.ProcentView);
        seekbar = (SeekBar) findViewById(R.id.procentSeekBar);
        seekbar.setOnSeekBarChangeListener(this);
        fooiTextView = (TextView) findViewById(R.id.ProcentTotaal);
        totaalTextView = (TextView) findViewById(R.id.Totaal);
        rekeningBedragEditText.setOnEditorActionListener(this);
        afrondRadioGroup = (RadioGroup) findViewById(R.id.radiogroep);
        afrondRadioGroup.setOnCheckedChangeListener(this);

    }

    @Override
    public void onPause() {
        // bewaar de variabelen
        SharedPreferences.Editor editor = bewaardeWaardes.edit();
        editor.putString("rekeningBedragString", rekeningBedragEditText.getText().toString());
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
        if (afronding == AFROND_FOOI) {
            fooibedrag = Math.round(fooibedrag);
        }
        double totaalbedrag = rekeningBedrag + fooibedrag;
        if (afronding == AFROND_TOTAAL) {
            totaalbedrag = Math.round(totaalbedrag);
            fooibedrag = totaalbedrag - rekeningBedrag;
        }

        DecimalFormat df = new DecimalFormat("€ ###,###.00");
        fooiTextView.setText(df.format(fooibedrag));
        totaalTextView.setText(df.format(totaalbedrag));
        procentTextView.setText("" + (int) fooiPercentage + "%");


    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.optie1RadioButton:
                afronding = AFROND_GEEN;
                break;
            case R.id.optie2RadioButton:
                afronding = AFROND_FOOI;
                break;
            case R.id.optie3RadioButton:
                afronding = AFROND_TOTAAL;
        }
        berekenEnToon();
    }

    //doet niets
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    //verandert alleen fooiPercentageTextview
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        fooiPercentageInt = i;
        fooiPercentage = (double) fooiPercentageInt;
        procentTextView.setText("" + fooiPercentageInt + "%");
    }

    //maak de berekening na input gebruiker
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        berekenEnToon();
    }
}

