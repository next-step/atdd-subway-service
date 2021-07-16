package nextstep.subway.line.dto;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LineRequest {
    @NotBlank(message = "노선의 이름을 공백일 수 없습니다.")
    private String name;
    @NotBlank(message = "노선의 색깔은 공백일 수 없습니다.")
    private String color;
    @NotNull(message = "상행 역의 아이디를 넣어주세요.")
    @Min(value = 1, message = "상행 역의 아이디는 0이하 일 수 없습니다.")
    private Long upStationId;
    @NotNull(message = "하행 역의 아이디를 넣어주세요.")
    @Min(value = 1, message = "하행 역의 아이디는 0이하 일 수 없습니다.")
    private Long downStationId;
    @NotNull
    @Min(value = 1, message = "노선에 속한 구간의 거리는 0 이하 일 수 없습니다.")
    private int distance;
    @NotNull
    @Min(value = 0, message = "노선의 요금은 0 미만 일 수 없습니다.")
    private int fare;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.fare = 0;
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance, int fare) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.fare = fare;
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

    public int getFare() {
        return fare;
    }

    public Line toLine(Station upStation, Station downStation) {
        return new Line(name, color, upStation, downStation, new Distance(distance), new Fare(fare));
    }
}
