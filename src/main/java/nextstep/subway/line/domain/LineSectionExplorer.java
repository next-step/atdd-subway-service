package nextstep.subway.line.domain;

import nextstep.subway.line.domain.exceptions.ExploreSectionException;
import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LineSectionExplorer {
    private final List<Section> sections;

    LineSectionExplorer(final List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    Station findUpStation() {
        Section theFirstSection = findFirstSection();
        return theFirstSection.getUpStation();
    }

    List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        LineSectionExplorer lineSectionExplorer = new LineSectionExplorer(sections);

        List<Station> stations = new ArrayList<>();
        stations.add(lineSectionExplorer.findUpStation());

        Section currentSection = lineSectionExplorer.findFirstSection();

        while (currentSection != null) {
            stations.add(currentSection.getDownStation());
            currentSection = this.findNextSection(currentSection);
        }

        return stations;
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

    Section findNextSection(Section currentSection) {
        return this.sections.stream()
                .filter(it -> currentSection.getDownStation().equals(it.getUpStation()))
                .findFirst()
                .orElse(null);
    }
}
