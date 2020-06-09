package verwaltung_Parkplatz;

import eigene_Datenstrukturen.list.List;

import java.util.Random;

public class Controller {
    private int anzahlParkplätze;
    private int anzahlMietParkplätze;
    public static List<Parkplatz> parkplätze;
    private Random rand;

    public Controller(int anzahlParkplätze, int anzahlMietParkplätze) {
        parkplätze = new List<>();
        rand = new Random();
        this.anzahlParkplätze = anzahlParkplätze;
        this.anzahlMietParkplätze = anzahlMietParkplätze;
        for (int i = 0; i < anzahlMietParkplätze; i++) {
            parkplätze.add(new Parkplatz(parkplätze.length(), Status.BELEGT, true));
        }
        for (int i = 0; i < anzahlParkplätze; i++) {
            parkplätze.add(new Parkplatz(parkplätze.length(), Status.FREI, false));
        }
    }

    public void belegeParkplatz(int id) {
        System.out.println("set");
        int i = rand.nextInt(parkplätze.length()-1);
        if(id == -1)
            parkplätze.get(i).setStatus(Status.BELEGT);
        else
            parkplätze.get(id).setStatus(Status.BELEGT);
        System.out.println(i + " " + parkplätze.get(i).getStatus() + " " +  parkplätze.get(i).getId());
    }

    public void parkplatzDefekt(int id) {
        if(id == -1)
            parkplätze.get(rand.nextInt(parkplätze.length())).setStatus(Status.DEFEKT);
        else
            parkplätze.get(id).setStatus(Status.DEFEKT);
    }

    public void raumeParkplatz(int id) {
        if(id == -1)
            parkplätze.get(rand.nextInt(parkplätze.length())).setStatus(Status.FREI);
        else
            parkplätze.get(id).setStatus(Status.FREI);

    }

    public void addParkplatz(Status status,boolean isMietet){
        //???
        parkplätze.add(new Parkplatz(parkplätze.length(), status, isMietet));
        if(isMietet)
            anzahlMietParkplätze++;
        else
            anzahlParkplätze++;
    }

    public int getAnzahlParkplätze() {
        return anzahlParkplätze;
    }

    public void setAnzahlParkplätze(int anzahlParkplätze) {
        this.anzahlParkplätze = anzahlParkplätze;
    }

    public int getAnzahlMietParkplätze() {
        return anzahlMietParkplätze;
    }

    public void setAnzahlMietParkplätze(int anzahlMietParkplätze) {
        this.anzahlMietParkplätze = anzahlMietParkplätze;
    }

    public List<Parkplatz> getParkplätze() {
        for (int i = 0; i < parkplätze.length(); i++) {
            System.out.println( i + " " +parkplätze.get(i).getStatus());
        }
        return parkplätze;
    }

    public void setParkplätze(List<Parkplatz> parkplätze) {
        this.parkplätze = parkplätze;
    }
}
