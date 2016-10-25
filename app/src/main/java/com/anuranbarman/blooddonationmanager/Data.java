package com.anuranbarman.blooddonationmanager;

/**
 * Created by anuran on 22/10/16.
 */

public class Data {
    private String name,username,useremail,usermob,userlocation,userblood;

    public Data(String name,String username,String useremail,String usermob,String userlocation,String userblood){
        this.name=name;
        this.username=username;
        this.useremail=useremail;
        this.usermob=usermob;
        this.userlocation=userlocation;
        this.userblood=userblood;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getUsermob() {
        return usermob;
    }

    public void setUsermob(String usermob) {
        this.usermob = usermob;
    }

    public String getUserlocation() {
        return userlocation;
    }

    public void setUserlocation(String userlocation) {
        this.userlocation = userlocation;
    }

    public String getUserblood() {
        return userblood;
    }

    public void setUserblood(String userblood) {
        this.userblood = userblood;
    }
}
