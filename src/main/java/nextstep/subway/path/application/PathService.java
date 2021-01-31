package nextstep.subway.path.application;


import nextstep.subway.line.application.SectionService;
import nextstep.subway.line.domain.*;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.member.domain.DiscountStrategy;
import nextstep.subway.member.dto.Money;

import nextstep.subway.path.dto.PathFindResponse;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class PathService {
    private final LineRepository lineRepository;
    private final SectionService sectionService;
    private final StationService stationService;

    public PathService(LineRepository lineRepository, SectionService sectionService, StationService stationService) {
        this.lineRepository = lineRepository;
        this.sectionService = sectionService;
        this.stationService = stationService;
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPath(PathRequest pathRequest, DiscountStrategy discountStrategy) {
        Lines lines = new Lines(lineRepository.findAll());
        Station source = stationService.getOne(pathRequest.getSourceId());
        Station target = stationService.getOne(pathRequest.getTargetId());
        if (source == target) {
            throw new RuntimeException();
        }
        PathFindResponse pathFindResponse = PathFinder.findPath(lines.getLines(), source, target);
        List<Section> sections = sectionService.getSections(pathFindResponse.getStationIds());
        Money extraCharge = getExtraCharge(sections);
        int distance = sections.stream()
                .map(Section::getDistance)
                .reduce(Integer::sum)
                .get();
        Stations stations = new Stations(sections);
        return new PathResponse(distance, stations.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList()),
                FeeCalculator.calculate(extraCharge, distance, discountStrategy).getValue());
    }

    private Money getExtraCharge(List<Section> sections) {
        return Money.of(sections.stream()
                .map(it -> it.getLine().getExtraCharge())
                .max(Long::compare)
                .get());
    }


}
