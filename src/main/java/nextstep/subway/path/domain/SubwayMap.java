package nextstep.subway.path.domain;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineSections;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class SubwayMap {
	private List<Line> lines;
	private LineSections lineSections;

	protected SubwayMap() {
	}

	public SubwayMap(List<Line> lines) {
		this.lines = lines;
		this.lineSections = new LineSections(allSections());
	}

	public ShortestPath findShortestPath(Station sourceStation, Station targetStation) {
		DijkstraShortestPath dijkstraShortestPath = this.findDijkstraShortestPath();
		GraphPath path = dijkstraShortestPath.getPath(sourceStation, targetStation);

		if (path == null) {
			throw new IllegalArgumentException("출발역과 도착역이 연결되어있지 않은 경우, 최단 경로를 조회할 수 없습니다.");
		}

		int maxLineOverFare = findMaxOverFare(path.getVertexList());
		return new ShortestPath(path.getVertexList(), (int) Math.round(path.getWeight()), maxLineOverFare);
	}

	private DijkstraShortestPath findDijkstraShortestPath() {
		WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

		for(Section section : lineSections.getSections()) {
			graph.addVertex(section.getUpStation());
			graph.addVertex(section.getDownStation());
			graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
		}

		return new DijkstraShortestPath(graph);
	}

	protected List<Section> allSections() {
		return this.lines.stream()
			.map(Line::getSections)
			.flatMap(Collection::stream)
			.collect(Collectors.toList());
	}

	protected int findMaxOverFare(List<Station> stations) {
		int maxOverFare = 0;
		for(int i = 0; i < stations.size() - 1; i++) {
			maxOverFare = findMax(maxOverFare, findLineOverFare(stations.get(i), stations.get(i + 1)));
		}
		return maxOverFare;
	}

	private int findLineOverFare(Station upStation, Station downStation) {
		Section section = lineSections.findSectionByStations(upStation, downStation)
			.orElseThrow(() -> new IllegalArgumentException("등록되지 않은 구간이 있습니다."));
		return section.getLineOverFare();
	}

	private int findMax(int maxValue, int newValue) {
		if (maxValue < newValue) {
			return newValue;
		}
		return maxValue;
	}
}
