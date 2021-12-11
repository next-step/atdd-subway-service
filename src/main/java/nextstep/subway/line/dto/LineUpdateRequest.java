package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;

public class LineUpdateRequest {
    private final String name;
    private final String color;
    private final int additionalFare;

    public LineUpdateRequest(String name, String color, int additionalFare) {
        this.name = name;
        this.color = color;
        this.additionalFare = additionalFare;
    }

    public Line toLine() {
        return Line.of(name, color);
    }
}
