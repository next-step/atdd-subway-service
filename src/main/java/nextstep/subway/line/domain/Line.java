package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
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
    private final Sections sections;

    public Line() {
        this.sections = new Sections();
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
        this.sections = new Sections();
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        this.sections = new Sections();
        this.sections.addSection(new Section(this, upStation, downStation, distance));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void addSection(Section section) {
        sections.addSection(section);
    }

    public void removeSection(Section section) {
        sections.removeSection(section);
    }

    public void updateSectionOfUpStation(Station upStation, Station downStation, int distance) {
        sections.updateSectionOfUpStation(upStation, downStation, distance);
    }

    public void updateSectionOfDownStation(Station upStation, Station downStation, int distance) {
        sections.updateSectionOfDownStation(upStation, downStation, distance);
    }

    public int sectionsSize() {
        return sections.sectionsSize();
    }

    public Optional<Section> findSectionByUpStation(Station station) {
        return sections.findSectionByUpStation(station);
    }

    public Optional<Section> findSectionByDownStation(Station station) {
        return sections.findSectionByDownStation(station);
    }

    public void reRegisterSection(Section upSection, Section downSection) {
        Station reUpStation = downSection.getUpStation();
        Station reDownStation = upSection.getDownStation();
        int reDistance = upSection.getDistance() + downSection.getDistance();
        addSection(new Section(this, reUpStation, reDownStation, reDistance));
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

    public Sections getSections() {
        return sections;
    }
}
