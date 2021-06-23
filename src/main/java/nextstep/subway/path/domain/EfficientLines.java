package nextstep.subway.path.domain;

import nextstep.subway.exception.NoRouteException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.StationPair;
import nextstep.subway.wrapped.Distance;
import nextstep.subway.wrapped.Money;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EfficientLines {
    private final List<Line> lines;

    public EfficientLines(List<Line> lines) {
        this.lines = new ArrayList<>(lines);
    }

    public Line findCheapAndShortestBy(StationPair stationPair) {
        List<Line> sectionContainLines = getLinesContainsBy(stationPair);

        Distance minimumDistance = getShortestDistanceBy(stationPair, sectionContainLines);

        Money minimumMoney = getCheapBy(stationPair, sectionContainLines, minimumDistance);

        return sectionContainLines.stream()
                .filter(item -> item.findSectionBy(stationPair).getDistance() == minimumDistance)
                .filter(item -> item.getMoney() == minimumMoney)
                .findFirst()
                .orElseThrow(NoRouteException::new);
    }

    private List<Line> getLinesContainsBy(StationPair stationPair) {
        List<Line> lines = this.lines.stream()
                .filter(item -> item.containsSection(stationPair))
                .collect(Collectors.toList());

        return lines;
    }

    private Distance getShortestDistanceBy(StationPair stationPair, List<Line> collect) {
        Distance minimumDistance = collect.stream()
                .map(item -> item.getSectionDistanceBy(stationPair))
                .min(Distance::compareTo)
                .orElseThrow(NoRouteException::new);
        return minimumDistance;
    }

    private Money getCheapBy(StationPair stationPair, List<Line> collect, Distance minimumDistance) {
        Money minimumMoney = collect.stream()
                .filter(item -> item.getSectionDistanceBy(stationPair) == minimumDistance)
                .map(item -> item.getMoney())
                .min(Money::compareTo)
                .orElseThrow(NoRouteException::new);
        return minimumMoney;
    }
}
