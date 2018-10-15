package com.fueltaxcalculator.fueltaxcalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fxn769.Numpad;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.amount_textview)
    TextView amountTextView;

    @BindView(R.id.numpad)
    Numpad numpad;

    @BindView(R.id.fuel_type_radio_group)
    RadioGroup radioGroup;

    private final static String NAME = "Settings";
    String fuelType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ((RadioButton)radioGroup.getChildAt(0)).setChecked(true);
        fuelType = "Super";

        numpad.setOnTextChangeListner((String text, int digits_remaining) -> {
            amountTextView.setText(text);
        });

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            for (int i = 0; i < 3; i++){
                RadioButton radioButton = ((RadioButton)radioGroup.getChildAt(i));
                if (radioButton.isChecked()){
                    fuelType = radioButton.getTag().toString();
                }
            }
        });
    }

    @OnClick(R.id.calculate_button)
    public void calculate(View view) {
        if (TextUtils.isEmpty(fuelType))return;

        String price = getSharedPrefs().getString(fuelType + "_price", null);

        String levy = getSharedPrefs().getString(fuelType + "_levy", null);

        String vat = getSharedPrefs().getString(fuelType + "_vat", null);

        if (price == null || levy == null || vat == null){
            SettingsDialog settingsDialog = new SettingsDialog(this, false, dialogInterface -> {
                //Do nothing
            });

            settingsDialog.show();

            Toast.makeText(this, "You need to enter some settings first", Toast.LENGTH_LONG).show();
        }else {
            try{
                double decimalPrice = Double.parseDouble(price);
                double decimalLevy = Double.parseDouble(levy);
                double decimalVat = Double.parseDouble(vat);

                double tax = (decimalPrice - decimalLevy) * (decimalVat/100);
                String readableTax = String.valueOf(tax);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(readableTax)
                        .setTitle("Your Tax value");
                AlertDialog dialog = builder.create();
                dialog.show();
            }catch (Exception ex){
                Toast.makeText(this, "We were unable to calculate your tax. Please check your figures and try again", Toast.LENGTH_LONG).show();
            }
        }
    }

    private SharedPreferences getSharedPrefs() {
        return this.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }
}
