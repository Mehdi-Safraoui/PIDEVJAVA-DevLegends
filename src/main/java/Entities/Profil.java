package Entities;

public class Profil {
    private int id;
    private User user;
    private String first_name;
    private String last_name;
    private String user_role;
    private int user_age;
    private String user_picture;
    private String doc_specialty;


    public Profil() {}

    public Profil(User user) {
        this.id = user.getId();
        this.user = user;
        this.first_name = user.getFirst_name();
        this.last_name = user.getLast_name();
        this.user_role = user.getUser_role();
        this.user_age = user.getUser_age();
        this.user_picture = user.getUser_picture();
        this.doc_specialty = user.getDoc_specialty();
    }

    public Profil(String first_name, String last_name, String user_role, int user_age,
                  String doc_specialty, String user_picture) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.user_role = user_role;
        this.user_age = user_age;
        this.doc_specialty = doc_specialty;
        this.user_picture = user_picture;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public String getFirst_name() { return first_name; }
    public void setFirst_name(String first_name) { this.first_name = first_name; }

    public String getLast_name() { return last_name; }
    public void setLast_name(String last_name) { this.last_name = last_name; }

    public String getUser_role() { return user_role; }
    public void setUser_role(String user_role) { this.user_role = user_role; }

    public int getUser_age() { return user_age; }
    public void setUser_age(int user_age) { this.user_age = user_age; }

    public String getUser_picture() { return user_picture; }
    public void setUser_picture(String user_picture) { this.user_picture = user_picture; }

    public String getDoc_specialty() { return doc_specialty; }
    public void setDoc_specialty(String doc_specialty) { this.doc_specialty = doc_specialty; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", user_role='" + user_role + '\'' +
                ", user_age=" + user_age +
                ", doc_specialty='" + doc_specialty + '\'' +
                ", user_picture='" + user_picture + '\'' +
                '}';
    }
}
