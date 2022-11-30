package nextstep.subway.line.domain;

import java.util.Collections;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nextstep.subway.BaseEntity;
import nextstep.subway.common.exception.ErrorEnum;
import nextstep.subway.station.domain.Station;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @Embedded
    private Sections sections;

    protected Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        validateLine(upStation, downStation, distance);
        Section section = new Section(upStation, downStation, distance);
        section.addLine(this);
        this.sections = new Sections(Collections.singletonList(section));
    }

    private void validateLine(Station upStation, Station downStation, int distance) {
        if (upStation == null || downStation == null) {
            throw new IllegalArgumentException(ErrorEnum.VALID_STATION_REQUIRE.message());
        }

        if (distance == Distance.ZERO) {
            throw new IllegalArgumentException(ErrorEnum.VALID_LINE_LENGTH_GREATER_THAN_ZERO.message());
        }
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public String getColor() {
        return color;
    }

    public Set<Station> findStations() {
        return sections.orderedStations();
    }

    public void addSection(Section newSection) {
        newSection.addLine(this);
        this.sections.add(newSection);
    }

    public void deleteStation(Station station) {
        this.sections.delete(station);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
