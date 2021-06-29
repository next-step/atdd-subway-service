package nextstep.subway.path.domain;

import org.assertj.core.api.Assertions;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.Test;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

class FareCalculatorTest {

	@Test
	void testCalculateFare() {

		Member loginMember = new Member("email@email.com", "123", 20);
		FareCalculator calculator = new FareCalculator();

		WeightedMultigraph<Station, SectionWeightedEdge> pathGraph = new WeightedMultigraph<>(
			SectionWeightedEdge.class);
		Station 강남역 = new Station("강남역");
		Station 역삼역 = new Station("역삼역");
		pathGraph.addVertex(강남역);
		pathGraph.addVertex(역삼역);
		SectionWeightedEdge edge = new SectionWeightedEdge(강남역, 역삼역, 500);
		pathGraph.addEdge(강남역, 역삼역, edge);
		pathGraph.setEdgeWeight(edge, 5);

		DijkstraShortestPath<Station, SectionWeightedEdge> shortestPath = new DijkstraShortestPath<>(pathGraph);
		GraphPath<Station, SectionWeightedEdge> path = shortestPath.getPath(강남역, 역삼역);
		int fare = calculator.calculateFare(path, loginMember);

		Assertions.assertThat(fare).isEqualTo(1750);
	}
}
