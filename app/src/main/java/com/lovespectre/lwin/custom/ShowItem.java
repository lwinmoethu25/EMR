package com.lovespectre.lwin.custom;

/**
 * Created by lwin on 6/17/15.
 */
public class ShowItem {

    String id;
    String Fname;
    String Lname;
    String City;

    public ShowItem(String id, String fname, String lname,String city) {
        this.id = id;
        this.Fname = fname;
        this.Lname = lname;
        this.City=city;
    }

    public ShowItem(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFname() {
        return Fname;
    }

    public void setFname(String fname) {
        Fname = fname;
    }

    public String getLname() {
        return Lname;
    }

    public void setLname(String lname) {
        Lname = lname;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }
}
