package nextstep.subway.path.application;

import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.route.PathRoute;
import nextstep.subway.path.exception.NotFoundPathException;
import nextstep.subway.path.util.SectionWeightEdge;
import nextstep.subway.station.domain.Station;

@Component
public class DijkstraPathFinder implements PathFinder {

	@Override
	public PathRoute findShortestPath(List<Line> lines, Station departStation, Station arriveStation) {

		WeightedMultigraph<Station, SectionWeightEdge> graph = new WeightedMultigraph(SectionWeightEdge.class);
		lines.stream()
			.map(line -> line.getSections())
			.flatMap(sections -> sections.getSections().stream())
			.forEach(section -> {
				graph.addVertex(section.getUpStation());
				graph.addVertex(section.getDownStation());

				SectionWeightEdge sectionEdge = new SectionWeightEdge(section);
				graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
				graph.setEdgeWeight(sectionEdge, section.getDistance());
			});

		DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
		GraphPath path = dijkstraShortestPath.getPath(departStation, arriveStation);
		validateExistPath(path);

		List<SectionWeightEdge> edges = path.getEdgeList();

		return new PathRoute((int)path.getWeight(), path.getVertexList(),
			edges.stream()
				.map(SectionWeightEdge::getSection)
				.collect(Collectors.toList()));
	}

	private void validateExistPath(GraphPath path) {
		if (path == null) {
			throw new NotFoundPathException();
		}
	}
}