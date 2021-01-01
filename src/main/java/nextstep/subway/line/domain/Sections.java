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
        if (isRemovable()) {
            throw new RuntimeException();
        }
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

    private boolean isRemovable() {
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

    public void add(Section section) {
        changeSameUpStation(section);
        changeSameDownStation(section);
        this.sections.add(section);
    }

    public void create(Section section) {
        this.sections.add(section);
    }

    public void checkAddValidation(Station upStation, Station downStation) {
        if (isStationExisted(upStation) && isStationExisted(downStation)) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!isEmpty() && !isStationExisted(upStation) && !isStationExisted(downStation)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private boolean isStationExisted(Station targetStation) {
        return this.getStations().contains(targetStation);
    }
}
