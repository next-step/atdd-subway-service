package nextstep.subway.line.domain;

public class LineTestFixture {
    public static Line line(String name, String color, Section section) {
        return Line.of(name, color, section);
    }
}
