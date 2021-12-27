package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;

public class LineUpdateRequest {

	private String name;
	private String color;
	private int fare;

	public LineUpdateRequest() {
	}

	public LineUpdateRequest(String name, String color, int fare) {
		this.color = color;
		this.name = name;
		this.fare = fare;
	}

	public Line toLine() {
		return Line.of(name, color, fare);
	}

	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}

}
