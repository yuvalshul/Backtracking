
public final class IntegrityStatement {
    public static String signature() {
        String names = "Nave Sarfati & Yuval Shulman"; // <- Fill in your names here!
        if (names.length() == 0) {
            throw new UnsupportedOperationException("You should sign here");
        }
        return names;
    }
}
