package nextstep.subway.path.domain;

import java.util.List;
import java.util.Objects;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.AppException;
import nextstep.subway.exception.ErrorCode;
import nextstep.subway.fare.domain.AgeDiscountPolicy;
import nextstep.subway.fare.domain.DistanceChargePolicy;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

public class PathFinder {

	private static final Fare BASE_FARE = Fare.of(1_250);

	private final Sections sections;
	private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

	private PathFinder(Sections sections) {
		this.sections = sections;
		this.graph = getGraph(sections);
	}

	public static PathFinder of(Sections sections) {
		return new PathFinder(sections);
	}

	public PathResponse findPath(Station source, Station target) {
		return findPath(source, target, new LoginMember());
	}

	public PathResponse findPath(Station source, Station target, LoginMember member) {
		GraphPath<Station, DefaultWeightedEdge> graphPath = getGraphPath(source, target);
		Distance distance = Distance.of(graphPath.getWeight());
		List<Station> stations = graphPath.getVertexList();
		Fare fare = calculateFare(distance, Stations.of(stations), member);
		return PathResponse.of(stations, distance, fare);
	}

	private Fare calculateFare(Distance distance, Stations stations, LoginMember member) {
		Lines lines = sections.getLines(stations);
		Fare fare = BASE_FARE;
		fare = fare.add(DistanceChargePolicy.getFare(distance));
		fare = fare.add(lines.findMostExpensiveLineFare());
		if (member.isNull()) {
			return fare;
		}
		return AgeDiscountPolicy.discountByAge(fare, member.getAge());
	}

	private GraphPath<Station, DefaultWeightedEdge> getGraphPath(Station source, Station target) {
		validateSourceAndTarget(graph, source, target);
		DijkstraShortestPath<Station, DefaultWeightedEdge> path = new DijkstraShortestPath<>(graph);
		GraphPath<Station, DefaultWeightedEdge> graphPath = path.getPath(source, target);
		validatePath(graphPath);
		return graphPath;
	}

	private static WeightedMultigraph<Station, DefaultWeightedEdge> getGraph(Sections sections) {
		WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
		sections.toList().forEach(section -> addSection(graph, section));
		return graph;
	}

	private static void addSection(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Section section) {
		graph.addVertex(section.getUpStation());
		graph.addVertex(section.getDownStation());
		graph.setEdgeWeight(
			graph.addEdge(section.getUpStation(), section.getDownStation()),
			section.getDistance().toInt());
	}

	private void validateSourceAndTarget(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Station source,
		Station target) {
		if (source.equals(target)) {
			throw new AppException(ErrorCode.WRONG_INPUT, "출발역과 도착역이 같으면 안됩니다");
		}
		if (!graph.containsVertex(source) || !graph.containsVertex(target)) {
			throw new AppException(ErrorCode.WRONG_INPUT, "존재하지 않는 출발역과 도착역을 조회할 경우 안된다");
		}
	}

	private void validatePath(GraphPath<Station, DefaultWeightedEdge> graphPath) {
		if (Objects.isNull(graphPath)) {
			throw new AppException(ErrorCode.WRONG_INPUT, "출발역과 도착역이 연결되어 있어야 한다");
		}
	}

}
