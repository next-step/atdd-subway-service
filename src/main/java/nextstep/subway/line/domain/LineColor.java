package nextstep.subway.line.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class LineColor {
    @Column(name = "color")
    private String value;

    protected LineColor() {
    }

    public LineColor(String value) {
        validate(value);
        this.value = value;
    }

    public static LineColor from(String color) {
        return new LineColor(color);
    }

    private void validate(String name) {
        validateNonNull(name);
        validateEmptyString(name);
    }

    private void validateNonNull(String name) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException("노선 색상은 필수값 입니다.");
        }
    }

    private void validateEmptyString(String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("노선 색상이 빈 문자열입니다.");
        }
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LineColor lineColor = (LineColor) o;
        return Objects.equals(value, lineColor.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
