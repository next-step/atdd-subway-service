package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.exception.ExceptionType;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> items = new ArrayList<>();

    public void add(Section section) {
        this.items.add(section);
    }

    public List<Section> getItems() {
        return items;
    }

    public List<Station> getOrderedStations() {
        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();

        Section section = findFirstSection();
        stations.add(section.getUpStation());
        stations.add(section.getDownStation());

        Optional<Section> optionalNextSection = findNextSection(section);
        while (optionalNextSection.isPresent()) {
            Section nextSection = optionalNextSection.get();
            stations.add(nextSection.getDownStation());
            optionalNextSection = findNextSection(nextSection);
        }

        return stations;
    }

    private Optional<Section> findNextSection(Section section) {
        return this.items.stream()
            .filter(item -> item.isEqualsUpStation(section.getDownStation()))
            .findAny();
    }

    private Section findFirstSection() {
        Station firstStation = findUpStation();

        return items.stream()
            .filter(item -> item.isEqualsUpStation(firstStation))
            .findAny()
            .orElseThrow(() -> new NotFoundException(ExceptionType.NOT_FOUND_LINE_STATION));
    }

    private Station findUpStation() {
        List<Station> upStations = this.items.stream()
            .map(Section::getUpStation)
            .collect(Collectors.toList());

        return upStations.stream()
            .filter(station -> isNoneMatchedDownStation(items, station))
            .findAny()
            .orElseThrow(() -> new NotFoundException(ExceptionType.NOT_FOUND_LINE_STATION));
    }

    private boolean isNoneMatchedDownStation(List<Section> sections, Station upStation) {
        return sections.stream()
            .noneMatch(section -> section.isEqualsDownStation(upStation));
    }
}
