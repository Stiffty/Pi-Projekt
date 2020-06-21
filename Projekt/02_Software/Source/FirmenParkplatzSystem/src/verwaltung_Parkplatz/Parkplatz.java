package verwaltung_Parkplatz;

import java.io.Serializable;

public class Parkplatz implements Serializable {
    private int id;
    private Status status;
    private boolean isMietet;

    public Parkplatz(int id, Status status, boolean isMietet) {
        this.id = id;
        this.status = status;
        this.isMietet = isMietet;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isMietet() {
        return isMietet;
    }

    public void setMietet(boolean mietet) {
        isMietet = mietet;
    }
}
