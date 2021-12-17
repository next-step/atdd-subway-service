package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.InputDataErrorCode;
import nextstep.subway.exception.InputDataErrorException;
import nextstep.subway.line.domain.*;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.TicketGate;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional(readOnly = true)
    public PathResponse findPath(Long sourceId, Long targetId, LoginMember loginMember) {
        Station sourceStation = stationRepository.findById(sourceId)
                .orElseThrow(() -> new InputDataErrorException(InputDataErrorCode.THERE_IS_NOT_SEARCHED_STATION));

        Station targetStation = stationRepository.findById(targetId)
                .orElseThrow(() -> new InputDataErrorException(InputDataErrorCode.THERE_IS_NOT_SEARCHED_STATION));

        List<Line> allLines = lineRepository.findAll();
        Path path = new Path(sourceStation, targetStation);
        List<Station> shortestPath = path.findShortestPath(allLines);
        Distance totalDistance = path.findShortestPathDistance(allLines, shortestPath);
        List<Section> sections = path.findSection(allLines, shortestPath);
        Fare bigSectionFare = path.calculateBigSectionFare(sections);
        Fare resultFare = TicketGate.calculateFare(bigSectionFare, totalDistance, loginMember);

        return PathResponse.of(shortestPath, totalDistance, resultFare);
    }
}
