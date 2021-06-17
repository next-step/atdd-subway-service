package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.*;
import java.util.function.Predicate;

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

    public List<Section> getSections() {
        return sections;
    }

    public Station getUpStation() {
        return sections.get(0).getUpStation();
    }

    public Station getDownStation() {
        return sections.get(0).getDownStation();
    }

    public boolean isEmptySection() {
        return sections.isEmpty();
    }

    public boolean isUnableRemoveStatus() {
        return sections.size() <= 1;
    }

    public void removeSection(Section section) {
        sections.remove(section);
    }

    public Optional<Section> getSection(Predicate<Section> sectionPredicate) {
        return sections.stream()
                .filter(sectionPredicate)
                .findFirst();
    }

    public void updateUpStationIfSameUpStation(Section section) {
        sections.stream()
                .filter(it -> it.getUpStation() == section.getUpStation())
                .findFirst()
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
    }

    public void updateDownStationIfSameDownStation(Section section) {
        sections.stream()
                .filter(it -> it.getDownStation() == section.getDownStation())
                .findFirst()
                .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
    }

    public List<Station> getStations() {

        if (isEmptySection()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = getSection(section -> section.isSameUpStation(finalDownStation));
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Station findUpStation() {
        Collections.sort(sections);

        Station downStation = getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;

            Optional<Section> nextLineStation = getSection(section -> section.isSameDownStation(finalDownStation));
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    public void addSection(Section section) {
        List<Station> stations = getStations();
        section.setLine(this);

        validationSection(section, stations);

        boolean isUpStationExisted = isExistStation(stations, section.getUpStation());
        boolean isDownStationExisted = isExistStation(stations, section.getDownStation());

        if (isFirstRegistSection(section, stations)) {
            return;
        }

        if (isUpStationExisted) {
            updateUpStationIfSameUpStation(section);
        } else if (isDownStationExisted) {
            updateDownStationIfSameDownStation(section);
        }
        sections.add(section);
    }

    private boolean isFirstRegistSection(Section section, List<Station> stations) {
        if (stations.isEmpty()) {
            sections.add(section);
            return true;
        }
        return false;
    }

    private void validationSection(Section section, List<Station> stations) {
        boolean isUpStationExisted = isExistStation(stations, section.getUpStation());
        boolean isDownStationExisted = isExistStation(stations, section.getDownStation());

        if (!(isUpStationExisted ^ isDownStationExisted)) {
            throw new RuntimeException("이미 등록된 구간 또는 등록할수 없는 구간입니다.");
        }

        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == section.getUpStation()) &&
                stations.stream().noneMatch(it -> it == section.getDownStation())) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private boolean isExistStation(List<Station> stations, Station station) {
        return stations.stream().anyMatch(it -> it == station);
    }

    public void removeLineStation(Station station) {
        if (isUnableRemoveStatus()) {
            throw new RuntimeException();
        }
        Optional<Section> upLineStation = getSection(section -> section.isSameUpStation(station));
        Optional<Section> downLineStation = getSection(section -> section.isSameDownStation(station));

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            getSections().add(new Section(this, newUpStation, newDownStation, newDistance));
        }
        upLineStation.ifPresent(it -> removeSection(it));
        downLineStation.ifPresent(it -> removeSection(it));
    }
}
