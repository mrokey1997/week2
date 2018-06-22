package com.example.mrokey.week2.filter.view;

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

    Calendar calendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        ButterKnife.bind(this);

        initCalender();

        spinner_sort_by.setOnItemSelectedListener(this);
    }

    @SuppressLint("SetTextI18n")
    private void initCalender() {
        calendar = Calendar.getInstance();

        edt_date.setText(calendar.get(Calendar.YEAR) + "/"
                + formatDate(calendar.get(Calendar.MONTH)) + "/"
                + formatDate(calendar.get(Calendar.DAY_OF_MONTH)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        restoreSharePreferences();
    }

    private void restoreSharePreferences() {
        SharedPreferences reader = getSharedPreferences("saved_data", MODE_PRIVATE);

        if (reader.getString("checkbox_arts", "fail").equals("true"))
            checkbox_arts.setChecked(true);
        if (reader.getString("checkbox_fashion_style", "fail").equals("true"))
            checkbox_fashion_style.setChecked(true);
        if (reader.getString("checkbox_sports", "fail").equals("true"))
            checkbox_sports.setChecked(true);
        if (!reader.getString("formatted_begin_date", "fail").equals("fail"))
            edt_date.setText(reader.getString("formatted_begin_date", "error"));

        String year = reader.getString("year", "fail");
        if (!year.equals("fail"))
            calendar.set(Calendar.YEAR, Integer.valueOf(year));
        String month = reader.getString("month", "fail");
        if (!month.equals("fail"))
            calendar.set(Calendar.MONTH, Integer.valueOf(month));
        String day = reader.getString("day", "fail");
        if (!month.equals("fail"))
            calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(day));
    }

    private void saveSharePreferences() {
        SharedPreferences.Editor editor = getSharedPreferences("saved_data", MODE_PRIVATE).edit();
        if (checkbox_arts.isChecked())
            editor.putString("checkbox_arts", "true");
        else editor.putString("checkbox_arts", "false");
        if (checkbox_fashion_style.isChecked())
            editor.putString("checkbox_fashion_style", "true");
        else editor.putString("checkbox_fashion_style", "false");
        if (checkbox_sports.isChecked())
            editor.putString("checkbox_sports", "true");
        else editor.putString("checkbox_sports", "false");

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String formattedDay = day >= 10 ? day + "" : "0" + day;
        String formattedMonth = month >= 10 ? month + "" : "0" + month;

        editor.putString("begin_date", year+formattedMonth+formattedDay+"");
        editor.putString("formatted_begin_date", edt_date.getText().toString());

        editor.putString("just_filter", "true");

        editor.apply();

        finish();
    }

    @OnClick(R.id.edt_date)
    public void setDate() {
        showDatePickerDialog();
    }

    @OnClick(R.id.btn_save)
    public void onClickSaveBtn() {
        saveSharePreferences();
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }

    public void showDatePickerDialog() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getSupportFragmentManager(), "date_picker");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        SharedPreferences.Editor editor = getSharedPreferences("saved_data", MODE_PRIVATE).edit();
        editor.putString("year", year+"");
        editor.putString("month", month+"");
        editor.putString("day", day+"");
        editor.apply();

        edt_date.setText(year + "/" + formatDate(month) + "/" + formatDate(day));
    }

    public String formatDate(int i) {
        return i >= 10 ? i + "" : "0" + i;
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
