package com.example.employee.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.employee.core.DbHelper;
import com.example.employee.core.Employee;
import com.example.employee.core.EmployeeAdapter;
import com.example.employee.core.OnItemClickListener;
import com.example.employee.R;

import java.util.ArrayList;
import java.util.Comparator;

public class ListActivity extends AppCompatActivity implements OnItemClickListener {

    private Spinner spint_status,spin_order;
    private RecyclerView rvEmployee;
    private TextView tvNoData;
    private ArrayList<Employee> list;
    private DbHelper dbHelper;
    LinearLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupClick();
        setRecyclerView();
        setAdapter();
    }

    /*initilization*/
    private void initViews() {
        rvEmployee = findViewById(R.id.rvEmployee);
        tvNoData = findViewById(R.id.txt_NoData);
        spin_order = findViewById(R.id.spinnerOrder);
        spint_status = findViewById(R.id.spinnerStatus);
        dbHelper = new DbHelper(this);
    }

    /*sorting through spinner*/
    private void setupClick(){
        spin_order.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String order;
                if(position==0){
                    order="ASC";
                }else {
                    order="DESC";
                }

                /*spint_status contain 2 spinner.
                    0 - sort by number
                    1 - sort by status*/
                if(spint_status.getSelectedItemPosition()==0){
                    list = dbHelper.getAllEmpNumberASC(order);
                }else {
                    list = dbHelper.getAllEmpStatusASC(order);
                }

                EmployeeAdapter adapter = new EmployeeAdapter(list,ListActivity.this);
                rvEmployee.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spint_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String order;
                if(spin_order.getSelectedItemPosition()==0){
                    order="ASC";
                }else {
                    order="DESC";
                }

                /*spint_status contain 2 spinner.
                    0 - sort by number
                    1 - sort by status*/
                if(position==0){
                    list = dbHelper.getAllEmpNumberASC(order);
                }else {
                    list = dbHelper.getAllEmpStatusASC(order);
                }

                EmployeeAdapter adapter = new EmployeeAdapter(list,ListActivity.this);
                rvEmployee.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /*setting up recyclerView*/
    private void setRecyclerView() {
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvEmployee.setLayoutManager(manager);
    }

    /*setting up adapter for recyclerView*/
    void setAdapter() {
        list = dbHelper.getEmployee();

        if (list.size() > 0) {
            tvNoData.setVisibility(View.GONE);
            rvEmployee.setVisibility(View.VISIBLE);

            EmployeeAdapter adapter = new EmployeeAdapter(list,this);
            rvEmployee.setAdapter(adapter);
        } else {
            tvNoData.setVisibility(View.VISIBLE);
            rvEmployee.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_add:
                /*put startActivityForResult,
                    when activity pass data to another activity*/
                Intent i = new Intent(this, FormActivity.class);
                i.putExtra("from", "list");
                startActivityForResult(i,999);

                return true;

            case R.id.item_setting:

                Intent intent=new Intent(ListActivity.this,SettingActivity.class);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(ListActivity.this, EmployeeDetailActivity.class);
        /*pass whole data model into another activity*/
        intent.putExtra("Data", list.get(position));
        startActivity(intent);
    }

    /*update recyclerView on RESULT.OK*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 999 && resultCode==RESULT_OK){
            setResult(RESULT_OK);
            setAdapter();
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
