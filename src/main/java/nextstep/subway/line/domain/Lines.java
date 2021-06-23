package nextstep.subway.line.domain;

import nextstep.subway.exception.NoRouteException;
import nextstep.subway.exception.NotExistMinimumFareLine;
import nextstep.subway.station.domain.Station;
import nextstep.subway.wrapped.Distance;
import nextstep.subway.wrapped.Money;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Lines {
    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = new ArrayList<>(lines);
    }

    public boolean containsStationsExactly(Station...stations) {
        boolean containsStations = lines.stream()
                .anyMatch(item -> item.containsStationsExactly(stations));

        return containsStations;
    }

    public Money findExpensiveFare() {
        return lines.stream()
                .map(Line::getMoney)
                .max(Money::compareTo)
                .orElseThrow(NotExistMinimumFareLine::new);
    }

    public Line findShortestSectionBy(SimpleSection simpleSection) {
        List<Line> collect = lines.stream()
                .filter(item -> item.containsSection(simpleSection))
                .collect(Collectors.toList());

        Distance minimumDistance = collect.stream()
                .map(item -> item.getSectionDistanceOf(simpleSection))
                .min(Distance::compareTo)
                .orElseThrow(NoRouteException::new);

        Money minimumMoney = collect.stream()
                .filter(item -> item.getSectionDistanceOf(simpleSection) == minimumDistance)
                .map(item -> item.getMoney())
                .min(Money::compareTo)
                .orElseThrow(NoRouteException::new);


        return collect.stream()
                .filter(item -> item.getSectionDistanceOf(simpleSection) == minimumDistance)
                .filter(item -> item.getMoney().equals(minimumMoney))
                .findFirst()
                .orElseThrow(NoRouteException::new);
    }
}
