package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;

import javax.validation.constraints.NotNull;

public class LineRequest {
    @NotNull(message = "노선 이름은 필수값입니다.")
    private String name;
    @NotNull(message = "노선 색상은 필수값입니다.")
    private String color;
    @NotNull(message = "노선 상생종점역은 필수값입니다.")
    private Long upStationId;
    @NotNull(message = "노선 하행종점역은 필수값입니다.")
    private Long downStationId;
    @NotNull(message = "종점역 간 거리는 필수값입니다.")
    private int distance;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public Line toLine() {
        return new Line(name, color);
    }
}
