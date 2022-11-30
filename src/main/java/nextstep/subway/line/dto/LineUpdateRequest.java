package nextstep.subway.line.dto;

import nextstep.subway.common.domain.Name;
import nextstep.subway.line.domain.Color;

public class LineUpdateRequest {
	private String name;
	private String color;

	public LineUpdateRequest(String name, String color) {
		this.name = name;
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}

	public Name name() {
		return Name.from(name);
	}

	public Color color() {
		return Color.from(color);
	}
}
