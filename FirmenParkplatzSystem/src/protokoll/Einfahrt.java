package protokoll;

public enum Einfahrt {
    FAHRZEUG_ANMELDEN(1L),
    FAHRZEUG_ABMELDEN(2L),
    ISTPARKHAUSVOLL(3L),
    PARKHAUSISTVOLL(4L),
    PARKHAUSISTNICHTVOLL(5L),
    ERROR001(001L); //Parkhaus ist voll

    private Long in;
    Einfahrt(Long i) {
        in = i;
    }

    public Long getIn() {
        return in;
    }
}
