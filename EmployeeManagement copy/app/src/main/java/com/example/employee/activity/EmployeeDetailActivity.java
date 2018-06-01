package com.example.employee.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.employee.R;
import com.example.employee.core.DbHelper;
import com.example.employee.core.Employee;
import com.example.employee.core.EmployeeAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class EmployeeDetailActivity extends AppCompatActivity {

    private Employee employee;
    private TextView firstname, lastname, emp_status, hiredate, emp_no;
    private String fname, lname, eStatus, date;
    int eNum;

    private DbHelper helper;
    private ArrayList<Employee> list;

    int id;
    SharedPreferences sharedPreferences;

    String dateformat;
    SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_detail);

        initViews();
        fetchData();
        setData();
    }

    /*init, getPreference from another activity,
        set dateFormat*/
    public void initViews() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        dateformat = sharedPreferences.getString("dateformat", "MMM dd, yyyy");
        simpleDateFormat = new SimpleDateFormat(dateformat,Locale.US);

        /*to get whole data model do getSerializableExtra("key") also implement it in model class*/
        employee = (Employee) getIntent().getSerializableExtra("Data");
        helper = new DbHelper(this);
        list = helper.getEmployee();

        firstname = findViewById(R.id.tv_fname);
        lastname = findViewById(R.id.tv_lname);
        emp_no = findViewById(R.id.tv_emp_no);
        emp_status = findViewById(R.id.tv_status);
        hiredate = findViewById(R.id.tv_date);

    }

    public void fetchData() {
        id = employee.getId();
        fname = employee.getfName();
        lname = employee.getlName();
        eNum = employee.getNumber();
        eStatus = employee.getStatus();
        String d = employee.getDate();

        /*parse date into date-formatter*/
        try {
            DateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
            Date cookiedate = formatter.parse(d);

            Calendar cal = Calendar.getInstance();
            cal.setTime(cookiedate);
            date = simpleDateFormat.format(cookiedate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setData() {
        firstname.setText(fname);
        lastname.setText(lname);
        emp_no.setText(eNum + "");
        emp_status.setText(eStatus);
        hiredate.setText(date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_delete:
                /*delete data*/
                helper.deleteEmployee(id);

                /*put startActivityForResult,
                    when activity pass data to another activity*/
                Intent i = new Intent(EmployeeDetailActivity.this, ListActivity.class);
                setResult(RESULT_OK, i);
                startActivityForResult(i,108);
                return true;

            case R.id.item_pen_Add:
                /*jump to already exist activity
                    give key-value*/
                Intent intent = new Intent(this, FormActivity.class);
                /*pass whole data model into another activity*/
                intent.putExtra("Data", employee);
                intent.putExtra("from", "detail");
                setResult(RESULT_OK, intent);
                startActivityForResult(intent,109);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if((requestCode==109 || requestCode==108) && requestCode == Activity.RESULT_OK){
                setResult(RESULT_OK);
                finish();
            }
        }
    }
}