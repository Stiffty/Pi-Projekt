package protokoll;

public enum Protokoll {
    FAHRZEUG_ANMELDEN(1L),
    FAHRZEUG_ABMELDEN(2L),
    ISTPARKHAUSVOLL(3L),
    PARKHAUSISTVOLL(4L),
    PARKHAUSISTNICHTVOLL(5L),
    BEZAHLEN(6L),
    ERROR001(001L),//Parkhaus ist voll
    ERROR002(002L), //Schon Bezahlt
    ERROR003(003L) // BezahlId nicht gefunden
    ;

    private Long in;
    Protokoll(Long i) {
        in = i;
    }

    public Long getIn() {
        return in;
    }
}
