package nextstep.subway.line.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    private String color;

    @Embedded
    private Sections sections;

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this(name, color, upStation, downStation, distance, 0);
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance, int extraCharge) {
        this.name = name;
        this.color = color;
        initializeSection();
        sections.add(new Section(this, upStation, downStation, distance, extraCharge));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void addSection(Station upStation, Station downStation, int distance, int extraCharge) {
        initializeSection();
        sections.add(new Section(this, upStation, downStation, distance, extraCharge));
    }

    public Stations getStations() {
        initializeSection();
        return sections.getStationsRelativeOrder();
    }

    public void removeSection(Station station) {
        initializeSection();
        sections.removeSection(station);
    }

    public LocalDateTime getCreatedDate() {
        return super.createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return super.modifiedDate;
    }

    private void initializeSection() {
        if (sections == null) {
            sections = new Sections();
        }
    }
}
