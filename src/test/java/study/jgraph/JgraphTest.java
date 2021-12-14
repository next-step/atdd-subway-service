package study.jgraph;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JgraphTest {

	/*
	  V3 --(11)-- V2
	  |           |
	  (3)        (10)
	  |           |
	  V4 --(2)-- V1
	 */
	WeightedMultigraph<String, DefaultWeightedEdge> 그래프_V1_V2_V3_V4;

	/*
	  V3 --(2)-- V2
	  |           |
	(100)        (2)
	  |           |
	  ----  V1 ----
	 */
	WeightedMultigraph<String, DefaultWeightedEdge> 그래프_V1_V2_V3;

	@BeforeEach
	public void setup() {
		WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
		graph.addVertex("v1");
		graph.addVertex("v2");
		graph.addVertex("v3");
		graph.addVertex("v4");
		graph.setEdgeWeight(graph.addEdge("v1", "v2"), 10);
		graph.setEdgeWeight(graph.addEdge("v2", "v3"), 11);
		graph.setEdgeWeight(graph.addEdge("v1", "v4"), 2);
		graph.setEdgeWeight(graph.addEdge("v4", "v3"), 3);
		this.그래프_V1_V2_V3_V4 = graph;

		WeightedMultigraph<String, DefaultWeightedEdge> graph2 = new WeightedMultigraph(DefaultWeightedEdge.class);
		graph2.addVertex("v1");
		graph2.addVertex("v2");
		graph2.addVertex("v3");
		graph2.setEdgeWeight(graph2.addEdge("v1", "v2"), 2);
		graph2.setEdgeWeight(graph2.addEdge("v2", "v3"), 2);
		graph2.setEdgeWeight(graph2.addEdge("v1", "v3"), 100);
		this.그래프_V1_V2_V3 = graph2;
	}

	/*
	  V3 --(2)-- V2
	  |           |
	(100)        (2)
	  |           |
	  ----  V1 ----
	 */
	@DisplayName("최단 경로를 구한다")
	@Test
	public void getDijkstraShortestPath() {
		String source = "v3";
		String target = "v1";

		DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(그래프_V1_V2_V3);
		List<String> shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();

		assertThat(shortestPath.size()).isEqualTo(3);
	}

	/*
	  V3 --(2)-- V2
	  |           |
	(100)        (2)
	  |           |
	  ----  V1 ----
	 */
	@DisplayName("최단 경로들을 구한다")
	@Test
	public void getKShortestPaths() {
		String source = "v3";
		String target = "v1";

		List<GraphPath> paths = new KShortestPaths(그래프_V1_V2_V3, 100).getPaths(source, target);

		assertThat(paths).hasSize(2);
		paths.stream()
			.forEach(it -> {
				assertThat(it.getVertexList()).startsWith(source);
				assertThat(it.getVertexList()).endsWith(target);
			});
	}

	/*
	  V3 --(11)-- V2
	  |           |
	  (3)        (10)
	  |           |
	  V4 --(2)-- V1
	*/
	@DisplayName("중복된 vertex에서도, 최단 경로를 구한다")
	@Test
	public void duplicateVertexTest() {
		String source = "v1";
		String target = "v3";

		WeightedMultigraph<String, DefaultWeightedEdge> graph = 그래프_V1_V2_V3_V4;
		// 중복된 vertex 추가
		graph.addVertex("v2");
		graph.addVertex("v2");
		graph.addVertex("v3");
		graph.addVertex("v4");

		DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
		GraphPath graphPathPath = dijkstraShortestPath.getPath(source, target);

		assertAll(
			() -> assertThat(graphPathPath.getWeight()).isEqualTo(5),
			() -> assertThat(graphPathPath.getVertexList()).containsExactly("v1", "v4", "v3")
		);

	}

	/*
	  V3 --(11)-- V2
	  |           |
	  (3)        (10)
	  |           |
	  V4 --(2)-- V1
	  V4 --(3)-- V1
	 */
	@DisplayName("동일한 구간의 edge가 있어도, 짧은 구간으로 최단 경로를 구한다")
	@Test
	public void duplicateEdgeTest() {
		String source = "v1";
		String target = "v3";

		WeightedMultigraph<String, DefaultWeightedEdge> graph = 그래프_V1_V2_V3_V4;
		// 동일한 구간이면서 거리가 긴 경로 추가 (V1-V4, 길이 3)
		graph.setEdgeWeight(graph.addEdge("v1", "v4"), 3);
		graph.setEdgeWeight(graph.addEdge("v4", "v3"), 3);

		DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
		GraphPath graphPathPath = dijkstraShortestPath.getPath(source, target);

		assertAll(
			() -> assertThat(graphPathPath.getWeight()).isEqualTo(5),
			() -> assertThat(graphPathPath.getVertexList()).containsExactly("v1", "v4", "v3")
		);
	}

}
