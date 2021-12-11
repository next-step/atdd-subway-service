package nextstep.subway.path.domain;

import java.util.List;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class PathFinder {
	private static SubwayGraph graph2;

	private PathFinder() {
	}

	public static PathFinder create(List<Station> stations, List<Section> sections) {
		graph2 = new SubwayGraph(SectionEdge.class);
		stations.stream().forEach(station -> graph2.addVertex(station));
		sections.stream().forEach(section -> graph2.addSectionEdge(section));
		return new PathFinder();
	}

	public List<Station> findShortestPathVertexes(Station source, Station target) {
		return new DijkstraShortestPath(graph2).getPath(source, target).getVertexList();
	}

	public Distance findShortestPathDistance(Station source, Station target) {
		return Distance.of(new DijkstraShortestPath(graph2).getPath(source, target).getWeight());
	}

}
