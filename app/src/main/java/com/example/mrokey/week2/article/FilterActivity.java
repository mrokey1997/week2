package com.example.mrokey.week2.article;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;

import com.example.mrokey.week2.R;
import com.example.mrokey.week2.nosort.DatePickerFragment;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    final String TAG = "DEBUGGG";

    @BindView(R.id.checkbox_arts)
    CheckBox checkbox_arts;

    @BindView(R.id.checkbox_fashion_style)
    CheckBox checkbox_fashion_style;

    @BindView(R.id.checkbox_sports)
    CheckBox checkbox_sports;

    CompoundButton.OnCheckedChangeListener checkListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        ButterKnife.bind(this);

        setupCheckListener();

        setupCheckboxes();
    }

    private void setupCheckListener() {
        checkListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switch (compoundButton.getId()) {
                    case R.id.checkbox_arts:
                        if (b) {
                            Log.d(TAG, "Arts true");
                            checkbox_arts.setChecked(true);
                        } else {
                            Log.d(TAG, "Arts false");
                            checkbox_arts.setChecked(false);
                        }
                        break;
                    case R.id.checkbox_fashion_style:
                        // TODO
                        break;
                    case R.id.checkbox_sports:
                        // TODO
                        break;
                }
            }
        };
    }

    @OnClick(R.id.tv_date)
    public void setDate() {
        showDatePickerDialog();
    }

    private void setupCheckboxes() {
        checkbox_arts.setOnCheckedChangeListener(checkListener);
        checkbox_fashion_style.setOnCheckedChangeListener(checkListener);
        checkbox_sports.setOnCheckedChangeListener(checkListener);
    }

    public void showDatePickerDialog() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getSupportFragmentManager(), "date_picker");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
    }
}
