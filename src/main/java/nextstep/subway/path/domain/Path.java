package nextstep.subway.path.domain;

import java.util.List;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class Path {
	private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
	private final DijkstraShortestPath dijkstraShortestPath;

	private final List<Line> lines;
	private final Station source;
	private final Station target;

	public Path(List<Line> lines, Station source, Station target) {
		validateSourceIsEqualsToTarget(source, target);
		validateDoesNotBelongToLines(lines, source, target);
		addVertexes(lines);
		addEdgeWeights(lines);

		this.lines = lines;
		this.source = source;
		this.target = target;

		dijkstraShortestPath = new DijkstraShortestPath(graph);
		validateConnectedSourceToTarget(source, target);
	}

	private void validateConnectedSourceToTarget(Station source, Station target) {
		if (dijkstraShortestPath.getPath(source, target) == null) {
			throw new IllegalArgumentException("출발역과 도착역이 연결되어 있지 않습니다.");
		}
	}

	private void validateDoesNotBelongToLines(List<Line> lines, Station source, Station target) {
		if (!hasStation(lines, source) || !hasStation(lines, target)) {
			throw new IllegalArgumentException("출발역 또는 도착역이 존재하지 않습니다.");
		}
	}

	private boolean hasStation(List<Line> lines, Station station) {
		return lines.stream()
			.flatMap(line -> line.getStations().stream())
			.distinct()
			.anyMatch(s -> s.equals(station));
	}

	private void validateSourceIsEqualsToTarget(Station source, Station target) {
		if (source.equals(target)) {
			throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
		}
	}

	private void addEdgeWeights(List<Line> lines) {
		lines.stream()
			.map(Line::getSections)
			.forEach(sections -> sections.setEdgeWeight(graph));
	}

	private void addVertexes(List<Line> lines) {
		lines.stream()
			.flatMap(line -> line.getStations().stream())
			.distinct()
			.forEach(station -> graph.addVertex(station));
	}

	public List<Station> getShortestStations() {
		return dijkstraShortestPath.getPath(source, target).getVertexList();
	}

	public int getShortestDistance() {
		return (int) dijkstraShortestPath.getPath(source, target).getWeight();
	}

	public int calcFare(Integer age) {
		int distance = getShortestDistance();

		if (age != null) {
			return AgeFarePolicy.fare(DistanceFarePolicy.fare(distance), age) + LineFarePolicy.fare(lines);
		}

		return DistanceFarePolicy.fare(distance) + LineFarePolicy.fare(lines);
	}
}
