package study.jgraph;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.Test;

class JgraphTest {

	/**
	 * 'v1' - (100) - `v3`
	 *  |              |
	 * (2)            (2)
	 *  |              |
	 *  ----- 'v2' -----
	 *
	 * path : v3 -> v1 = 100
	 * path : v3 -> v2 -> v1 = 4
	 */
	@Test
	void getDijkstraShortestPath() {
		String source = "v3";
		String target = "v1";
		WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
		graph.addVertex("v1");
		graph.addVertex("v2");
		graph.addVertex("v3");
		graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
		graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
		graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

		DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
		List<String> shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();

		assertThat(shortestPath)
			.hasSize(3)
			.containsExactly("v3", "v2", "v1");
	}

	/**
	 * `v5` - (1)  -  'v4'
	 *  |		       |
	 * (1)            (1)
	 *  |			   |
	 * 'v1' - (100) - `v3`
	 *  |              |
	 * (2)            (2)
	 *  |              |
	 *  ----- 'v2' -----
	 *
	 * path : v3 -> v1 = 100
	 * path : v3 -> v2 -> v1 = 4
	 * path : v3 -> v4 -> v5 -> v1 = 3
	 */
	@Test
	void getDijkstraShortestPathStudyTest() {
		String source = "v3";
		String target = "v1";
		WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
		graph.addVertex("v1");
		graph.addVertex("v2");
		graph.addVertex("v3");
		graph.addVertex("v4");
		graph.addVertex("v5");
		graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
		graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
		graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);
		graph.setEdgeWeight(graph.addEdge("v4", "v3"), 1);
		graph.setEdgeWeight(graph.addEdge("v1", "v5"), 1);
		graph.setEdgeWeight(graph.addEdge("v4", "v5"), 1);

		DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
		List<String> shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();

		assertThat(shortestPath)
			.hasSize(4)
			.containsExactly("v3", "v4", "v5", "v1");
	}

	/**
	 * 'v1' - (100) - `v3`
	 *  |              |
	 * (2)            (2)
	 *  |              |
	 *  ----- 'v2' -----
	 *
	 * path : v3 -> v1 = 100
	 * path : v3 -> v2 -> v1 = 4
	 */
	@Test
	void getKShortestPaths() {
		String source = "v3";
		String target = "v1";

		WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
		graph.addVertex("v1");
		graph.addVertex("v2");
		graph.addVertex("v3");
		graph.addVertex("v4");
		graph.addVertex("v5");
		graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
		graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
		graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);
		graph.setEdgeWeight(graph.addEdge("v4", "v3"), 1);
		graph.setEdgeWeight(graph.addEdge("v1", "v5"), 1);
		graph.setEdgeWeight(graph.addEdge("v4", "v5"), 1);

		List<GraphPath> paths = new KShortestPaths(graph, 100).getPaths(source, target);

		assertThat(paths).hasSize(3);
		paths.forEach(it -> {
			assertThat(it.getVertexList()).startsWith(source);
			assertThat(it.getVertexList()).endsWith(target);
		});

		assertThat(paths.get(0).getVertexList()).containsExactly("v3", "v4", "v5", "v1");
		assertThat(paths.get(1).getVertexList()).containsExactly("v3", "v2", "v1");
		assertThat(paths.get(2).getVertexList()).containsExactly("v3", "v1");
	}
}
