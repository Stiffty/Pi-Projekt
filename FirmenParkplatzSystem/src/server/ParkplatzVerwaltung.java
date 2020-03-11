package server;

import server.Verwaltung.Parkplatz;
import server.Verwaltung.Preisliste;
import server.Verwaltung.RifdKarte;

import java.util.ArrayList;
import java.util.List;

public class ParkplatzVerwaltung {

    private List<Parkplatz> parkplätze;
    private List<RifdKarte> rifdKarten;
    private static Preisliste preisliste;

    public ParkplatzVerwaltung() {

        parkplätze = new ArrayList<>();
        rifdKarten = new ArrayList<>();
        preisliste = new Preisliste();

        init();
    }

    private void init(){

        int parkplätzeAnz = 50;
        int[] dauerparker = {1,10};

        for (int i = 0; i < parkplätzeAnz; i++) {
            parkplätze.add(new Parkplatz());
            if(i+1 >= dauerparker[0]&&i+1<=dauerparker[1])
                parkplätze.get(parkplätze.size()-1).setVermietet(true);
        }
    }

}
