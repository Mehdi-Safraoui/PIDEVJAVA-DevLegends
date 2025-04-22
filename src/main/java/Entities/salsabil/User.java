package Entities.salsabil;

import java.time.LocalDateTime;

public class User {
    private int id;
    private String first_name;
    private String last_name;
    private String user_email;
    private String password;
    private String user_role;
    private int user_age;
    private String user_picture;
    private String doc_specialty;
    private String roles; // à parser si JSON
    private boolean statut_compte;
    private String num_tel;
    private String discount_code;
    private boolean is_discount_used;
    private String reset_code;
    private LocalDateTime banned_until;
    private Double latitude;
    private Double longitude;
    private String address;

    public User() {}

    public User(String first_name, String last_name, String user_email, String password,
                String user_role, int user_age, String doc_specialty, String num_tel, String address, String user_picture) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.user_email = user_email;
        this.password = password;
        this.user_role = user_role;
        this.user_age = user_age;
        this.doc_specialty = doc_specialty;
        this.num_tel = num_tel;
        this.address = address;
        this.statut_compte = true;
        this.user_picture = user_picture;
    }

    public User(int id, String first_name, String last_name, String user_email, String password,
                String user_role, int user_age, String user_picture, String doc_specialty, String roles,
                boolean statut_compte, String num_tel, String discount_code, boolean is_discount_used,
                String reset_code, LocalDateTime banned_until, Double latitude, Double longitude, String address) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.user_email = user_email;
        this.password = password;
        this.user_role = user_role;
        this.user_age = user_age;
        this.user_picture = user_picture;
        this.doc_specialty = doc_specialty;
        this.roles = roles;
        this.statut_compte = statut_compte;
        this.num_tel = num_tel;
        this.discount_code = discount_code;
        this.is_discount_used = is_discount_used;
        this.reset_code = reset_code;
        this.banned_until = banned_until;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFirst_name() { return first_name; }
    public void setFirst_name(String first_name) { this.first_name = first_name; }

    public String getLast_name() { return last_name; }
    public void setLast_name(String last_name) { this.last_name = last_name; }

    public String getUser_email() { return user_email; }
    public void setUser_email(String user_email) { this.user_email = user_email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getUser_role() { return user_role; }
    public void setUser_role(String user_role) { this.user_role = user_role; }

    public int getUser_age() { return user_age; }
    public void setUser_age(int user_age) { this.user_age = user_age; }

    public String getUser_picture() { return user_picture; }
    public void setUser_picture(String user_picture) { this.user_picture = user_picture; }

    public String getDoc_specialty() { return doc_specialty; }
    public void setDoc_specialty(String doc_specialty) { this.doc_specialty = doc_specialty; }

    public String getRoles() { return roles; }
    public void setRoles(String roles) { this.roles = roles; }

    public boolean isStatut_compte() { return statut_compte; }
    public void setStatut_compte(boolean statut_compte) { this.statut_compte = statut_compte; }

    public String getNum_tel() { return num_tel; }
    public void setNum_tel(String num_tel) { this.num_tel = num_tel; }

    public String getDiscount_code() { return discount_code; }
    public void setDiscount_code(String discount_code) { this.discount_code = discount_code; }

    public boolean isIs_discount_used() { return is_discount_used; }
    public void setIs_discount_used(boolean is_discount_used) { this.is_discount_used = is_discount_used; }

    public String getReset_code() { return reset_code; }
    public void setReset_code(String reset_code) { this.reset_code = reset_code; }

    public LocalDateTime getBanned_until() { return banned_until; }
    public void setBanned_until(LocalDateTime banned_until) { this.banned_until = banned_until; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", user_email='" + user_email + '\'' +
                ", password='" + password + '\'' +
                ", user_role='" + user_role + '\'' +
                ", user_age=" + user_age +
                ", doc_specialty='" + doc_specialty + '\'' +
                ", statut_compte=" + statut_compte +
                ", num_tel='" + num_tel + '\'' +
                ", address='" + address + '\'' +
                ", user_picture='" + user_picture + '\'' +
                '}';
    }
}