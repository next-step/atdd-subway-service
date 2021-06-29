package nextstep.subway.path.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.station.NoStationException;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.LinePathSearch;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.SectionEdge;
import nextstep.subway.path.domain.calculator.FareCalculatorService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
@Transactional
public class PathService {

    private final LineService lineService;
    private final StationService stationService;
    private FareCalculatorService fareCalculatorService;

    public PathService(LineService lineService, StationService stationService,
            FareCalculatorService fareCalculatorService) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.fareCalculatorService = fareCalculatorService;
    }

    @Transactional(readOnly = true)
    public PathResponse findPathBySourceAndTarget(Long sourceId, Long targetId, LoginMember loginMember) {
        List<Section> allSections = lineService.getAllSections();

        Station source = stationService.findByIdWithError(sourceId,
            new NoStationException(NoStationException.NO_UPSTAION));

        Station target = stationService.findByIdWithError(targetId,
            new NoStationException(NoStationException.NO_DOWNSTATION));
        Path path = LinePathSearch.of(allSections).searchPath(source, target);
        int lineAddPrice = checkAddFareLine(path.getsectionEdges());
        int subwayFare = fareCalculatorService.getPrice(path.getMinDistance(), loginMember, lineAddPrice);

        return PathResponse.of(path.getStations(), path.getMinDistance(), subwayFare);
    }

    private int checkAddFareLine(List<SectionEdge> sectionEdges) {
        return sectionEdges.stream()
            .mapToInt(edge -> edge.getAddFare())
            .max()
            .orElse(0);
    }

}
