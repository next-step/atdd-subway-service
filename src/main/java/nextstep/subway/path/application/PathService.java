package nextstep.subway.path.application;

import static nextstep.subway.errorMessage.ErrorMessage.NOT_FOUND_STATION;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.farePolicy.MemberDiscountPolicyService;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findPath(LoginMember loginMember, PathRequest pathRequest) {
        Station source = stationRepository.findById(pathRequest.getSource()).orElseThrow(() -> new RuntimeException(NOT_FOUND_STATION));
        Station target = stationRepository.findById(pathRequest.getTarget()).orElseThrow(() -> new RuntimeException(NOT_FOUND_STATION));

        List<Line> lines = lineRepository.findAll();

        Path path = Path.of(lines);
        return path.findShortestPath(source, target, MemberDiscountPolicyService.getPolicy(loginMember));
    }
}
