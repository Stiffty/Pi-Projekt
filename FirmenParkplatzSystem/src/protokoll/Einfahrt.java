package protokoll;

public enum Einfahrt {
    FAHRZEUG_ANMELDEN(1L),
    FAHRZEUG_ABMELDEN(2L);

    private Long in;
    Einfahrt(Long i) {
        in = i;
    }

    public Long getIn() {
        return in;
    }
}
