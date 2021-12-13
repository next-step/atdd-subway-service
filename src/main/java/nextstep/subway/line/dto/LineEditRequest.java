package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;

public class LineEditRequest {
    private String name;
    private String color;
    private int extraFare;

    public LineEditRequest() {
    }

    public LineEditRequest(String name, String color, int extraFare) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public static LineEditRequest of(String name, String color, int extraFare) {
        return new LineEditRequest(name, color, extraFare);
    }

    public Line toLine() {
        return new Line(name, color, extraFare);
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getExtraFare() {
        return extraFare;
    }
}

