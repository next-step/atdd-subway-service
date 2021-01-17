package study.jgraph;

import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JgraphTest {

	/**
	 * 교대역    --- *2호선* ---   강남역
	 * |                        |
	 * *3호선*                *신분당선*
	 * |                        |
	 * 남부터미널역  --- *3호선* ---   양재
	 */
//    1 강남역
//    2 양재역
//    3 교대역
//    4 남부터미널역
	@Test
	public void findShotPathsInSubway() {
		String sourceId = "v4";
		String targetId = "v1";
		WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
		// sour ~ target 사이에 모든 id 구하기
		graph.addVertex("v1");
		graph.addVertex("v2");
		graph.addVertex("v3");
		graph.addVertex("v4");
		graph.setEdgeWeight(graph.addEdge("v1", "v2"), 10);
		graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
		graph.setEdgeWeight(graph.addEdge("v3", "v4"), 3);
		graph.setEdgeWeight(graph.addEdge("v4", "v1"), 10);

		DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
		GraphPath shortestPath = dijkstraShortestPath.getPath(sourceId, targetId);
		System.out.println(shortestPath.getVertexList());
		System.out.println(shortestPath.getWeight());
		List<String> shortestPathVertexs = dijkstraShortestPath.getPath(sourceId, targetId).getVertexList();
		System.out.println(shortestPathVertexs.toString());

		assertThat(shortestPathVertexs.size()).isEqualTo(2);
	}

	@Test
	public void getDijkstraShortestPath() {
		String source = "v3";
		String target = "v1";
		WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
		graph.addVertex("v1");
		graph.addVertex("v2");
		graph.addVertex("v3");
		graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
		graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
		graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

		DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
		List<String> shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();

		assertThat(shortestPath.size()).isEqualTo(3);
	}

	@Test
	public void getKShortestPaths() {
		String source = "v3";
		String target = "v1";

		WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
		graph.addVertex("v1");
		graph.addVertex("v2");
		graph.addVertex("v3");
		graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
		graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
		graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

		List<GraphPath> paths = new KShortestPaths(graph, 100).getPaths(source, target);

		assertThat(paths).hasSize(2);
		paths.stream()
				.forEach(it -> {
					assertThat(it.getVertexList()).startsWith(source);
					assertThat(it.getVertexList()).endsWith(target);
				});
	}
}
