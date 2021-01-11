package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.domain.OptionalLoginMember;
import nextstep.subway.common.Fare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.LineMap;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathCalculateRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PathService {

	private final LineRepository lineRepository;
	private final StationRepository stationRepository;

	public PathService(LineRepository lines, StationRepository stationRepository) {
		this.lineRepository = lines;
		this.stationRepository = stationRepository;
	}

	public PathResponse calculatePath(OptionalLoginMember optionalLoginMember,
	                                  PathCalculateRequest pathCalculateRequest) {
		Path path = getPath(pathCalculateRequest);
		Optional<Integer> age = optionalLoginMember.optional().map(LoginMember::getAge);
		Fare fare = age.isPresent() ? path.getFare(age.get()) : path.getFare();
		return PathResponse.of(path.getStations(), path.getDistance(), fare);
	}

	private Path getPath(PathCalculateRequest pathCalculateRequest) {
		List<Station> findResult = stationRepository.findAllByIdIn(
				Arrays.asList(pathCalculateRequest.getSourceStationId(), pathCalculateRequest.getTargetStationId()));

		Station source = findResult.stream()
				.filter(station -> station.getId().equals(pathCalculateRequest.getSourceStationId()))
				.findFirst()
				.orElseThrow(() -> new PathCalculateException("존재하지 않는 출발역입니다."));
		Station target = findResult.stream()
				.filter(station -> station.getId().equals(pathCalculateRequest.getTargetStationId()))
				.findFirst()
				.orElseThrow(() -> new PathCalculateException("존재하지 않는 도착역입니다."));

		List<Line> allLines = lineRepository.findAll();
		return new LineMap(allLines).calculate(source, target);
	}
}
