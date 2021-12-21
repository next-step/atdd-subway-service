package nextstep.subway.path.domain;

import java.util.List;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class PathFinder {
	private static SubwayGraph graph;

	private PathFinder() {
	}

	public static PathFinder create(List<Station> stations, List<Section> sections) {
		graph = new SubwayGraph(SectionEdge.class);
		stations.stream().forEach(station -> graph.addVertex(station));
		sections.stream().forEach(section -> graph.addSectionEdge(section));
		return new PathFinder();
	}

	public List<Station> findShortestPathVertexes(Station source, Station target) {
		return new DijkstraShortestPath(graph).getPath(source, target).getVertexList();
	}

	public Distance findShortestPathDistance(Station source, Station target) {
		return Distance.of(new DijkstraShortestPath(graph).getPath(source, target).getWeight());
	}

	public List<SectionEdge> findShortestPathEdges(Station source, Station target) {
		return new DijkstraShortestPath(graph).getPath(source, target).getEdgeList();
	}

	public List<Station> findShortestPathVertexes(SectionEdge pathFinderParam) {
		return new DijkstraShortestPath(graph).getPath(pathFinderParam.getSource(), pathFinderParam.getTarget()).getVertexList();
	}

	public Distance findShortestPathDistance(SectionEdge pathFinderParam) {
		return Distance.of(new DijkstraShortestPath(graph).getPath(pathFinderParam.getSource(), pathFinderParam.getTarget()).getWeight());
	}

	public List<SectionEdge> findShortestPathEdges(SectionEdge pathFinderParam) {
		return new DijkstraShortestPath(graph).getPath(pathFinderParam.getSource(), pathFinderParam.getTarget()).getEdgeList();
	}

}
