package Entities;

public class Pack {
    private int id;
    private String nomPack;
    private String descriptPack;
    private double prixPack;
    private String dureePack;
    private String photoPack;
    private String discountCode;
    private boolean isUsed;

    // Constructeurs
    public Pack() {}

    public Pack(int id, String nomPack, String descriptPack, double prixPack, String dureePack, String photoPack, String discountCode, boolean isUsed) {
        this.id = id;
        this.nomPack = nomPack;
        this.descriptPack = descriptPack;
        this.prixPack = prixPack;
        this.dureePack = dureePack;
        this.photoPack = photoPack;
        this.discountCode = discountCode;
        this.isUsed = isUsed;
    }

    public Pack(String nomPack, String descriptPack, double prixPack, String dureePack, String photoPack, String discountCode, boolean isUsed) {
        this.nomPack = nomPack;
        this.descriptPack = descriptPack;
        this.prixPack = prixPack;
        this.dureePack = dureePack;
        this.photoPack = photoPack;
        this.discountCode = discountCode;
        this.isUsed = isUsed;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNomPack() {
        return nomPack;
    }

    public String getDescriptPack() {
        return descriptPack;
    }

    public double getPrixPack() {
        return prixPack;
    }

    public String getDureePack() {
        return dureePack;
    }

    public String getPhotoPack() {
        return photoPack;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public boolean isUsed() {
        return isUsed;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNomPack(String nomPack) {
        this.nomPack = nomPack;
    }

    public void setDescriptPack(String descriptPack) {
        this.descriptPack = descriptPack;
    }

    public void setPrixPack(double prixPack) {
        this.prixPack = prixPack;
    }

    public void setDureePack(String dureePack) {
        this.dureePack = dureePack;
    }

    public void setPhotoPack(String photoPack) {
        this.photoPack = photoPack;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public void setUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

    @Override
    public String toString() {
        return "Pack [id=" + id + ", nomPack=" + nomPack + ", descriptPack=" + descriptPack +
                ", prixPack=" + prixPack + ", dureePack=" + dureePack + ", photoPack=" + photoPack +
                ", discountCode=" + discountCode + ", isUsed=" + isUsed + "]";
    }
}
