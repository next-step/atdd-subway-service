package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.*;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;
    private int extraFare;

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, int extraFare) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        this.extraFare = 0;
        sections.addFirstSection(new Section(this, upStation, downStation, distance));
    }

    public Line(String name, String color, int extraFare, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        sections.addFirstSection(new Section(this, upStation, downStation, distance));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
        this.extraFare = line.getExtraFare();
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

    public int getExtraFare() {
        return extraFare;
    }

    private boolean isEmptySection() {
        return sections.isEmptySections();
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public List<Station> getStations() {

        if (isEmptySection()) {
            return Arrays.asList();
        }

        return sections.getStations();
    }

    public void addSection(Section section) {
        section.setLine(this);
        if (sections.isEmptySections()) {
            sections.addFirstSection(section);
            return;
        }

        sections.addSection(section);
    }

    public void removeLineStation(Station station) {
        Section newSection = sections.removeLineStation(station);

        if (newSection != null) {
            newSection.setLine(this);
        }
    }
}
