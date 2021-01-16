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

	public List<Section> findSections(List<Station> stations) {

		List<Section> sections = new ArrayList<>();
		Station firstStation = stations.get(0);

		for (int i = 1; i < stations.size(); i++) {
			Station upStation = firstStation;
			Station downStation = stations.get(i);

			sections.add(this.sections().stream()
				.filter(it -> it.isMatchStation(upStation, downStation))
				.findFirst()
				.orElse(null));
			firstStation = downStation;
		}
		return sections;
	}

	public Money maximumLineFare(List<Station> stations) {
		return findSections(stations).stream()
			.map(Section::fare)
			.max(Comparator.comparingLong(Money::getMoney))
			.orElse(Money.of(0));
	}
}
