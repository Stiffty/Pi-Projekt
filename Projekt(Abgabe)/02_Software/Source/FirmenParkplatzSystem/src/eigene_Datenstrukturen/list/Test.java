package eigene_Datenstrukturen.list;

public class Test {
    public static void main(String[] args) {
        List<String> s = new List<>();
        s.add("Hi1");
        s.add("Lol1");
        s.add("Lol");
        s.add("Lol3");
        s.add("Hi1");
        s.add("Lol1");
        s.add("Lol");
        s.add("Lol3");
        s.remove(4);
        for (int i = 0; i < s.length(); i++) {
            System.out.println(s.get(i));
        }

    }
}
