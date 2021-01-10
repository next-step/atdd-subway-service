package nextstep.subway.line.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    @Builder
    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        this.sections = new Sections(Arrays.asList(Section.builder().line(this)
                .upStation(upStation)
                .downStation(downStation)
                .distance(new Distance(distance))
                .build()));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void add(Section section) {
        sections.addSection(section);
    }

    public void remove(Station station) {
        sections.removeSection(this, station);
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
        return sections.getStations();
    }

    public List<Section> getSections() {
        return sections.getSections();
    }
}
