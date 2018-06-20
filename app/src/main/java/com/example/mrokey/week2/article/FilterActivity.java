package com.example.mrokey.week2.article;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mrokey.week2.R;
import com.example.mrokey.week2.fragment.DatePickerFragment;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    final String TAG = "DEBUGGG";

    @BindView(R.id.checkbox_arts)
    CheckBox checkbox_arts;

    @BindView(R.id.checkbox_fashion_style)
    CheckBox checkbox_fashion_style;

    @BindView(R.id.checkbox_sports)
    CheckBox checkbox_sports;

    @BindView(R.id.edt_date)
    EditText edt_date;

    @BindView(R.id.spinner_sort_by)
    Spinner spinner_sort_by;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        ButterKnife.bind(this);

        spinner_sort_by.setOnItemSelectedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSharePreferences();
    }

    @Override
    protected void onResume() {
        super.onResume();
        restoreSharePreferences();
    }

    private void restoreSharePreferences() {
        SharedPreferences reader = getSharedPreferences("saved_data", MODE_PRIVATE);
        if (reader.getString("checkbox_arts", "fail") == "true")
            checkbox_arts.setChecked(true);
        if (reader.getString("checkbox_fashion_style", "fail") == "true")
            checkbox_fashion_style.setChecked(true);
        if (reader.getString("checkbox_sports", "fail") == "true")
            checkbox_sports.setChecked(true);
        if (reader.getString("formatted_begin_date","fail") != "fail")
            edt_date.setText(reader.getString("formatted_begin_date", "error"));
    }

    private void saveSharePreferences() {
        SharedPreferences.Editor editor = getSharedPreferences("saved_data", MODE_PRIVATE).edit();
        editor.clear();
        if (checkbox_arts.isChecked())
            editor.putString("checkbox_arts", "true");
        else editor.putString("checkbox_arts", "false");
        if (checkbox_fashion_style.isChecked())
            editor.putString("checkbox_fashion_style", "true");
        else editor.putString("checkbox_fashion_style", "false");
        if (checkbox_sports.isChecked())
            editor.putString("checkbox_sports", "true");
        else editor.putString("checkbox_sports", "false");
        editor.apply();
    }

    @OnClick(R.id.edt_date)
    public void setDate() {
        showDatePickerDialog();
    }

    @OnClick(R.id.btn_save)
    public void onClickSaveBtn() {
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }

    public void showDatePickerDialog() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getSupportFragmentManager(), "date_picker");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        edt_date.setText(year + "//" + month + "//" + day);
        String formatedDay = day >= 10 ? day + "" : "0" + day;
        String formatedMonth = month >= 10 ? month + "" : "0" + month;

        SharedPreferences.Editor editor = getSharedPreferences("saved_data", MODE_PRIVATE).edit();
        editor.putString("begin_date", year+formatedMonth+formatedDay+"");
        editor.putString("formatted_begin_date", edt_date.getText().toString());
        editor.apply();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SharedPreferences.Editor editor = getSharedPreferences("saved_data", MODE_PRIVATE).edit();
        if (position == 0) editor.putString("spinner", "newest");
        else if (position == 1) editor.putString("spinner", "oldest");
        editor.apply();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
