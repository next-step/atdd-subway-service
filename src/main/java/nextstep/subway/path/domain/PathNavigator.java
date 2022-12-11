package nextstep.subway.path.domain;

import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.util.Assert;

import nextstep.subway.common.exception.InvalidDataException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

public class PathNavigator {

	private final ShortestPathAlgorithm<Station, SectionWeightEdge> shortestPath;

	private PathNavigator(Lines lines) {
		validateLines(lines);
		shortestPath = shortestPathAlgorithm(lines);
	}

	public static PathNavigator from(Lines lines) {
		return new PathNavigator(lines);
	}

	public Path path(Station source, Station target) {
		validateStations(source, target);
		GraphPath<Station, SectionWeightEdge> graphPath = graphPath(source, target);
		return Path.of(
			Stations.from(graphPath.getVertexList()),
			Distance.from(graphPath.getWeight()),
			sections(graphPath.getEdgeList()));
	}

	private GraphPath<Station, SectionWeightEdge> graphPath(Station source, Station target) {
		try {
			return shortestPath.getPath(source, target);
		} catch (IllegalArgumentException e) {
			throw new InvalidDataException(String.format("출발역(%s) 과 도착역(%s) 의 연결 정보를 찾을 수 없습니다.", source, target), e);
		}
	}

	public boolean isInvalidPath(Station source, Station target) {
		try {
			shortestPath.getPath(source, target);
			return false;
		} catch (IllegalArgumentException e) {
			return true;
		}
	}

	private void validateStations(Station source, Station target) {
		Assert.notNull(source, "출발역이 존재하지 않습니다.");
		Assert.notNull(target, "도착역이 존재하지 않습니다.");
		if (source.equals(target)) {
			throw new InvalidDataException(String.format("출발역과 도착역(%s)이 동일할 경우 조회가 불가합니다.", source));
		}
	}

	private Sections sections(List<SectionWeightEdge> edges) {
		return Sections.from(
			edges.stream()
				.map(SectionWeightEdge::section)
				.collect(Collectors.toList())
		);
	}

	private Graph<Station, SectionWeightEdge> stationGraph(Lines lines) {
		WeightedMultigraph<Station, SectionWeightEdge> graph = new WeightedMultigraph<>(SectionWeightEdge.class);
		lines.stationList().forEach(graph::addVertex);
		lines.sectionList().forEach(section -> {
			SectionWeightEdge edge = SectionWeightEdge.from(section);
			graph.addEdge(section.getUpStation(), section.getDownStation(), edge);
			graph.setEdgeWeight(edge, section.getDistance());
		});
		return graph;
	}

	private ShortestPathAlgorithm<Station, SectionWeightEdge> shortestPathAlgorithm(Lines lines) {
		return new DijkstraShortestPath<>(stationGraph(lines));
	}

	private void validateLines(Lines lines) {
		if (lines == null || lines.isEmpty()) {
			throw new IllegalArgumentException("노선이 존재하지 않습니다.");
		}
	}
}
