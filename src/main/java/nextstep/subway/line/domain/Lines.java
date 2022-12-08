package nextstep.subway.line.domain;

import java.util.List;

import io.jsonwebtoken.lang.Assert;
import nextstep.subway.station.domain.Station;

public class Lines {

	private final List<Line> lines;

	private Lines(List<Line> lines) {
		Assert.notNull(lines, "지하철 노선 목록은 필수입니다.");
		this.lines = lines;
	}

	public static Lines from(List<Line> lines) {
		return new Lines(lines);
	}

	public boolean isEmpty() {
		return lines.isEmpty();
	}

	public List<Station> stationList() {
		List<Station> stations = null;
		for (Line line : lines) {
			stations.addAll(line.allStations());
		}
		return stations;
	}
}
