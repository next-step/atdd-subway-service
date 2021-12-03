package nextstep.subway.line.domain;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST,
        CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections(Section... sections) {
        if (isContains(sections)) {
            return;
        }
        this.sections.addAll(asList(sections));
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    protected Sections() {
    }

    private boolean isContains(Section... newSections) {
        List<Section> sections = asList(newSections);
        boolean match = this.sections.stream()
            .anyMatch(savedSection -> sections.stream()
                .anyMatch(section -> Objects.equals(section, savedSection)));
        return match;
    }

    public void addSection(Section section) {
        if (isContains(section)) {
            return;
        }
        sections.add(section);
    }

    public void addLineStation(Section section) {
        List<Station> stations = getStations();
        Station downStation = section.getDownStation();
        Station upStation = section.getUpStation();
        int distance = section.getDistance();

        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == upStation);
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == downStation);

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == upStation) &&
            stations.stream().noneMatch(it -> it == downStation)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }

        if (stations.isEmpty()) {
            sections
                .add(new Section(upStation, downStation, distance));
            return;
        }

        if (isUpStationExisted) {
            sections.stream()
                .filter(it -> it.getUpStation() == upStation)
                .findFirst()
                .ifPresent(it -> it.updateUpStation(downStation, distance));

            sections
                .add(new Section(upStation, downStation, distance));
        } else if (isDownStationExisted) {
            sections.stream()
                .filter(it -> it.getDownStation() == downStation)
                .findFirst()
                .ifPresent(it -> it.updateDownStation(upStation, distance));

            sections
                .add(new Section(upStation, downStation, distance));
        } else {
            throw new RuntimeException();
        }
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
                .filter(it -> it.getUpStation() == finalDownStation)
                .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
                .filter(it -> it.getDownStation() == finalDownStation)
                .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }
        return downStation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sections sections1 = (Sections) o;
        return Objects.equals(sections, sections1.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sections);
    }
}
