package nextstep.subway.line.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.BaseEntity;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Line extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @Embedded
    private Fare fare;

    @Embedded
    private Sections sections;

    public Line(String name, String color, int fare) {
        this.name = name;
        this.color = color;
        this.fare = Fare.of(fare);
    }

    @Builder
    public Line(String name, String color, Station upStation, Station downStation, int distance, int fare) {
        this.name = name;
        this.color = color;
        this.sections = new Sections(this, upStation, downStation, distance);
        this.fare = Fare.of(fare);
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public void remove(Station station) {
        sections.removeSection(this, station);
    }

    public List<Section> getAllSection() {
        return Collections.unmodifiableList(sections.getSections());
    }

    public Fare getFare() {
        return fare;
    }
}
