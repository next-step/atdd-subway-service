package nextstep.subway.path.domain;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineSections;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class SubwayMap {
	private List<Line> lines;
	private LineSections lineSections;
	private PathFinder pathFinder;

	protected SubwayMap() {
	}

	public SubwayMap(List<Line> lines, PathFinder pathFinder) {
		this.lines = lines;
		this.pathFinder = pathFinder;
		this.lineSections = new LineSections(allSections());
	}

	public ShortestPath findShortestPathAndFare(Station sourceStation, Station targetStation, int age) {
		ShortestPath path = pathFinder.findShortestPath(lineSections, sourceStation, targetStation);
		path.calculateLineOverFareAndAgeDiscount(findMaxOverFare(path.getStations()), age);
		return path;
	}

	protected List<Section> allSections() {
		return this.lines.stream()
			.map(Line::getSections)
			.flatMap(Collection::stream)
			.collect(Collectors.toList());
	}

	protected int findMaxOverFare(List<Station> stations) {
		int maxOverFare = 0;
		for(int i = 0; i < stations.size() - 1; i++) {
			maxOverFare = findMax(maxOverFare, findLineOverFare(stations.get(i), stations.get(i + 1)));
		}
		return maxOverFare;
	}

	private int findLineOverFare(Station upStation, Station downStation) {
		Section section = lineSections.findSectionByStations(upStation, downStation)
			.orElseThrow(() -> new IllegalArgumentException("등록되지 않은 구간이 있습니다."));
		return section.getLineOverFare();
	}

	private int findMax(int maxValue, int newValue) {
		return Math.max(maxValue, newValue);
	}
}
