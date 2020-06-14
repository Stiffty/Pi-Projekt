package verwaltung_Parkplatz;

import eigene_Datenstrukturen.list.List;
import protokoll.Protokoll;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

public class Controller {
    private int anzahlParkplätze;
    private int anzahlMietParkplätze;
    public static List<Parkplatz> parkplätze;
    public static List<Ticket> tickets;
    private Random rand;

    private int preisPro30Min = 1;
    private int preisProStunde = 3;
    private int prisProTag = 30;

    public Controller(int anzahlParkplätze, int anzahlMietParkplätze) {
        parkplätze = new List<>();
        tickets = new List<>();
        rand = new Random();
        this.anzahlParkplätze = anzahlParkplätze;
        this.anzahlMietParkplätze = anzahlMietParkplätze;
        for (int i = 0; i < anzahlMietParkplätze; i++) {
            parkplätze.add(new Parkplatz(parkplätze.length(), Status.FREI, true));
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
        System.out.println(id + " " + parkplätze.get(id).getStatus() + " " +  parkplätze.get(id).getId());
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

    public int regNewTicket(){
        Date now = new Date();
        Ticket ticket = new Ticket(-1,now,null );
        tickets.add(ticket);
        return ticket.getId();
    }

    public double getPreisForParkdauer(int id){
        Ticket ticket = getTicketforId(id);
        if(ticket == null)
            return 0;
        LocalTime time = ticket.getParkdauer();

        double preis = 0;
        if(time.getMinute() <= 30){
            preis += preisPro30Min;
        } else if(time.getMinute()>30||time.getHour()>=1){
            if(time.getHour() == 0){
                preis += preisProStunde;
            }else{
                preis += preisProStunde*(time.getHour()+1);
            }
        }
        if(time.getHour() > 23){
            preis+=prisProTag;
        }
        return preis;
    }

    public String getParkTimeForId(int id ){
        Ticket ticket = getTicketforId(id);
        if(ticket == null)
            return Protokoll.ERROR003.name();

        if(ticket.getBezahlt() == null) {
            long time = ticket.getEinfahrt().getTime();

            LocalTime now = LocalTime.now();
            Date date = new Date();
            setTicketbezahlung(id, date);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            ticket.setParkdauer(now.minusSeconds(time / 1000).minusHours(2));

            return formatter.format(now.minusSeconds(time / 1000).minusHours(2));
        }else{
            return null;
        }
    }

    public void setTicketbezahlung(int id ,Date date){
        Ticket ticket = getTicketforId(id);
        ticket.setBezahlt(date);
    }

    public Ticket getTicketforId(int id){
        for (int i = 0; i < tickets.length(); i++) {
            if(tickets.get(i).getId() == id){
                return tickets.get(i);
            }
        }
        return null;
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
        return parkplätze;
    }

    public void setParkplätze(List<Parkplatz> parkplätze) {
        this.parkplätze = parkplätze;
    }
}
