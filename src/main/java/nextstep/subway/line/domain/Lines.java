package nextstep.subway.line.domain;

import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.station.domain.Station;

public class Lines {

	private List<Line> lines;

	public Lines(List<Line> lines) {
		this.lines = lines;
	}

	public List<Station> getStations() {
		return lines.stream()
			.flatMap(line -> line.getStations().stream())
			.distinct()
			.collect(Collectors.toList());
	}

	public List<Sections> getSectionsByLine() {
		return lines.stream()
			.map(Line::getSections)
			.collect(Collectors.toList());
	}

}
