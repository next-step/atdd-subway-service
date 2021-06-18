package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        this.sections.add(section);
    }

    public List<Section> get() {
        return this.sections;
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public void updateIfMidFront(Section section) {
        hasSameUpStation(section)
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
    }

    public Optional<Section> hasSameUpStation(Section section) {
        return sections.stream()
                .filter(it -> it.getUpStation() == section.getUpStation())
                .findFirst();
    }

    public Optional<Section> hasSameUpStationWith(Station station) {
        return sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
    }

    public void updateIfMidRear(Section section) {
        sections.stream()
                .filter(it -> it.getDownStation() == section.getDownStation())
                .findFirst()
                .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
    }

    public Optional<Section> hasSameDownStationWith(Station station) {
        return sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();
    }

    public Stream<Section> stream() {
        return sections.stream();
    }

    public boolean alreadyHas(Section section) {
        List<Station> stations = sections.stream()
                .flatMap(it -> Stream.of(it.getUpStation(), it.getDownStation()))
                .collect(Collectors.toList());
        return stations.contains(section.getUpStation()) && stations.contains(section.getDownStation());
    }

    public boolean cannotConnect(Section section) {
        List<Station> stations = sections.stream()
                .flatMap(it -> Stream.of(it.getUpStation(), it.getDownStation()))
                .collect(Collectors.toList());
        return !stations.contains(section.getUpStation()) && !stations.contains(section.getDownStation());
    }

    public void validateRemovableSize() {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }
    }

    public void remove(Station station) {
        Optional<Section> upSection = hasSameUpStationWith(station);
        Optional<Section> downSection = hasSameDownStationWith(station);

        if (upSection.isPresent() && downSection.isPresent()) {
            Station newUpStation = downSection.get().getUpStation();
            Station newDownStation = upSection.get().getDownStation();
            int newDistance = upSection.get().getDistance() + downSection.get().getDistance();
            sections.add(new Section(upSection.get().getLine(), newUpStation, newDownStation, newDistance));
        }

        upSection.ifPresent(it -> sections.remove(it));
        downSection.ifPresent(it -> sections.remove(it));
    }
}
