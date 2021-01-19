package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.path.domain.fare.Money;
import nextstep.subway.station.domain.Station;

public class Lines {
	private final List<Line> lines;

	private Lines(List<Line> lines) {
		this.lines = lines;
	}

	public static Lines of(List<Line> lines) {
		return new Lines(lines);
	}

	public List<Station> stations() {
		return lines.stream()
			.flatMap(it -> it.stations().stream())
			.collect(Collectors.toList());
	}

	public List<Section> sections() {
		return lines.stream()
			.flatMap(it -> it.sections().stream())
			.collect(Collectors.toList());
	}

	public Money maximumLineFare(List<Station> stations) {
		return findSections(stations).stream()
			.map(Section::fare)
			.max(Comparator.comparingLong(Money::getMoney))
			.orElse(Money.of(0));
	}

	private List<Section> findSections(List<Station> stations) {

		List<Section> sections = new ArrayList<>();

		for (int i = 1; i < stations.size(); i++) {
			Station upStation = stations.get(i - 1);
			Station downStation = stations.get(i);
			this.sections().stream()
				.filter(it -> it.isMatchStation(upStation, downStation))
				.findFirst()
				.ifPresent(sections::add);
		}
		return sections;
	}
}
