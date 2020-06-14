package verwaltung_Parkplatz;

import java.time.LocalTime;
import java.util.Date;
import java.util.Random;

public class Ticket {
    private int id;
    private Date einfahrt;
    private Date bezahlt;

    private LocalTime parkdauer;

    public Ticket(int id, Date einfahrt, Date bezahlt) {
        if(id == -1){
            this.id = new Random().nextInt(Integer.MAX_VALUE);
        }else{
            this.id = id;
        }
        this.einfahrt = einfahrt;
        this.bezahlt = bezahlt;
    }

    public LocalTime getParkdauer() {
        return parkdauer;
    }

    public void setParkdauer(LocalTime parkdauer) {
        this.parkdauer = parkdauer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getEinfahrt() {
        return einfahrt;
    }

    public void setEinfahrt(Date einfahrt) {
        this.einfahrt = einfahrt;
    }

    public Date getBezahlt() {
        return bezahlt;
    }

    public void setBezahlt(Date bezahlt) {
        this.bezahlt = bezahlt;
    }
}
