package nextstep.subway.path.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ShortestPathFinderTest {
	@Test
	@DisplayName("")
	void name() {

	}


	@Test
	@DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 익셉션 발생")
	void findPathValidationTest() {
	}


	@Test
	void getShortestPath() {
		WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
		graph.addVertex("v1");
		graph.addVertex("v2");
		graph.addVertex("v3");
		graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
		graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
		graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

		DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
		List<String> shortestPath = dijkstraShortestPath.getPath("v3", "v1").getVertexList();

		assertThat(shortestPath.size()).isEqualTo(3);
		assertThat(dijkstraShortestPath.getPathWeight("v3", "v1")).isEqualTo(4);
	}
}