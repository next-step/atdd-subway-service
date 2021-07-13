package nextstep.subway.path.domain;

import java.util.Collection;
import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.common.exception.InvalidPathException;
import nextstep.subway.common.exception.SameStationException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationsResponse;

public class PathFinder {

	private Station source;
	private Station target;
	WeightedMultigraph<Station, DefaultWeightedEdge> graph;
	GraphPath path;

	public PathFinder(Station source, Station target, List<Line> lines) {
		validate(source, target);
		this.source = source;
		this.target = target;
		this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
		this.path = getDijkstraShortestPath(lines);
	}

	public List<Station> findShortestPath() {
		if (path == null) {
			throw new InvalidPathException();
		}
		return path.getVertexList();
	}

	public PathResponse findShortestPathPathResponse() {
		List<Station> stations = findShortestPath();
		return PathResponse.of(StationsResponse.of(stations), shortestPathDistance());
	}

	public int shortestPathDistance() {
		return (int) path.getWeight();
	}

	private void setGraphByLine(List<Line> lines) {
		lines.stream()
			.map(Line::getSections)
			.map(Sections::getSections)
			.flatMap(Collection::stream)
			.forEach(section -> setEdgeAndVertex(section));
	}

	private void setEdgeAndVertex(Section section) {
		Station upStation = section.getUpStation();
		Station downStation = section.getDownStation();
		graph.addVertex(upStation);
		graph.addVertex(downStation);
		graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance());
	}

	private void validate(Station source, Station target) {
		if(source.equals(target)) {
			throw new SameStationException();
		}
	}

	private GraphPath getDijkstraShortestPath(List<Line> lines) {
		setGraphByLine(lines);
		DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath<>(graph);
		return dijkstraShortestPath.getPath(source, target);
	}
}
