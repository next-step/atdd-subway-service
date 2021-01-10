package nextstep.subway.path.domain;

import nextstep.subway.common.Fare;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;

public class Path {

	private final GraphPath<Station, DefaultWeightedEdge> path;

	public Path(GraphPath<Station, DefaultWeightedEdge> path) {
		this.path = path;
	}

	public List<Station> getStations() {
		return path.getVertexList();
	}

	public Fare getFare(int age) {
		return null;
	}

	public Distance getDistance() {
		return null;
	}
}
