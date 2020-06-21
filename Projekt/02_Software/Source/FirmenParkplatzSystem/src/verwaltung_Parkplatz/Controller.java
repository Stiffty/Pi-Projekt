package verwaltung_Parkplatz;

import eigene_Datenstrukturen.list.List;
import protokoll.Protokoll;

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

    /**
     * Konstruktor fügt die festgelegten Parkplätze bei Programmstart hinzu.
     *
     * @param anzahlParkplätze     Parkplätze mit isMietet = false.
     * @param anzahlMietParkplätze Parkplätze mit isMietet = true.
     */
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

    /**
     * Setzt Status gleich belegt.
     *
     * @param id id des Parkplatzes.
     */
    public void belegeParkplatz(int id) {
        int i = rand.nextInt(parkplätze.length() - 1);
        if (id == -1)
            parkplätze.get(i).setStatus(Status.BELEGT);
        else
            parkplätze.get(id).setStatus(Status.BELEGT);
    }

    /**
     * Setzt Status gleich defekt.
     *
     * @param id id des Parkplatzes.
     */
    public void parkplatzDefekt(int id) {
        if (id == -1)
            parkplätze.get(rand.nextInt(parkplätze.length())).setStatus(Status.DEFEKT);
        else
            parkplätze.get(id).setStatus(Status.DEFEKT);
    }

    /**
     * Setzt Status gleich frei.
     *
     * @param id id des Parkplatzes.
     */
    public void raumeParkplatz(int id) {
        if (id == -1)
            parkplätze.get(rand.nextInt(parkplätze.length())).setStatus(Status.FREI);
        else
            parkplätze.get(id).setStatus(Status.FREI);

    }

    /**
     * Füge neuen Parkplatz hinzu.
     *
     * @param status   status
     * @param isMietet ob gemietet.
     * @return id des erstellten Parkplatzes.
     */
    public int addParkplatz(Status status, boolean isMietet) {
        //???
        Parkplatz p = new Parkplatz(parkplätze.length(), status, isMietet);
        parkplätze.add(p);
        if (isMietet)
            anzahlMietParkplätze++;
        else
            anzahlParkplätze++;
        return p.getId();
    }

    /**
     * Updatet die Parameter eines Parkplatzes.
     *
     * @param id       id des Parkplatzes.
     * @param status   status zu dem geändert wird.
     * @param isMietet ob gemietet ist.
     */
    public void updateParkplatz(int id, Status status, boolean isMietet) {
        Parkplatz p = null;
        for (int i = 0; i < parkplätze.length(); i++) {
            if (parkplätze.get(i).getId() == id) {
                p = parkplätze.get(i);
                break;
            }
        }
        if (p == null)
            return;
        p.setStatus(status);
        p.setMietet(isMietet);
    }

    /**
     * @return Anzahl der freien Parkplätze.
     */
    public int getFreieParkplaetze() {
        int counter = 0;
        for (int i = 0; i < parkplätze.length(); i++) {
            if (parkplätze.get(i).getStatus() == Status.FREI) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * Erstellt ein neues Ticket aka Karte.
     *
     * @return id des Tickets.
     */
    public int regNewTicket() {
        Date now = new Date();
        Ticket ticket = new Ticket(-1, now, null);
        tickets.add(ticket);
        return ticket.getId();
    }

    /**
     * Entfernt ein Ticket.
     *
     * @param id id des zu entfernenden Tickets.
     */
    public void removeTicket(int id) {
        for (int i = 0; i < tickets.length(); i++) {
            if (tickets.get(i).getId() == id && tickets.get(i).getBezahlt() != null) {
                tickets.remove(i);
                break;
            }
        }
    }

    /**
     * Gibt die Park preis eines Tickets.
     *
     * @param id id des Tickets.
     * @return den Preis.
     */
    public double getPreisForParkdauer(int id) {
        Ticket ticket = getTicketforId(id);
        if (ticket == null)
            return 0;
        LocalTime time = ticket.getParkdauer();

        double preis = 0;
        if (time.getMinute() <= 30) {
            preis += preisPro30Min;
        } else if (time.getMinute() > 30 || time.getHour() >= 1) {
            if (time.getHour() == 0) {
                preis += preisProStunde;
            } else {
                preis += preisProStunde * (time.getHour() + 1);
            }
        }
        if (time.getHour() > 23) {
            preis += prisProTag;
        }
        return preis;
    }

    /**
     * Gibt die Parkdauer bis zum zeitpunkt des Aufrufs.
     *
     * @param id id des Tickets.
     * @return die Formatierte Zeit.
     */
    public String getParkTimeForId(int id) {
        Ticket ticket = getTicketforId(id);
        if (ticket == null)
            return Protokoll.ERROR003.name();

        if (ticket.getBezahlt() == null) {
            long time = ticket.getEinfahrt().getTime();

            LocalTime now = LocalTime.now();
            Date date = new Date();
            setTicketbezahlung(id, date);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            ticket.setParkdauer(now.minusSeconds(time / 1000).minusHours(2));

            return formatter.format(now.minusSeconds(time / 1000).minusHours(2));
        } else {
            return null;
        }
    }

    /**
     * Setzt das Beizahldatum für ein Ticket.
     *
     * @param id   id des Tickets.
     * @param date das Datum.
     */
    public void setTicketbezahlung(int id, Date date) {
        Ticket ticket = getTicketforId(id);
        ticket.setBezahlt(date);
    }

    /**
     * Gibt ein Ticket für eine id.
     *
     * @param id id des Tickets.
     * @return das Ticket.
     */
    public Ticket getTicketforId(int id) {
        for (int i = 0; i < tickets.length(); i++) {
            if (tickets.get(i).getId() == id) {
                return tickets.get(i);
            }
        }
        return null;
    }

    /**
     * Gibt zurück, ob ein Ticket schon bezahlt wurde.
     *
     * @param id id des Tickets.
     * @return true wenn ja sonst false.
     */
    public boolean getIfBezahlt(int id) {
        if (getTicketforId(id) == null) {
            return false;
        }
        return getTicketforId(id).getBezahlt() != null;
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
