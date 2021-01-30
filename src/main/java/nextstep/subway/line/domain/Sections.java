package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void add(Section newSection) {

        validateAlreadyUsedSection(newSection);
        validateNotUsedStations(newSection);

        if (this.isEmpty()) {
            this.sections.add(newSection);
            return;
        }

        if (this.anyMatch(newSection.getUpStation())) {
            this.updateUpStation(newSection);
            this.sections.add(newSection);
            return;
        }

        if (this.anyMatch(newSection.getDownStation())) {
            this.updateDownStation(newSection);
            this.sections.add(newSection);
            return;
        }

        throw new RuntimeException();


    }




    private void validateNotUsedStations(Section section) {
        if (!this.isEmpty() && this.noneMatch(section.getUpStation()) &&
            this.noneMatch(section.getDownStation())) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }
    }

    private void validateAlreadyUsedSection(Section section) {

        if (this.anyMatch(section.getUpStation()) && this.anyMatch(section.getDownStation())) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }


    }

    private boolean noneMatch(Station station) {
        return Stream
            .concat(sections.stream().map(Section::getUpStation), sections.stream().map(Section::getDownStation))
            .noneMatch(it -> it == station);
    }

    private boolean anyMatch(Station station) {
        return Stream
            .concat(sections.stream().map(Section::getUpStation), sections.stream().map(Section::getDownStation))
            .anyMatch(it -> it == station);
    }


    private boolean isEmpty() {
        return this.sections.isEmpty();
    }

    public Stations getStations() {
        if (this.getSections().isEmpty()) {
            return new Stations(Collections.emptyList());
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = this.getSections().stream()
                .filter(it -> it.getUpStation() == finalDownStation)
                .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return new Stations(stations);
    }

    private Station findUpStation() {
        Station downStation = this.getSections().get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = this.getSections().stream()
                .filter(it -> it.getDownStation() == finalDownStation)
                .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }


    public void updateUpStation(Section section) {
        this.sections.stream()
            .filter(it -> it.getUpStation() == section.getUpStation())
            .findFirst()
            .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
    }
    private void updateDownStation(Section section) {
        this.sections.stream()
            .filter(it -> it.getDownStation() == section.getDownStation())
            .findFirst()
            .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
    }
}
