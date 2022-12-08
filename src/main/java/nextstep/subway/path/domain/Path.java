package nextstep.subway.path.domain;

import java.util.List;

import org.springframework.util.Assert;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class Path {

	private final List<Station> stations;
	private final Distance distance;
	private final List<Section> sections;

	private Path(List<Station> stations, Distance distance, List<Section> sections) {
		Assert.notNull(stations, "지하철 역 경로는 필수입니다.");
		Assert.notNull(distance, "거리는 필수입니다.");
		Assert.notNull(sections, "구간은 필수입니다.");
		this.stations = stations;
		this.distance = distance;
		this.sections = sections;
	}

	public static Path of(List<Station> stations, Distance distance, List<Section> sections) {
		return new Path(stations, distance, sections);
	}

	public List<Station> getStations() {
		return stations;
	}

	public Distance getDistance() {
		return distance;
	}

	public List<Section> getSections() {
		return sections;
	}
}


