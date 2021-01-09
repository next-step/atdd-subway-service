package nextstep.subway.path.application;

import org.springframework.stereotype.Service;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.SubwayMap;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

@Service
public class PathService {
	private LineRepository lineRepository;
	private StationRepository stationRepository;

	public PathService(LineRepository lineRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	public PathResponse findShortestPath(PathRequest request) {
		if (request.isSourceEqualToTarget()) {
			throw new IllegalArgumentException("출발역과 도착역이 같은 경우, 최단 경로를 조회할 수 없습니다.");
		}
		SubwayMap subwayMap = new SubwayMap(lineRepository.findAll());
		Station sourceStation = stationRepository.findById(request.getSource())
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 출발역입니다."));
		Station targetStation = stationRepository.findById(request.getTarget())
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 도착역입니다."));

		return PathResponse.of(subwayMap.findShortestPath(sourceStation, targetStation));
	}
}
