package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class LineColor {
    @Column(name = "color", unique = true)
    private String value;

    protected LineColor() {
    }

    public LineColor(String color) {
        if (Objects.isNull(color) || color.trim().isEmpty()) {
            throw new IllegalArgumentException("노선 색상을 지정해주세요.");
        }

        this.value = color;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LineColor lineColor = (LineColor) o;

        return Objects.equals(value, lineColor.value);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
