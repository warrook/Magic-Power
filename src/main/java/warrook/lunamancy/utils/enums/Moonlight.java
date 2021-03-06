package warrook.lunamancy.utils.enums;

import net.minecraft.util.StringIdentifiable;

public enum Moonlight implements StringIdentifiable {
    WHITE("white"),
    BLACK("black"),
    BOTH("both"),
    NEITHER("neither");

    private final String name;
    private Moonlight(String string) {
        this.name = string;
    }

    @Override
    public String toString() {
        return this.asString();
    }

    @Override
    public String asString() {
        return this.name;
    }

    public static Moonlight fromString(String string) {
        if (string.equals(WHITE.name))
            return WHITE;
        if (string.equals(BLACK.name))
            return BLACK;
        if (string.equals(BOTH.name))
            return BOTH;
        return NEITHER;
    }

    public Moonlight next() {
        if (this == WHITE)
            return BLACK;
        if (this == BLACK)
            return BOTH;
        if (this == BOTH)
            return WHITE;
        //The Moonlight type should be set or unset explicitly.
        return NEITHER;
    }
}
