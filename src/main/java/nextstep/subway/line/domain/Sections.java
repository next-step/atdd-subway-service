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

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getStations() {
        return getNextStations(findFirstStation());
    }

    private Station findFirstStation() {
        return this.sections.stream()
                .map(Section::getUpStation)
                .filter(station -> !getSameDownStation(station).isPresent())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("첫번째 역을 찾을수 없습니다."));
    }

    private List<Station> getNextStations(Station rootStation) {
        List<Station> stations = new ArrayList<>();
        stations.add(rootStation);
        Optional<Section> nextSection = getSameUpStation(rootStation);
        while (nextSection.isPresent()) {
            Station nextStation = nextSection.get().getDownStation();
            stations.add(nextStation);
            nextSection = getSameUpStation(nextStation);
        }
        return stations;
    }

    public void removeStation(Station station) {
        Optional<Section> upLineStation = getSameUpStation(station);
        Optional<Section> downLineStation = getSameDownStation(station);
        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Section newSection = upLineStation.get().merge(downLineStation.get());
            this.sections.add(newSection);
        }
        upLineStation.ifPresent(it -> this.sections.remove(it));
        downLineStation.ifPresent(it -> this.sections.remove(it));
    }

    private Optional<Section> getSameDownStation(Station station) {
        return this.sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();
    }

    private Optional<Section> getSameUpStation(Station station) {
        return this.sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
    }

    public boolean isRemovable() {
        return this.sections.size() <= 1;
    }

    private void changeSameDownStation(Section section) {
        getSameDownStation(section.getDownStation())
                .ifPresent(it -> it.updateDownStation(section));
    }

    private void changeSameUpStation(Section section) {
        getSameUpStation(section.getUpStation())
                .ifPresent(it -> it.updateUpStation(section));
    }

    public void create(Section section) {
        this.sections.add(section);
    }

    public void add(Section section) {
        checkAddValidation(section);
        changeSameUpStation(section);
        changeSameDownStation(section);
        this.sections.add(section);
    }

    private void checkAddValidation(Section targetSection) {
        if (this.sections.contains(targetSection)) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }

        if (checkConnectableSection(targetSection)) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }
    }

    private boolean checkConnectableSection(Section targetSection) {
        return hasNotStation(targetSection.getUpStation()) && hasNotStation(targetSection.getDownStation());
    }

    private boolean hasNotStation(Station targetStation) {
        return !this.getStations().contains(targetStation);
    }

    public boolean hasSection(long source, long target) {
        return this.sections.stream()
                .anyMatch(section -> section.equalsById(source, target));
    }
}
