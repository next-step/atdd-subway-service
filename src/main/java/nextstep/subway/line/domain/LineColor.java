package nextstep.subway.line.domain;

import static nextstep.subway.line.enums.LineExceptionType.CANNOT_EMPTY_LINE_COLOR;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import nextstep.subway.utils.StringUtils;

@Embeddable
public class LineColor {

    @Column(name = "color")
    private String color;

    protected LineColor() {}

    private LineColor(String color) {
        this.color = color;
    }

    public static LineColor from(String color) {
        validateLineColor(color);
        return new LineColor(color);
    }

    public String getValue() {
        return this.color;
    }

    private static void validateLineColor(String color) {
        if (StringUtils.isEmpty(color)) {
            throw new IllegalArgumentException(CANNOT_EMPTY_LINE_COLOR.getMessage());
        }
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
        return Objects.equals(color, lineColor.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color);
    }
}
