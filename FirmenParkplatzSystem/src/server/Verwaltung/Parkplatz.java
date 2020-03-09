package server.Verwaltung;

public class Parkplatz {
    private String status;
    private boolean isVermietet;
    public Parkplatz() {
        status = String.valueOf(Status.FREI);
        isVermietet = false;
    }

    public boolean isVermietet() {
        return isVermietet;
    }

    public void setVermietet(boolean vermietet) {
        isVermietet = vermietet;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
