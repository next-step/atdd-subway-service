package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    private Sections sections = new Sections();

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
        return sections.toCollection();
    }

    public List<Station> sortedStation2() {
        List<Station> results = new ArrayList<>();

        List<Section> copiedSections = new ArrayList<>(sections.toCollection());

        TopSection topSection = new TopSection(copiedSections);

        if (topSection.hasTopSection()) {
            results.add(topSection.getTopSection().getUpStation());
        }

        while (topSection.hasTopSection()) {
            Section section = topSection.getTopSection();
            results.add(section.getDownStation());
            copiedSections.remove(section);

            topSection = new TopSection(copiedSections);
        }

        return results;
    }

    public SortedStations sortedStation() {
        return sections.toSortedStations();
    }

    public void removeStation(Station station) {
        NewSection newSection = sections.removeStation(station);
        if (newSection != null) {
            Section section = newSection.toSection(this);
            sections.add(section);
        }
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        Section section = new Section(this, upStation, downStation, distance);
        sections.add(section);
    }
}
