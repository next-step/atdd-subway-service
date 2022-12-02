package nextstep.subway.line.domain;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

import static nextstep.subway.utils.Message.INVALID_EMPTY_STRING;

@Embeddable
public class Color {

    @Column(nullable = false)
    private String color;

    protected Color() {
    }

    private Color(String color) {
        this.color = color;
    }

    public static Color from(String color) {
        checkNotNull(color);
        return new Color(color);
    }

    private static void checkNotNull(String color) {
        if (color == null || color.isEmpty()) {
            throw new RuntimeException(INVALID_EMPTY_STRING);
        }
    }

    public String getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Color color1 = (Color) o;
        return Objects.equals(color, color1.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color);
    }
}
