package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

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

    private void validateSections(List<Station> stations, Section targetSection) {
        validateHasEqualsSection(stations, targetSection);
        validateContainsAnyStation(stations, targetSection);
    }

    private void validateHasEqualsSection(List<Station> stations, Section addTargetSection) {
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == addTargetSection.getUpStation());
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == addTargetSection.getDownStation());

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    private void validateContainsAnyStation(List<Station> stations, Section addTargetSection) {
        if (!stations.isEmpty()
            && stations.stream().noneMatch(it -> it == addTargetSection.getUpStation())
            && stations.stream().noneMatch(it -> it == addTargetSection.getDownStation())
        ) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    public void addSection(Section targetSection) {
        List<Station> stations = getAllStation();
        validateSections(stations, targetSection);

        if (stations.contains(targetSection.getUpStation())) {
            sections.stream()
                .filter(it -> it.getUpStation() == targetSection.getUpStation())
                .findFirst()
                .ifPresent(it -> it.updateUpStation(targetSection.getDownStation(), targetSection.getDistance()));
        }

        if (stations.contains(targetSection.getDownStation())) {
            sections.stream()
                .filter(it -> it.getDownStation() == targetSection.getDownStation())
                .findFirst()
                .ifPresent(it -> it.updateDownStation(targetSection.getUpStation(), targetSection.getDistance()));
        }
        sections.add(targetSection);
    }

    public List<Station> getAllStation() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findFinalUpStation();
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

    // 전체 구간에서 다른 구간의 downStation에 포함되지 않은 상행역 = 상행종점역
    public Station findFinalUpStation() {
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
}
