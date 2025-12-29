package com.bank.app.model;

import org.springframework.data.annotation.Id; 
import org.springframework.data.mongodb.core.mapping.Document; 

@Document(collection = "user_details") // Maps to the 'user_details' collection
public class UserDetail {

    @Id // Marks username as the MongoDB document ID
    private String username;
    
    // Personal Fields
    private String firstName;
    private String middleName;
    private String lastName;
    private String suffix;
    private String birthdate; // Stored as String (YYYY-MM-DD) for simplicity

    // Address Fields
    private String houseNoStreet;
    private String district;
    private String cityMunicipality;
    private String state;

    // Default Constructor (Required by Spring/JPA if used)
    public UserDetail() {
    }

    // --- Getters and Setters ---

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getSuffix() { return suffix; }
    public void setSuffix(String suffix) { this.suffix = suffix; }
    public String getBirthdate() { return birthdate; }
    public void setBirthdate(String birthdate) { this.birthdate = birthdate; }
    public String getHouseNoStreet() { return houseNoStreet; }
    public void setHouseNoStreet(String houseNoStreet) { this.houseNoStreet = houseNoStreet; }
    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }
    public String getCityMunicipality() { return cityMunicipality; }
    public void setCityMunicipality(String cityMunicipality) { this.cityMunicipality = cityMunicipality; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
}