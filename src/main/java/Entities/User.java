package Entities;

import java.util.Date;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
    private int age;
    private String picture;
    private String specialty;
    private String roles; // ou List<String> si vous désérialisez le JSON
    private boolean accountStatus;
    private String phoneNumber;
    private String avatar;
    private String discountCode;
    private boolean isDiscountUsed;
    private String resetCode;
    private Date bannedUntil;
    private Double latitude;
    private Double longitude;
    private String address;

    // Constructeurs
    public User() {
    }

    public User(int id, String firstName, String lastName, String email, String password,
                String role, int age, String picture, String specialty, String roles,
                boolean accountStatus, String phoneNumber, String avatar, String discountCode,
                boolean isDiscountUsed, String resetCode, Date bannedUntil, Double latitude,
                Double longitude, String address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.age = age;
        this.picture = picture;
        this.specialty = specialty;
        this.roles = roles;
        this.accountStatus = accountStatus;
        this.phoneNumber = phoneNumber;
        this.avatar = avatar;
        this.discountCode = discountCode;
        this.isDiscountUsed = isDiscountUsed;
        this.resetCode = resetCode;
        this.bannedUntil = bannedUntil;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public boolean isAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(boolean accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public boolean isDiscountUsed() {
        return isDiscountUsed;
    }

    public void setDiscountUsed(boolean discountUsed) {
        isDiscountUsed = discountUsed;
    }

    public String getResetCode() {
        return resetCode;
    }

    public void setResetCode(String resetCode) {
        this.resetCode = resetCode;
    }

    public Date getBannedUntil() {
        return bannedUntil;
    }

    public void setBannedUntil(Date bannedUntil) {
        this.bannedUntil = bannedUntil;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Méthode utilitaire pour obtenir le nom complet
    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}