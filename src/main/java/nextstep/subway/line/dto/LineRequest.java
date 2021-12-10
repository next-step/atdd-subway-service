package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;

public class LineRequest extends SectionRequest {
    private String name;
    private String color;
    private int surcharge;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        super(upStationId, downStationId, distance);
        this.name = name;
        this.color = color;
        this.surcharge = 0;
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance, int surcharge) {
        super(upStationId, downStationId, distance);
        this.name = name;
        this.color = color;
        this.surcharge = surcharge;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getSurcharge() {
        return surcharge;
    }

    public Line toLine() {
        return new Line(name, color, surcharge);
    }
}
