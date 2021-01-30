package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public Sections(Section section) {
        this.sections = new ArrayList<>();
        this.sections.add(section);
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getStations() {
        Section startSection = findStartSection(sections.get(0));
        List<Station> stations = new ArrayList<>();
        stations.add(startSection.getUpStation());
        Station nextStation = startSection.getDownStation();
        while (nextStation != null) {
            stations.add(nextStation);
            nextStation = findDownStation(nextStation);
        }
        return stations;
    }

    public void addSection(Line line, Station upStation, Station downStation, int distance) {
        List<Station> stations = this.getStations();
        if (stations.contains(upStation) == stations.contains(downStation)) {
            throw new RuntimeException();
        }
        tryAddToUp(upStation, downStation, distance);
        sections.add(new Section(line, upStation, downStation, distance));
    }

    public void removeStation(Line line, Station station) {
        checkValidation(station);
        Optional<Section> upLineStation = sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
        Optional<Section> downLineStation = sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();
        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            sections.add(createNewSection(line, upLineStation.get(), downLineStation.get()));
        }
        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }


    private void checkValidation(Station station) {
        if (sections.size() <= 1 || !getStations().contains(station)) {
            throw new RuntimeException();
        }
    }

    private void tryAddToUp(Station upStation, Station downStation, int distance) {
        Optional<Section> section = sections.stream()
                .filter(it -> it.getUpStation() == upStation)
                .findFirst();
        if (section.isPresent()) {
            section.get().updateUpStation(downStation, distance);
            return;
        }
        tryAddToDown(upStation, downStation, distance);
    }

    private void tryAddToDown(Station upStation, Station downStation, int distance) {
        Optional<Section> section = sections.stream()
                .filter(it -> it.getDownStation() == downStation)
                .findFirst();
        section.ifPresent(value -> value.updateDownStation(upStation, distance));
    }

    private Section findStartSection(Section section) {
        Optional<Section> optionalSection = sections.stream()
                .filter(it -> it.getDownStation().equals(section.getUpStation()))
                .findFirst();
        if (!optionalSection.isPresent()) {
            return section;
        }
        return findStartSection(optionalSection.get());
    }

    private Station findDownStation(Station station) {
        return this.sections.stream()
                .filter(it -> it.getUpStation().equals(station))
                .findFirst()
                .map(Section::getDownStation)
                .orElse(null);
    }

    private Section createNewSection(Line line, Section upLineSection, Section downLineSection) {
        Station newUpStation = downLineSection.getUpStation();
        Station newDownStation = upLineSection.getDownStation();
        int newDistance = upLineSection.getDistance() + downLineSection.getDistance();
        return new Section(line, newUpStation, newDownStation, newDistance);
    }


}
