package nextstep.subway.line.domain;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.exception.InvalidLineException;
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

	public Fare getMaxLineFare() {
		return getDescendingOrderLines().stream()
			.findFirst()
			.orElseThrow(() -> new InvalidLineException("Lines에 Line이 존재하지 않아 가장 비싼 요금을 조회할 수 없습니다."))
			.getFare();
	}

	private List<Line> getDescendingOrderLines() {
		return getSortLines((line, lineOther) -> (lineOther.minusFare(line)));
	}

	private List<Line> getSortLines(Comparator<Line> predicate) {
		return lines.stream()
			.sorted(predicate)
			.collect(Collectors.toList());
	}

}
