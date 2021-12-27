package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PathFinder {
    private static final int NONE_EXTRA_FARE = 0;
    private final ShortestPathCalculator shortestPathCalculator;

    public PathFinder(ShortestPathCalculator shortestPathCalculator) {
        this.shortestPathCalculator = shortestPathCalculator;
    }

    public PathResponse findPath(List<Line> lines, Station sourceStation, Station targetStation) {
        validateSourceEqualsTarget(sourceStation, targetStation);
        List<Section> sections = findSections(lines);
        PathResponse pathResponse = shortestPathCalculator.calculatePath(sections, sourceStation, targetStation);
        pathResponse.calculateExtraFare(findContainLines(sections, pathResponse.getStations()));
        return pathResponse;
    }

    private void validateSourceEqualsTarget(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
        }
    }

    private List<Section> findSections(List<Line> lines) {
        List<Section> sections = new ArrayList<>();
        lines.stream()
                .map(line -> line.getSections())
                .forEach(sections::addAll);
        return sections;
    }

    private int findContainLines(List<Section> sections, List<StationResponse> stations) {
        Set<Line> lines = new HashSet<>();
        for (int i = 0; i < stations.size()-1; i++) {
            Section containSection = findSection(sections, stations.get(i), stations.get(i+1));
            lines.add(containSection.getLine());
        }
        return findExtraFare(lines);
    }

    private Section findSection(List<Section> sections, StationResponse station, StationResponse nextStation) {
        return sections.stream()
                .filter(section -> section.getUpStation().getId().equals(station.getId()))
                .filter(section -> section.getDownStation().getId().equals(nextStation.getId()))
                .findFirst()
                .orElseGet(() -> findSectionReverse(sections, station, nextStation));
    }

    private Section findSectionReverse(List<Section> sections, StationResponse station, StationResponse nextStation) {
        return sections.stream()
                .filter(section -> section.getUpStation().getId().equals(nextStation.getId()))
                .filter(section -> section.getDownStation().getId().equals(station.getId()))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    private int findExtraFare(Set<Line> containLines) {
        return containLines.stream()
                .filter(line -> line.getExtraFare() > NONE_EXTRA_FARE)
                .mapToInt(Line::getExtraFare)
                .max()
                .orElse(NONE_EXTRA_FARE);
    }
}
