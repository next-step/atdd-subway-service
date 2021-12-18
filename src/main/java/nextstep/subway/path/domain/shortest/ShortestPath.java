package nextstep.subway.path.domain.shortest;

import java.util.List;
import java.util.stream.IntStream;

import nextstep.subway.fare.domain.DefaultFare;
import nextstep.subway.fare.domain.DistanceFare;
import nextstep.subway.fare.domain.LineFare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class ShortestPath {

	private final List<Line> lines;
	private final List<Station> stations;
	private final double distance;

	private ShortestPath(List<Line> lines, List<Station> stations, double distance) {
		this.lines = lines;
		this.stations = stations;
		this.distance = distance;
	}

	public static ShortestPath of(List<Line> lines, List<Station> stations, double distance) {
		return new ShortestPath(lines, stations, distance);
	}

	public List<Station> getStations() {
		return stations;
	}

	public double getDistance() {
		return distance;
	}

	public int getFare() {
		return IntStream.of(
			DefaultFare.calculate().getFare(),
			LineFare.calculate(lines).getFare(),
			DistanceFare.calculate(distance).getFare()
		).sum();
	}
}
