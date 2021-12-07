package nextstep.subway.path.application;

import nextstep.subway.common.exception.ErrorCode;
import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathResultV2;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final PathSearch pathSearch;

    public PathService(LineRepository lineRepository, StationRepository stationRepository,
        PathSearch pathSearch) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.pathSearch = pathSearch;
    }

    @Transactional(readOnly = true)
    public PathResponse getShortestPath(Long source, Long target) {
        Station sourceStation = stationRepository.findById(source)
            .orElseThrow(() -> NotFoundException.of(ErrorCode.NOT_EMPTY));
        Station targetStation = stationRepository.findById(target)
            .orElseThrow(() -> NotFoundException.of(ErrorCode.NOT_EMPTY));

        PathResultV2 pathSearchResult = this.pathSearch.findShortestPath(lineRepository.findAll(),
            sourceStation, targetStation);

        return PathResponse.of(StationResponse.toList(pathSearchResult.getResult()),
            pathSearchResult.getWeight());
    }
}
