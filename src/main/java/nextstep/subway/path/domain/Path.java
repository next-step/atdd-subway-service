package nextstep.subway.path.domain;

import org.springframework.util.Assert;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Stations;

public class Path {

	private final Stations stations;
	private final Distance distance;
	private final Sections sections;

	private Path(Stations stations, Distance distance, Sections sections) {
		Assert.notNull(stations, "지하철 역 경로는 필수입니다.");
		Assert.notNull(distance, "거리는 필수입니다.");
		Assert.notNull(sections, "구간은 필수입니다.");
		this.stations = stations;
		this.distance = distance;
		this.sections = sections;
	}

	public static Path of(Stations stations, Distance distance, Sections sections) {
		return new Path(stations, distance, sections);
	}

	public Stations stations() {
		return stations;
	}

	public Distance distance() {
		return distance;
	}

	public Sections sections() {
		return sections;
	}
}


