package nextstep.subway.line.domain;

import nextstep.subway.line.domain.exceptions.ExploreSectionException;
import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LineSectionExplorer {
    private final List<Section> sections;

    public LineSectionExplorer(final List<Section> sections) {
        this.sections = sections;
    }

    public Station findUpStation() {
        Section theFirstSection = findFirstSection();
        return theFirstSection.getUpStation();
    }

    Section findFirstSection() {
        return this.sections.stream().filter(it -> it.isUpStationBelongsTo(findEndStationsInSections()))
                .findFirst()
                .orElseThrow(() -> new ExploreSectionException("해당 노선의 첫번째 구간을 찾을 수 없습니다."));
    }

    List<Station> findEndStationsInSections() {
        List<Station> stations = sections.stream()
                .flatMap(it -> it.getStations().stream())
                .collect(Collectors.toList());

        return stations.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .filter(it -> it.getValue() == 1L)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
