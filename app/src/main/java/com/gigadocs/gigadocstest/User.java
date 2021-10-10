package com.gigadocs.gigadocstest;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    private int Id;
    @ColumnInfo(name = "Name")
    private String Name;
    @ColumnInfo(name = "Mobile")
    private String Mobile;
    @ColumnInfo(name = "Email")
    private String Email;
    @ColumnInfo(name = "Gender")
    private String Gender;
    @ColumnInfo(name = "Age")
    private int Age;
    @ColumnInfo(name = "Image")
    private String Image;
    @ColumnInfo(name = "Date")
    private String Date;


    public User(String name, String mobile, String email, String gender, int age, String image, String date) {
        Name = name;
        Mobile = mobile;
        Email = email;
        Gender = gender;
        Age = age;
        Image = image;
        Date = date;
    }

    public User(int id, String name, String mobile, String email, String gender, int age, String image, String date) {
        Id = id;
        Name = name;
        Mobile = mobile;
        Email = email;
        Gender = gender;
        Age = age;
        Image = image;
        Date = date;
    }

    public User() {
    }

    public int getId() {
        return Id;
    }

    public String getGender() {
        return Gender;
    }

    public String getName() {
        return Name;
    }

    public String getMobile() {
        return Mobile;
    }

    public String getEmail() {
        return Email;
    }

    public int getAge() {
        return Age;
    }


    public void setId(int id) {
        Id = id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}

