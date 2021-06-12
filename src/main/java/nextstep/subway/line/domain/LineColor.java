package nextstep.subway.line.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class LineColor {
    private String color;

    protected LineColor() {
    }

    public LineColor(String name) {
        validate(name);

        this.color = name;
    }

    private void validate(String name) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("색깔은 공백이거나 공백이면 안됩니다");
        }
    }

    @Override
    public String toString() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineColor lineName = (LineColor) o;
        return Objects.equals(color, lineName.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color);
    }
}
