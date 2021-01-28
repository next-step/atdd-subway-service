package nextstep.subway.path.domain;

import java.util.List;

import nextstep.subway.line.domain.Line;

public class Lines {
	private List<Line> lines;

	public Lines(List<Line> lines) {
		this.lines = lines;
	}

	public int calculateOverFare() {
		return lines.stream()
			.map(Line::getOverFare)
			.reduce(Math::max)
			.orElse(0);
	}
}
