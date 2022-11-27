package nextstep.subway.line.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Color {

    private static final String ROUTE_COLOR_ERROR = "노선 색상을 지정해주세요.";

    @Column(nullable = false)
    private String color;

    protected Color() {
    }

    private Color(String color) {
        this.color = color;
    }

    public static Color from(String color) {
        if (Objects.isNull(color) || color.trim().isEmpty()) {
            throw new IllegalArgumentException(ROUTE_COLOR_ERROR);
        }
        return new Color(color);
    }

    public String value() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Color color1 = (Color) o;
        return Objects.equals(color, color1.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color);
    }
}