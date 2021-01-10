package nextstep.subway.path.domain;

import nextstep.subway.common.Fare;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

import java.util.List;

public class Path {

	private final GraphPath<Station, LineEdge> path;

	public Path(GraphPath<Station, LineEdge> path) {
		this.path = path;
	}

	public List<Station> getStations() {
		return path.getVertexList();
	}

	public Fare getFare(int age) {
		return null;
	}

	public Distance getDistance() {
		return path.getEdgeList().stream()
				.map(LineEdge::getDistance)
				.reduce(Distance::plus)
				.orElseGet(() -> new Distance(0));
	}
}
