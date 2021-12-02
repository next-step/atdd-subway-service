package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Entity
public class Line extends BaseEntity {
    private static final String CANNOT_REMOVE_IF_SECTION_IS_ONLY_ONE_EXCEPTION_STATEMENT = "구간이 하나 이므로 삭제할 수 없습니다.";

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

    public static Line of(String name, String color, Station upStation, Station downStation, int distance) {
        return new Line(name, color, upStation, downStation, distance);
    }

    public static Line from(LineRequest lineRequest) {
        return new Line(lineRequest.getName(), lineRequest.getColor());
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

    public List<Station> getStations() {
        if (getSections().isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        Optional<Section> nextLineStation = findSectionBasedOnUpStation(downStation);
        while (downStation != null && nextLineStation.isPresent()) {
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
            nextLineStation = findSectionBasedOnUpStation(downStation);
        }

        return stations;
    }

    private Station findUpStation() {
        Station downStation = getSections().get(0).getUpStation();
        Optional<Section> nextLineStation = findSectionBasedOnDownStation(downStation);
        while (downStation != null && nextLineStation.isPresent()) {
            downStation = nextLineStation.get().getUpStation();
            nextLineStation = findSectionBasedOnDownStation(downStation);
        }
        return downStation;
    }

    public void removeLineStation(Station station) {
        if (getSections().size() <= 1) {
            throw new RuntimeException(CANNOT_REMOVE_IF_SECTION_IS_ONLY_ONE_EXCEPTION_STATEMENT);
        }

        Optional<Section> upLineStation = findSectionBasedOnUpStation(station);
        Optional<Section> downLineStation = findSectionBasedOnDownStation(station);

        addSectionIfBothStationsOf(upLineStation, downLineStation);

        upLineStation.ifPresent(it -> getSections().remove(it));
        downLineStation.ifPresent(it -> getSections().remove(it));
    }

    private void addSectionIfBothStationsOf(final Optional<Section> upLineStation, final Optional<Section> downLineStation) {
        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Section downLineSection = downLineStation.get();
            Section upLineSection = upLineStation.get();
            getSections().add(Section.of(this,
                downLineSection.getUpStation(),
                upLineSection.getDownStation(),
                upLineSection.getDistance() + downLineSection.getDistance())
            );
        }
    }

    private Optional<Section> findSectionBasedOnUpStation(Station station) {
        return getSections().stream()
            .filter(it -> it.getUpStation() == station)
            .findFirst();
    }

    private Optional<Section> findSectionBasedOnDownStation(Station station) {
        return getSections().stream()
            .filter(it -> it.getDownStation() == station)
            .findFirst();
    }
}
