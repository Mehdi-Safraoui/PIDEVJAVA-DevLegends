package Entities;

import java.util.Date;

public class Contrat {
    private int id;
    private int centreId;
    private Date datdebCont;
    private Date datfinCont;
    private String modpaimentCont;
    private boolean renouvAutoCont;
    private Integer userId;

    // Constructeurs
    public Contrat() {}

    public Contrat(int centreId, Date datdebCont, Date datfinCont, String modpaimentCont, boolean renouvAutoCont, Integer userId) {
        this.centreId = centreId;
        this.datdebCont = datdebCont;
        this.datfinCont = datfinCont;
        this.modpaimentCont = modpaimentCont;
        this.renouvAutoCont = renouvAutoCont;
        this.userId = userId;
    }

    public Contrat(int id, int centreId, Date datdebCont, Date datfinCont, String modpaimentCont, boolean renouvAutoCont, Integer userId) {
        this.id = id;
        this.centreId = centreId;
        this.datdebCont = datdebCont;
        this.datfinCont = datfinCont;
        this.modpaimentCont = modpaimentCont;
        this.renouvAutoCont = renouvAutoCont;
        this.userId = userId;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getCentreId() {
        return centreId;
    }

    public Date getDatdebCont() {
        return datdebCont;
    }

    public Date getDatfinCont() {
        return datfinCont;
    }

    public String getModpaimentCont() {
        return modpaimentCont;
    }

    public boolean isRenouvAutoCont() {
        return renouvAutoCont;
    }

    public Integer getUserId() {
        return userId;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setCentreId(int centreId) {
        this.centreId = centreId;
    }

    public void setDatdebCont(Date datdebCont) {
        this.datdebCont = datdebCont;
    }

    public void setDatfinCont(Date datfinCont) {
        this.datfinCont = datfinCont;
    }

    public void setModpaimentCont(String modpaimentCont) {
        this.modpaimentCont = modpaimentCont;
    }

    public void setRenouvAutoCont(boolean renouvAutoCont) {
        this.renouvAutoCont = renouvAutoCont;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Contrat [id=" + id + ", centreId=" + centreId + ", datdebCont=" + datdebCont +
                ", datfinCont=" + datfinCont + ", modpaimentCont=" + modpaimentCont +
                ", renouvAutoCont=" + renouvAutoCont + ", userId=" + userId + "]";
    }
}
