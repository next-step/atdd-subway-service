package nextstep.subway.path.application;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathAnalysisKey;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathStationDto;
import nextstep.subway.path.dto.ShortestPathInfo;
import nextstep.subway.path.infrastructure.PathAnalysis;
import nextstep.subway.policy.DiscountPolicyFactory;
import nextstep.subway.policy.domain.Price;
import nextstep.subway.policy.price.DistancePricePolicy;
import nextstep.subway.policy.price.LinePricePolicy;
import nextstep.subway.policy.price.PricePolicy;
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

    public PathResponse searchShortestPath(Long sourceStationId, Long targetStationId) {
        vaildateShortestPath(sourceStationId, targetStationId);

        Station source = stationService.findById(sourceStationId);
        Station target = stationService.findById(targetStationId);

        PathAnalysis pathAnalysis = PathAnalysis.of(lineService.findAllSections());

        ShortestPathInfo shortestPathInfo = pathAnalysis.findShortestPaths(source, target);

        List<Line> lines = shortestPathInfo.getLines();

        PricePolicy linePricePolicy = new LinePricePolicy(lines);
        PricePolicy distancePricePolicy = new DistancePricePolicy(shortestPathInfo.getDistance());

        List<PricePolicy> pricePolicys = List.of(linePricePolicy, distancePricePolicy);

        // when
        Price totalFare = pricePolicys.stream()
                                        .map(PricePolicy::apply)
                                        .reduce((seed, result) -> seed.plus(result))
                                        .orElseThrow(() -> new NoSuchElementException("계산되는 운임이 없습니다."));

        return createPathResponse(shortestPathInfo, totalFare);
    }

    private void vaildateShortestPath(Long sourceStationId, Long targetStationId) {
        if (sourceStationId.equals(targetStationId)) {
            throw new IllegalArgumentException("시작역과 도착역이 같습니다.");
        }
    }

    public PathResponse searchShortestPath(Long sourceStationId, Long targetStationId, Integer age) {
        vaildateShortestPath(sourceStationId, targetStationId);

        Station source = stationService.findById(sourceStationId);
        Station target = stationService.findById(targetStationId);

        PathAnalysis pathAnalysis = PathAnalysis.of(lineService.findAllSections());

        ShortestPathInfo shortestPathInfo = pathAnalysis.findShortestPaths(source, target);

        List<Line> lines = shortestPathInfo.getLines();

        PricePolicy linePricePolicy = new LinePricePolicy(lines);
        PricePolicy distancePricePolicy = new DistancePricePolicy(shortestPathInfo.getDistance());

        List<PricePolicy> pricePolicys = List.of(linePricePolicy, distancePricePolicy);

        // when
        Price totalFare = pricePolicys.stream()
                                        .map(PricePolicy::apply)
                                        .reduce((seed, result) -> seed.plus(result))
                                        .orElseThrow(() -> new NoSuchElementException("계산되는 운임이 없습니다."));

        Price discoutnAcceptedPrice = DiscountPolicyFactory.generate(age).apply(totalFare);
        return createPathResponse(shortestPathInfo, discoutnAcceptedPrice);
    }

    private PathResponse createPathResponse(ShortestPathInfo shortestPathInfo, Price fare) {
        List<Long> stationIds = convertPathAnalysisKeyToStationKey(shortestPathInfo);

        List<Station> stations = new ArrayList<>();

        for (Long stationId : stationIds) {
            stations.add(stationService.findById(stationId));
        }

        List<PathStationDto> pathStationDtos = convertPathAnaylysisKeyToPathStaionDto(stations);

        return new PathResponse(pathStationDtos, shortestPathInfo.getDistance().value(), fare.value());
    }

    private List<PathStationDto> convertPathAnaylysisKeyToPathStaionDto(List<Station> stations) {
        return stations.stream()
                        .map(PathStationDto::of)
                        .collect(Collectors.toList());
    }

    private List<Long> convertPathAnalysisKeyToStationKey(ShortestPathInfo shortestPathInfo) {
        return shortestPathInfo.getPathAnalysisKeys().stream()
                                .map(PathAnalysisKey::getStationId)
                                .collect(Collectors.toList());
    }
}
