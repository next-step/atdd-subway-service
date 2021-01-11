package study.jgraph;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.Test;

public class JgraphTest {

	@Test
	public void exceptionPath() {
		String source = "v6";
		String target = "v1";
		WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
		graph.addVertex("v1");
		graph.addVertex("v2");
		graph.addVertex("v3");
		graph.addVertex("v4");
		graph.addVertex("v5");
		graph.addVertex("v6");

		graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
		graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
		graph.setEdgeWeight(graph.addEdge("v3", "v4"), 2);
		graph.setEdgeWeight(graph.addEdge("v4", "v5"), 2);
		graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

		List<GraphPath> paths = new KShortestPaths(graph, 100).getPaths(source, target);

		System.out.println(paths);
		System.out.println(paths.size());
		System.out.println(paths.get(0).getVertexList());
		System.out.println(paths.get(0).getEdgeList());
		System.out.println(paths.get(0).getLength());
		System.out.println(paths.get(0).getWeight());
		//DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
		//List<String> shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();

		//System.out.println(shortestPath);
		//assertThat(shortestPath.size()).isEqualTo(3);
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

		System.out.println(shortestPath);
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
