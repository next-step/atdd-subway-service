package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return Collections.emptyList();
        }
        return getNextStations(findFirstStation());
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

    private Station findFirstStation() {
        return this.sections.stream()
                .map(Section::getUpStation)
                .filter(station -> !getSameDownStation(station).isPresent())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("첫번째 역을 찾을수 없습니다."));
    }

    public void add(Station upStation, Station downStation, int distance) {
        if (isStationExisted(upStation) && isStationExisted(downStation)) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!this.getStations().isEmpty() && !isStationExisted(upStation) && !isStationExisted(downStation)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }

        getSameUpStation(upStation)
                .ifPresent(it -> it.updateUpStation(downStation, distance));

        getSameDownStation(downStation)
                .ifPresent(it -> it.updateDownStation(upStation, distance));

        this.sections.add(new Section(this, upStation, downStation, distance));
    }

    private boolean isStationExisted(Station targetStation) {
        return this.getStations().stream().anyMatch(it -> it == targetStation);
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

    public boolean isRemovable() {
        return this.sections.size() <= 1;
    }
}
