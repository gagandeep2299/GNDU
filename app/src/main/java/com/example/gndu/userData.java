package com.example.gndu;

public class userData {

    private String userName;
    private String userContactNumber;
    private String userId;
    private String Semester;
    private String Section;
    private String phoneNumber;


    public userData()
    {

    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    private String ImageUrl;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserContactNumber() {
        return userContactNumber;
    }

    public void setUserContactNumber(String userContactNumber) {
        this.userContactNumber = userContactNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSemester() {
        return Semester;
    }

    public void setSemester(String semester) {
        Semester = semester;
    }

    public String getSection() {
        return Section;
    }

    public void setSection(String section) {
        Section = section;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    private String email;
    private String password;

    // Firebase Realtime Database.
    public userData(String email,String password, String Section,String semester,String id,String name,String phoneNumber,String ImageUrl) {
        this.email=email;
        this.password=password;
        this.Section=Section;
        this.Semester=semester;
        this.userId=id;
        this.userName=name;
        this.phoneNumber=phoneNumber;
        this.ImageUrl=ImageUrl;
    }
    // created getter and setter methods
    // for all our variables.

}

