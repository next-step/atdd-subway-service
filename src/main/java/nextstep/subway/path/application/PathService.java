package nextstep.subway.path.application;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.application.FareService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.shortest.ShortestPath;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.exception.StationNotFoundException;

@Service
@Transactional(readOnly = true)
public class PathService {

	private final FareService fareService;
	private final StationRepository stationRepository;
	private final LineRepository lineRepository;

	public PathService(FareService fareService, StationRepository stationRepository, LineRepository lineRepository) {
		this.fareService = fareService;
		this.stationRepository = stationRepository;
		this.lineRepository = lineRepository;
	}

	public PathResponse findPath(LoginMember loginMember, Long sourceStationId, Long targetStationId) {
		validateStationIds(sourceStationId, targetStationId);
		final List<Station> stations = findStations(sourceStationId, targetStationId);
		final Station sourceStation = getStation(stations, sourceStationId);
		final Station targetStation = getStation(stations, targetStationId);

		final List<Line> lines = lineRepository.findAll();
		final ShortestPath shortestPath = Path.of(lines).findShortest(sourceStation, targetStation);
		return PathResponse.of(shortestPath, fareService.calculate(loginMember, shortestPath));
	}

	private void validateStationIds(Long sourceStationId, Long targetStationId) {
		if (Objects.equals(sourceStationId, targetStationId)) {
			throw new IllegalArgumentException("출발역과 도착역의 식별자는 서로 달라야 합니다.");
		}
	}

	private List<Station> findStations(Long... ids) {
		return stationRepository.findByIdIn(ids);
	}

	private Station getStation(List<Station> stations, Long id) {
		return stations.stream()
			.filter(station -> station.getId().equals(id))
			.findAny()
			.orElseThrow(StationNotFoundException::new);
	}
}
