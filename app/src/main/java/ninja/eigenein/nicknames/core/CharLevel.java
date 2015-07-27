package ninja.eigenein.nicknames.core;

public class CharLevel {

    private final char aChar;
    private final double level;

    public CharLevel(final char aChar, final double level) {
        this.aChar = aChar;
        this.level = level;
    }

    public char getChar() {
        return aChar;
    }

    public double getLevel() {
        return level;
    }
}
