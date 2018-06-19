package com.example.mrokey.week2.article;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;

import com.example.mrokey.week2.R;
import com.example.mrokey.week2.nosort.DatePickerFragment;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    @BindView(R.id.checkbox_arts)
    CheckBox checkbox_arts;

    @BindView(R.id.checkbox_fashion_style)
    CheckBox checkbox_fashion_style;

    @BindView(R.id.checkbox_sports)
    CheckBox checkbox_sports;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    CompoundButton.OnCheckedChangeListener checkListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //showDatePickerDialog();
        ButterKnife.bind(this);

        setupToolbar();

        setupCheckListener();

        setupCheckboxes();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);

        /*
         Transparent Status Bar
         */
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miFilter: {

            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setupCheckListener() {
        checkListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switch (compoundButton.getId()) {
                    case R.id.checkbox_arts:
                        // TODO
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

    private void setupCheckboxes() {
        checkbox_arts.setOnCheckedChangeListener(checkListener);
        checkbox_fashion_style.setOnCheckedChangeListener(checkListener);
        checkbox_sports.setOnCheckedChangeListener(checkListener);
    }

    public void showDatePickerDialog() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            final Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
    }
}
