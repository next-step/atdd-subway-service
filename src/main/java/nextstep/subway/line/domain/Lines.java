package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import nextstep.subway.exception.AppException;
import nextstep.subway.exception.ErrorCode;
import nextstep.subway.station.domain.Station;

public class Lines {

	private List<Line> lines;

	private Lines(List<Line> lines) {
		this.lines = new ArrayList<>(lines);
	}

	public static Lines of(List<Line> lines) {
		return new Lines(lines);
	}

	public static Lines of() {
		return new Lines(new ArrayList<>());
	}

	public int findMostExpensiveLineFare(List<Station> stations) {
		Set<Line> filteredLines = findLinesByStations(stations);
		Line line = findMostExpensiveLine(filteredLines);
		return line.getFare();
	}

	private Set<Line> findLinesByStations(List<Station> stations) {
		return lines.stream()
			.filter(line -> line.containsAnySection(stations))
			.collect(Collectors.toSet());
	}

	private Line findMostExpensiveLine(Collection<Line> filteredLines) {
		return filteredLines.stream()
			.max(Line::compareByFare)
			.orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "가장 비싼 노선을 찾을 수 없습니다"));
	}

	public List<Section> getSections() {
		return this.lines.stream()
			.map(Line::getSections)
			.map(Sections::toList)
			.flatMap(Collection::stream)
			.collect(Collectors.toList());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Lines lines1 = (Lines)o;

		return lines.equals(lines1.lines);
	}

	@Override
	public int hashCode() {
		return lines.hashCode();
	}

}
