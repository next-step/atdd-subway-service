package nextstep.subway.line.domain;

import java.util.function.Consumer;
import nextstep.subway.BaseEntity;
import nextstep.subway.generic.domain.Price;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @Embedded
    private Sections sections = new Sections();

    private Price surcharge = Price.valueOf(0);

    public Line() {
    }

    public Line(String name, String color, int surcharge) {
        this.name = name;
        this.color = color;
        this.surcharge = Price.valueOf(surcharge);
    }

    public Line(String name, String color, int surcharge, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        this.surcharge = Price.valueOf(surcharge);
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
        return sections.getStations();
    }

    public Price getSurcharge() {
        return surcharge;
    }

    public void addSection(final Section section) {
        sections.add(section);
    }

    public void removeStation(final Station station) {
        sections.removeStation(station);
    }

    public boolean hasSection(Section section) {
        return sections.containsStations(section);
    }

    public Section bindDistance(Section section) {
        return sections.bindDistance(section);
    }

    public void foreachSections(Consumer<Section> consumer) {
        sections.foreach(consumer);
    }
}
