package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
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

    @Embedded
    private Sections sections = Sections.create();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;

        sections.addSection(new Section(this, upStation, downStation, distance));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        return sections.orderBySection();
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

    public void addLineSection(Station upStation, Station downStation, int distance) {
        boolean isUpStationExisted = sections.isExisted(upStation);
        boolean isDownStationExisted = sections.isExisted(downStation);

        sections.valid(isUpStationExisted, isDownStationExisted);

        if (isUpStationExisted) {
            sections.updateUpStation(upStation, downStation, distance);
        }

        if (isDownStationExisted) {
            sections.updateDownStation(upStation, downStation, distance);
        }

        sections.addSection(new Section(this, upStation, downStation, distance));
    }

    public void removeStation(Long stationId) {
        Optional<Section> upLineStation = sections.findUpStation(stationId);
        Optional<Section> downLineStation = sections.findDownStation(stationId);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            createSection(upLineStation.get(), downLineStation.get());
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    private void createSection(Section upLineStation, Section downLineStation) {
        Station newUpStation = downLineStation.getUpStation();
        Station newDownStation = upLineStation.getDownStation();
        int newDistance = upLineStation.getDistance() + downLineStation.getDistance();
        sections.addSection(new Section(this, newUpStation, newDownStation, newDistance));
    }
}
