package protokoll;

public enum Einfahrt {
    NEUESFAHRZEUG (1L);

    private Long in;
    Einfahrt(Long i) {
        in = i;
    }

    public Long getIn() {
        return in;
    }
}
