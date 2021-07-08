package nextstep.subway.path.service;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.line.service.SectionService;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.Path;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.String.format;
import static java.util.Arrays.asList;

@Service
public class PathService {
    private SectionService sectionService;
    private StationService stationService;
    private PathFinder pathFinder;

    public PathService(SectionService sectionService, StationService stationService, PathFinder pathFinder) {
        this.sectionService = sectionService;
        this.stationService = stationService;
        this.pathFinder = pathFinder;
    }

    public PathResponse findPaths(PathRequest request) {
        Sections sections = sectionService.findSections();
        Stations stations = stationService.findAllById(asList(request.getSource(), request.getTarget()));

        Station  source = stations.getById(request.getSource())
                .orElseThrow(new IllegalArgumentException(format("%d란 id로된 역을 찾을 수 없습니다,", request.getSource()));
        Station target = stations.getById(request.getSource())
                .orElseThrow(new IllegalArgumentException(format("%d란 id로된 역을 찾을 수 없습니다,", request.getSource()));

        Path path = pathFinder.findShortestPath(sections, source, target);
        return PathResponse.of(path);
    }

    public static void main(String[] args) {
        new HashMap<Integer, Station>().get(123);
    }
}
