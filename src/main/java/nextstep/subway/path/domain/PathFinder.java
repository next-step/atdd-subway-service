package nextstep.subway.path.domain;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import nextstep.subway.exception.AppException;
import nextstep.subway.exception.ErrorCode;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;

public class PathFinder {

	private final Sections sections;

	private PathFinder(Sections sections) {
		this.sections = sections;
	}

	public static PathFinder of(Sections sections) {
		return new PathFinder(sections);
	}

	public static PathFinder ofLines(List<Line> lines) {
		List<Section> sections = lines.stream()
			.map(Line::getSections)
			.flatMap(Collection::stream)
			.collect(Collectors.toList());
		return new PathFinder(Sections.of(sections));
	}

	public PathResponse findPath(Station source, Station target) {
		validateFindPath(source, target);
		DijkstraShortestPath path = new DijkstraShortestPath(this.sections.toGraph());
		List<Station> shortestPath = path.getPath(source, target).getVertexList();
		int distance = (int)path.getPathWeight(source, target);
		return PathResponse.of(shortestPath, distance);
	}

	private void validateFindPath(Station source, Station target) {
		if (source.equals(target)) {
			throw new AppException(ErrorCode.WRONG_INPUT, "출발역과 도착역이 같으면 안됩니다");
		}
		if (!sections.containStation(source) || !sections.containStation(target)) {
			throw new AppException(ErrorCode.WRONG_INPUT, "존재하지 않는 출발역과 도착역을 조회할 경우 안된다");
		}
		DijkstraShortestPath path = new DijkstraShortestPath(this.sections.toGraph());
		if (Objects.isNull(path.getPath(source, target))) {
			throw new AppException(ErrorCode.WRONG_INPUT, "출발역과 도착역이 연결되어 있어야 한다");
		}
	}

}
