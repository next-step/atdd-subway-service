package nextstep.subway.line.dto;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class LineRequest {
	private String name;
	private String color;
	private Long upStationId;
	private Long downStationId;
	private int distance;
	private int fare;

	public LineRequest() {
	}

	public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance, int extraFee) {
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

	public Line toLine() {
		return new Line(name, color, new Fare(fare));
	}

	public Line toLine(Station upStation, Station downStation) {
		Line line = toLine();
		line.addLineStation(new Section(line, upStation, downStation, new Distance(distance)));
		return line;
	}
}
