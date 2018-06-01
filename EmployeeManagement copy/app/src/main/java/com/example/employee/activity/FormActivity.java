package com.example.employee.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.employee.core.DbHelper;
import com.example.employee.core.Employee;
import com.example.employee.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FormActivity extends AppCompatActivity {
    private EditText etFname, etEstatus, etLname, etEnum;
    private TextView tvdate;
    private Button btnDate;
    private Calendar myCalendar;
    private DatePickerDialog datePickerDialog;
    private String strDate;
    Employee employee;
    String name, lname, status, edate;
    String activity;
    int eno;
    DbHelper dbHelper;
    SharedPreferences sharedPreferences;
    String dateformat;
    String ddate;

    SimpleDateFormat simpleDateFormat;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        initView();
        setupClick();
        checkActivity();
    }

    /*init, getPreference from another activity,
        set dateFormat*/
    public void initView() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        dateformat = sharedPreferences.getString("dateformat", "MMM dd, yyyy");
        simpleDateFormat = new SimpleDateFormat(dateformat,Locale.US);

        myCalendar = Calendar.getInstance();
        etFname = findViewById(R.id.etFname);
        etLname = findViewById(R.id.etLname);
        etEnum = findViewById(R.id.etEnum);
        etEstatus = findViewById(R.id.etEstatus);
        tvdate = findViewById(R.id.txtDate);

        btnDate = findViewById(R.id.btnDate);
    }

    /*open date picker dialog*/
    public void setupClick() {
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(FormActivity.this, R.style.DialogTheme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.setCancelable(true);
                datePickerDialog.setCanceledOnTouchOutside(true);

                datePickerDialog.show();

                datePickerDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            }
        });
    }

    /* check value is come from which activity,
    get value from other activity & set it in editText
           check parse value in this*/
    @SuppressLint("SetTextI18n")
    public void checkActivity() {
        activity = getIntent().getExtras().getString("from");
        if (activity.equals("detail")) {
            /*get data model*/
            employee = (Employee) getIntent().getSerializableExtra("Data");
            etFname.setText(employee.getfName());
            etLname.setText(employee.getlName());
            etEnum.setText(employee.getNumber()+"");
            etEstatus.setText(employee.getStatus());

            strDate = employee.getDate();

            try {
                DateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US);
                Date cookiedate = formatter.parse(strDate);
                Calendar cal = Calendar.getInstance();
                cal.setTime(cookiedate);
                ddate = simpleDateFormat.format(cookiedate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            tvdate.setText(ddate);
        }
    }

    /*choose date from calendar and set into textView*/
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            strDate = String.valueOf(myCalendar.getTime());
            tvdate.setText(simpleDateFormat.format(myCalendar.getTime()));
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        /* check field isBlank or not*/
        switch (item.getItemId()) {

            case R.id.item_add:
                if (!(etEnum.getText().length() > 0) || !(etEstatus.getText().length() > 0) || !(etFname.getText().length() > 0) || !(etLname.getText().length() > 0) || !(tvdate.getText().length() > 0)) {
                    if (!(etEnum.getText().length() > 0)) {
                        etEnum.setError("This Field is required");
                    }
                    if (!(etEstatus.getText().length() > 0)) {
                        etEstatus.setError("This Field is required");

                    }
                    if (!(etFname.getText().length() > 0)) {
                        etFname.setError("This Field is required");

                    }
                    if (!(etLname.getText().length() > 0)) {
                        etLname.setError("This Field is required");
                    }
                    if (!(tvdate.getText().length() > 0)) {
                        Toast.makeText(this, "Please select date!!", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    name = etFname.getText().toString();
                    lname = etLname.getText().toString();
                    eno = Integer.parseInt(etEnum.getText().toString());
                    status = etEstatus.getText().toString();
                    edate = strDate;

                    /*add value into database*/
                    dbHelper = new DbHelper(this);
                    if (activity.equals("list")) {
                        int id = dbHelper.addEmployee(name, lname, eno, status, edate);
                        clearField(id);
                    } else {
                        boolean isUpdate = dbHelper.updateEmployee(String.valueOf(employee.getId()), name, lname, eno, status, edate);
                        clearField(employee.getId());
                        if (isUpdate == true) {
                            Toast.makeText(FormActivity.this, "Data Update", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(FormActivity.this, "Data not Updated", Toast.LENGTH_SHORT).show();
                        }
                    }

                    Intent i = new Intent(this, ListActivity.class);
                    setResult(RESULT_OK,i);
                    startActivityForResult(i,100);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK){
            setResult(RESULT_OK);
            finish();
        }
    }

    /*clear editText & update model*/
    public void clearField(int id) {
        etFname.getText().clear();
        etLname.getText().clear();
        etEstatus.getText().clear();
        etEnum.getText().clear();
        tvdate.setText("");

        Intent intent = new Intent();
        Employee employee = new Employee(id, name, lname, eno, status, edate);
        intent.putExtra("Data", employee);
        setResult(RESULT_OK, intent);
        finish();
    }
}
