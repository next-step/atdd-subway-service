package nextstep.subway.path.application;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathStationDto;
import nextstep.subway.path.dto.ShortestPathInfo;
import nextstep.subway.path.infrastructure.PathAnalysis;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @PostConstruct
    public void init() {
        Sections sections = lineService.findAllSections();
        PathAnalysis.getInstance().initialze(sections);
    }
    
    public PathResponse searchShortestPath(Long sourceStationId, Long targetStationId) {
        vaildateShortestPath(sourceStationId, targetStationId);

        Station source = stationService.findById(sourceStationId);
        Station target = stationService.findById(targetStationId);

        ShortestPathInfo shortestPathInfo = PathAnalysis.getInstance().findShortestPaths(source, target);
        
        return createpathResponse(shortestPathInfo);
    }

    private void vaildateShortestPath(Long sourceStationId, Long targetStationId) {
        if (sourceStationId.equals(targetStationId)) {
            throw new IllegalArgumentException("시작역과 도착역이 같습니다.");
        }
    }

    private PathResponse createpathResponse(ShortestPathInfo shortestPathInfo) {
        List<PathStationDto> pathStationDtos = new ArrayList<>();

        for (Station shortestPath : shortestPathInfo.getStations()) {
            PathStationDto pathStationDto = PathStationDto.of(shortestPath);
            pathStationDtos.add(pathStationDto);
        }
        
        return new PathResponse(pathStationDtos, shortestPathInfo.getDistance().value());
    }
}
