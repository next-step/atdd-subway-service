package nextstep.subway.station.domain;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

public class StationPath {

	private GraphPath<Station, DefaultWeightedEdge> stationPath;

	public StationPath(GraphPath<Station, DefaultWeightedEdge> stationPath) {
		this.stationPath = stationPath;
	}

	public int getDistance() {
		return (int)stationPath.getWeight();
	}

	public List<Station> getStations() {
		return stationPath.getVertexList();
	}
}
