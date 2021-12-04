package nextstep.subway.path.domain;

import static nextstep.subway.exception.ExceptionMessage.*;

import java.util.Set;
import java.util.stream.Collectors;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class PathSections {
    private final Set<Section> sections;

    public PathSections(Set<Section> sections) {
        this.sections = sections;
    }

    public Set<Section> getSections() {
        return sections;
    }

    public void getShortestPath(Station sourceStation, Station targetStation) {
        validate(sourceStation, targetStation);
    }

    private void validate(Station sourceStation, Station targetStation) {
        validateSameStation(sourceStation, targetStation);
        validateExistStation(sourceStation, targetStation);
    }

    private void validateSameStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException(SAME_STATION.getMessage());
        }
    }

    private void validateExistStation(Station sourceStation, Station targetStation) {
        Set<Station> allStation = getAllStation();
        if (!allStation.contains(sourceStation) || !allStation.contains(targetStation)) {
            throw new IllegalArgumentException(NOT_EXIST_STATION.getMessage());
        }
    }

    public Set<Station> getAllStation() {
        Set<Station> upStations = getUpStations();
        Set<Station> downStations = getDownStations();
        upStations.addAll(downStations);
        return upStations;
    }

    private Set<Station> getUpStations() {
        return sections.stream()
            .map(Section::getUpStation)
            .collect(Collectors.toSet());
    }

    private Set<Station> getDownStations() {
        return sections.stream()
            .map(Section::getDownStation)
            .collect(Collectors.toSet());
    }
}
