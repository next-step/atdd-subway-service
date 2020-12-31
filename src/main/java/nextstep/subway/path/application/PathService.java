package nextstep.subway.path.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PathService {

	private final LineService lineService;

	public PathResponse getShortestPath(Long startStationId, Long destinationStationId, Integer age) {

		List<Line> lines = lineService.findAll();
		PathFinder pathFinder = new PathFinder(lines, startStationId, destinationStationId, age);
		return pathFinder.getShortestPathResponse();
	}
}
