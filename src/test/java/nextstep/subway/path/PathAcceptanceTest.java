package nextstep.subway.path;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.AcceptanceTest;

@DisplayName("최단 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {


	@DisplayName("jgrapht 라이브러리 테스트")
	@Test
	void jgrapght_libray_test() {
		WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
		graph.addVertex("v1");
		graph.addVertex("v2");
		graph.addVertex("v3");
		graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
		graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
		graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

		DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
		List<String> shortestPath = dijkstraShortestPath.getPath("v3", "v1").getVertexList();
		double weight = dijkstraShortestPath.getPath("v3", "v1").getWeight();

		assertThat(shortestPath).containsExactly("v3", "v2", "v1");
		assertThat((int)weight).isEqualTo(4);
	}
}
