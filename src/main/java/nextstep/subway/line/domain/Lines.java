package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.exception.AppException;
import nextstep.subway.exception.ErrorCode;
import nextstep.subway.fare.domain.Fare;

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

	public Fare findMostExpensiveLineFare() {
		Line line = lines.stream()
			.max(Line::compareByFare)
			.orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "가장 비싼 노선을 찾을 수 없습니다"));
		return line.getFare();
	}

	public List<Section> getSections() {
		return this.lines.stream()
			.map(Line::getSections)
			.map(Sections::toList)
			.flatMap(Collection::stream)
			.collect(Collectors.toList());
	}

	public List<Line> toList() {
		return Collections.unmodifiableList(this.lines);
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
