package nextstep.subway.line.domain;

import nextstep.subway.exception.EmptyLineColorException;
import nextstep.subway.message.ExceptionMessage;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class LineColor {

    @Column(nullable = false)
    private String color;

    protected LineColor() {
    }

    private LineColor(String color) {
        this.color = color;
    }

    public static LineColor from(String color) {
        checkNotNull(color);
        return new LineColor(color);
    }

    private static void checkNotNull(String color) {
        if (color == null || color.isEmpty()) {
            throw new EmptyLineColorException(ExceptionMessage.EMPTY_LINE_COLOR);
        }
    }

    public String getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineColor lineColor = (LineColor) o;
        return color.equals(lineColor.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color);
    }
}
