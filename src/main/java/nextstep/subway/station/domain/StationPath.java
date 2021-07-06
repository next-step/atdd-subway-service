package nextstep.subway.station.domain;

import java.util.List;

import org.jgrapht.GraphPath;

import nextstep.subway.line.domain.SectionEdge;

public class StationPath {

	private GraphPath<Station, SectionEdge> stationPath;

	public StationPath(GraphPath<Station, SectionEdge> stationPath) {
		this.stationPath = stationPath;
	}

	public int getDistance() {
		return (int)stationPath.getWeight();
	}

	public List<SectionEdge> getEdgeList() {
		return stationPath.getEdgeList();
	}

	public List<Station> getVertexList() {
		return stationPath.getVertexList();
	}
}
