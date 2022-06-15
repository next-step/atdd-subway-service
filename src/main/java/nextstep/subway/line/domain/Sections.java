package nextstep.subway.line.domain;


import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public void add(Line line, Station upStation, Station downStation, int distance) {
        this.add(line, upStation, downStation, new Distance(distance));
    }

    public void add(Line line, Station upStation, Station downStation, Distance distance) {
        validateSections(upStation, downStation);
        updateUpStationIfPresent(upStation, downStation, distance);
        updateDownStationIfPresent(upStation, downStation, distance);
        sections.add(new Section(line, upStation, downStation, distance));
    }

    private void validateSections(Station upStation, Station downStation) {
        if (isPresentSection(upStation, downStation)) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (canAdd(upStation, downStation)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private boolean isPresentSection(Station upStation, Station downStation) {
        return isPresentStation(upStation) && isPresentStation(downStation);
    }

    private boolean canAdd(Station upStation, Station downStation) {
        return !stations().isEmpty() && !isPresentStation(upStation) && !isPresentStation(downStation);
    }

    private void updateUpStationIfPresent(Station upStation, Station downStation, Distance distance) {
        if (!isPresentStation(upStation)) {
            return;
        }
        getUpLineStation(upStation).ifPresent(it -> it.updateUpStation(downStation, distance));
    }

    private void updateDownStationIfPresent(Station upStation, Station downStation, Distance distance) {
        if (!isPresentStation(downStation)) {
            return;
        }
        getDownLineStation(downStation).ifPresent(it -> it.updateDownStation(upStation, distance));
    }

    private boolean isPresentStation(Station station) {
        return stations().stream().anyMatch(it -> it == station);
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

    public List<Station> stations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station station = firstStation();
        stations.add(station);

        while (isPresentNextSection(station)) {
            station = nextSection(station).getDownStation();
            stations.add(station);
        }

        return stations;
    }

    private Station firstStation() {
        Station firstStation = sections.get(0).getUpStation();
        while (isPresentPreSection(firstStation)) {
            firstStation = preSection(firstStation).getUpStation();
        }

        return firstStation;
    }

    private boolean isPresentPreSection(Station station) {
        return sections.stream()
                .filter(Section::existUpStation)
                .anyMatch(s -> s.equalsDownStation(station));
    }

    private Section preSection(Station downStation) {
        return getDownLineStation(downStation)
                .orElseThrow(() -> new IllegalArgumentException("이전 구간이 존재하지 않습니다."));
    }

    private Section nextSection(Station station) {
        return getUpLineStation(station)
                .orElseThrow(() -> new IllegalArgumentException("다음 구간이 존재하지 않습니다."));
    }

    private boolean isPresentNextSection(Station station) {
        return sections.stream()
                .filter(Section::existUpStation)
                .anyMatch(s -> s.equalsUpStation(station));
    }

    public void remove(Line line, Station station) {
        validateSectionSize();

        Section upLineStation = getUpLineStation(station).orElse(null);
        Section downLineStation = getDownLineStation(station).orElse(null);

        createSection(line, upLineStation, downLineStation);
        removeSections(upLineStation, downLineStation);
    }

    private void validateSectionSize() {
        if (sections.size() <= 1) {
            throw new RuntimeException("구간은 최소 1개 이상 존재해야 합니다. size = " + sections.size());
        }
    }

    private Optional<Section> getUpLineStation(Station station) {
        return sections.stream()
                .filter(it -> it.equalsUpStation(station))
                .findFirst();
    }

    private Optional<Section> getDownLineStation(Station station) {
        return sections.stream()
                .filter(it -> it.equalsDownStation(station))
                .findFirst();
    }

    private void createSection(Line line, Section upLineStation, Section downLineStation) {
        if (upLineStation == null || downLineStation == null) {
            return;
        }
        Distance newDistance = upLineStation.plusDistance(downLineStation);
        sections.add(new Section(line, downLineStation.getUpStation(), upLineStation.getDownStation(), newDistance));
    }

    private void removeSections(Section upLineStation, Section downLineStation) {
        if (upLineStation != null) {
            sections.remove(upLineStation);
        }
        if (downLineStation != null) {
            sections.remove(downLineStation);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sections sections1 = (Sections) o;
        return Objects.equals(getSections(), sections1.getSections());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSections());
    }
}
