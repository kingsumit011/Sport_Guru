package com.android.example.sportyguru;

import java.io.Serializable;

public class University implements Serializable {
    private String university_Name;
    private String university_State_Province;
    private String university_country;
    private String university_Web_Page;
    private String universityDomainName;
    private String universityCode;
    private String university_Address;

    public University(String university_Name, String university_State_Province, String university_country, String university_Web_Page, String universityDomainName, String universityCode, String university_Address) {
        this.university_Name = university_Name;
        this.university_State_Province = university_State_Province;
        this.university_country = university_country;
        this.university_Web_Page = university_Web_Page;
        this.universityDomainName = universityDomainName;
        this.universityCode = universityCode;
        this.university_Address = university_Address;
    }

    public University(String university_Name, String university_State_Province, String university_country, String university_Web_Page, String universityDomainName, String universityCode) {
        this.university_Name = university_Name;
        this.university_State_Province = university_State_Province;
        this.university_country = university_country;
        this.university_Web_Page = university_Web_Page;
        this.universityDomainName = universityDomainName;
        this.universityCode = universityCode;
        if (university_State_Province != null && !university_State_Province.equals("null")) {
            university_Address = university_State_Province + " , " + university_country;
        } else {
            university_Address = university_country;
        }
    }

    public String getUniversityDomainName() {
        return universityDomainName;
    }

    public void setUniversityDomainName(String universityDomainName) {
        this.universityDomainName = universityDomainName;
    }

    public String getUniversityCode() {
        return universityCode;
    }

    public void setUniversityCode(String universityCode) {
        this.universityCode = universityCode;
    }

    public String getUniversity_Address() {
        return university_Address;
    }

    public void setUniversity_Address(String university_Address) {
        this.university_Address = university_Address;
    }

    public String getUniversity_State_Province() {
        return university_State_Province;
    }

    public void setUniversity_State_Province(String university_State_Province) {
        this.university_State_Province = university_State_Province;
    }

    public String getUniversity_country() {
        return university_country;
    }

    public void setUniversity_country(String university_country) {
        this.university_country = university_country;
    }

    public String getUniversity_Web_Page() {
        return university_Web_Page;
    }

    public void setUniversity_Web_Page(String university_Web_Page) {
        this.university_Web_Page = university_Web_Page;
    }


    public String getUniversity_Name() {
        if (university_Name == null)
            return "Name Not Available";
        return university_Name;
    }

    public void setUniversity_Name(String university_Name) {
        this.university_Name = university_Name;
    }
}
