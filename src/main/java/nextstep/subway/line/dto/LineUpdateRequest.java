package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;

public class LineUpdateRequest {
    private final String name;
    private final String color;
    private final int fare;

    public LineUpdateRequest(String name, String color, int fare) {
        this.name = name;
        this.color = color;
        this.fare = fare;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Line toLine() {
        return new Line(name, color, fare);
    }
}
