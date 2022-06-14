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
        validateSections(upStation, downStation);
        updateUpStationIfPresent(upStation, downStation, distance);
        updateDownStationIfPresent(upStation, downStation, distance);
        sections.add(new Section(line, upStation, downStation, distance));
    }

    private void validateSections(Station upStation, Station downStation) {
        if (isPresentStation(upStation) && isPresentStation(downStation)) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!stations().isEmpty() && !isPresentStation(upStation) && !isPresentStation(downStation) ) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private void updateUpStationIfPresent(Station upStation, Station downStation, int distance) {
        if (!isPresentStation(upStation)) {
            return;
        }
        getUpLineStation(upStation).ifPresent(it -> it.updateUpStation(downStation, distance));
    }

    private void updateDownStationIfPresent(Station upStation, Station downStation, int distance) {
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
        Station downStation = sections.get(0).getUpStation();
        while(isPresentPreSection(downStation)) {
            downStation = preSection(downStation).getUpStation();
        }

        return downStation;
    }

    private boolean isPresentPreSection(Station station) {
        return sections.stream()
                .filter(s -> s.getUpStation() != null)
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
                .filter(s -> s.getUpStation() != null)
                .anyMatch(s -> s.equalsUpStation(station));
    }

    public void remove(Line line, Station station) {
        validateSectionSize();

        Optional<Section> upLineStation = getUpLineStation(station);
        Optional<Section> downLineStation = getDownLineStation(station);

        createSection(line, upLineStation, downLineStation);
        removeSections(upLineStation, downLineStation);
    }

    private void validateSectionSize() {
        if (sections.size() <= 1) {
            throw new RuntimeException();
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

    private void createSection(Line line, Optional<Section> upLineStation, Optional<Section> downLineStation) {
        if (!upLineStation.isPresent() || !downLineStation.isPresent()) {
            return;
        }
        int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
        sections.add(new Section(line, downLineStation.get().getUpStation(), upLineStation.get().getDownStation(), newDistance));
    }

    private void removeSections(Optional<Section> upLineStation, Optional<Section> downLineStation) {
        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
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
