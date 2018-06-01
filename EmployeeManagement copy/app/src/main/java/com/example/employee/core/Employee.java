package com.example.employee.core;

import java.io.Serializable;
/*if data model has pass then make it Serializable*/
public class Employee implements Serializable {
    int id;
    String fName;
    String lName;
    int number;
    String status;
    String date;

    public Employee() {

    }

    public Employee(String fName, String lName, int number, String status, String date) {
        this.fName = fName;
        this.lName = lName;
        this.number = number;
        this.status = status;
        this.date = date;
    }

    public Employee(int id, String fName, String lName, int number, String status, String date) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.number = number;
        this.status = status;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
