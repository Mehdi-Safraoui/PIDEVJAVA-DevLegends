package Entities.malek;

public class Centre {
    private int id;
    private String nomCentre;
    private String adresseCentre;
    private String telCentre;
    private String emailCentre;
    private String specialiteCentre;
    private int capaciteCentre;
    private String photoCentre;

    // Constructeurs
    public Centre() {}

    public Centre(int id, String nomCentre, String adresseCentre, String telCentre, String emailCentre,
                  String specialiteCentre, int capaciteCentre, String photoCentre) {
        this.id = id;
        this.nomCentre = nomCentre;
        this.adresseCentre = adresseCentre;
        this.telCentre = telCentre;
        this.emailCentre = emailCentre;
        this.specialiteCentre = specialiteCentre;
        this.capaciteCentre = capaciteCentre;
        this.photoCentre = photoCentre;
    }

    public Centre(String nomCentre, String adresseCentre, String telCentre, String emailCentre,
                  String specialiteCentre, int capaciteCentre, String photoCentre) {
        this.nomCentre = nomCentre;
        this.adresseCentre = adresseCentre;
        this.telCentre = telCentre;
        this.emailCentre = emailCentre;
        this.specialiteCentre = specialiteCentre;
        this.capaciteCentre = capaciteCentre;
        this.photoCentre = photoCentre;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNomCentre() {
        return nomCentre;
    }

    public String getAdresseCentre() {
        return adresseCentre;
    }

    public String getTelCentre() {
        return telCentre;
    }

    public String getEmailCentre() {
        return emailCentre;
    }

    public String getSpecialiteCentre() {
        return specialiteCentre;
    }

    public int getCapaciteCentre() {
        return capaciteCentre;
    }

    public String getPhotoCentre() {
        return photoCentre;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNomCentre(String nomCentre) {
        this.nomCentre = nomCentre;
    }

    public void setAdresseCentre(String adresseCentre) {
        this.adresseCentre = adresseCentre;
    }

    public void setTelCentre(String telCentre) {
        this.telCentre = telCentre;
    }

    public void setEmailCentre(String emailCentre) {
        this.emailCentre = emailCentre;
    }

    public void setSpecialiteCentre(String specialiteCentre) {
        this.specialiteCentre = specialiteCentre;
    }

    public void setCapaciteCentre(int capaciteCentre) {
        this.capaciteCentre = capaciteCentre;
    }

    public void setPhotoCentre(String photoCentre) {
        this.photoCentre = photoCentre;
    }

    @Override
    public String toString() {
        return "Centre [id=" + id + ", nomCentre=" + nomCentre + ", adresseCentre=" + adresseCentre +
                ", telCentre=" + telCentre + ", emailCentre=" + emailCentre + ", specialiteCentre=" + specialiteCentre +
                ", capaciteCentre=" + capaciteCentre + ", photoCentre=" + photoCentre + "]";
    }


}
