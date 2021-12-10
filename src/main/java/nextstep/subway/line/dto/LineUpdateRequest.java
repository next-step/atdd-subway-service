package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;

public class LineUpdateRequest {
    private final String name;
    private final String color;

    public LineUpdateRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line toLine() {
        return Line.of(name, color);
    }
}
