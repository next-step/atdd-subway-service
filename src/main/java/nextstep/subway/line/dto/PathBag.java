package nextstep.subway.line.dto;

import nextstep.subway.auth.domain.Money;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationLineUp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PathBag {
    private final List<SectionPath> sectionPaths;

    private PathBag(List<SectionPath> sectionPaths) {
        this.sectionPaths = sectionPaths;
    }

    public static PathBag from(List<SectionPath> sectionPaths) {
        return new PathBag(sectionPaths);
    }

    public static PathBag fromLines(List<Line> lines) {
        return new PathBag(lines.stream().map(Line::getSections)
                .flatMap(List::stream)
                .map(SectionPath::from)
                .collect(Collectors.toList()));
    }

    public List<Station> findVertex() {
        Set<Station> stationSet = new HashSet<>();
        sectionPaths.forEach(it -> {
            stationSet.add(it.getUpStation());
            stationSet.add(it.getDownStation());
        });
        return new ArrayList<>(stationSet);
    }

    public List<SectionPath> getSectionPaths() {
        return sectionPaths;
    }

    public Money getMaxLineCharge(StationLineUp stationLineUp) {
        return Money.from(sectionPaths.stream()
                .filter(it ->
                        stationLineUp.hasStation(it.getUpStation()) &&
                                stationLineUp.hasStation(it.getDownStation()))
                .map(SectionPath::getExtraCharge)
                .mapToDouble(Money::getAmount)
                .max()
                .orElseThrow(() -> new IllegalStateException("추가 운임 값이 없습니다")));
    }
}
