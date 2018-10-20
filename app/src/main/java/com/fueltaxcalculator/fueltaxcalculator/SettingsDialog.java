package com.fueltaxcalculator.fueltaxcalculator;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by munene on 10/14/2018.
 */
public class SettingsDialog extends Dialog implements android.view.View.OnClickListener {
    @BindView(R.id.titleTextView)
    TextView titleTextView;

    @BindView(R.id.priceEditText)
    EditText priceEditText;

    @BindView(R.id.levyEditText)
    EditText levyEditText;

    @BindView(R.id.vatEditText)
    EditText vatEditText;

    @BindView(R.id.saveButton)
    Button saveButton;

    String fuelType;

    private final static String NAME = "Settings";

    protected SettingsDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_settings);
        ButterKnife.bind(this);
        saveButton.setOnClickListener(this);
    }

    public void save() {
        if (TextUtils.isEmpty(priceEditText.getText().toString()))
        {
            Toast.makeText(this.getContext(), "You need to set the price", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(levyEditText.getText().toString()))
        {
            Toast.makeText(this.getContext(), "You need to set the levy", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(vatEditText.getText().toString()))
        {
            Toast.makeText(this.getContext(), "You need to set the VAT", Toast.LENGTH_LONG).show();
            return;
        }

        getSharedPrefs().edit().putString( fuelType + "_price", priceEditText.getText().toString()).apply();

        getSharedPrefs().edit().putString(fuelType + "_levy", levyEditText.getText().toString()).apply();

        getSharedPrefs().edit().putString(fuelType + "_vat", vatEditText.getText().toString()).apply();

        this.dismiss();
    }

    private SharedPreferences getSharedPrefs() {
        return this.getContext().getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public void setTitle(String title){
        titleTextView.setText(title);
        fuelType = title;

        String price = getSharedPrefs().getString(fuelType + "_price", null);

        String levy = getSharedPrefs().getString(fuelType + "_levy", null);

        String vat = getSharedPrefs().getString(fuelType + "_vat", null);

        if (price != null) priceEditText.setText(price);
        if (levy != null) levyEditText.setText(levy);
        if(vat != null) vatEditText.setText(vat);
    }

    @Override
    public void onClick(View view) {
        save();
    }
}
