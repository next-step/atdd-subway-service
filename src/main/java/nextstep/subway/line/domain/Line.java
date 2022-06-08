package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
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
        this(null, null);
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
        this.sections = new Sections();
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this(name, color);
        addSection(upStation, downStation, distance);
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void removeSection(Section section) {
        sections.removeSection(section);
    }

    public int sectionsSize() {
        return sections.sectionsSize();
    }

    public List<Station> findStations() {
        return sections.findStations();
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        sections.addSection(new Section(this, upStation, downStation, distance));
    }

    public void removeSectionByStation(Station station) {
        if (sectionsSize() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> upSection = sections.findSectionByUpStation(station);
        Optional<Section> downSection = sections.findSectionByDownStation(station);

        if (upSection.isPresent() && downSection.isPresent()) {
            reRegisterSection(upSection.get(), downSection.get());
        }

        upSection.ifPresent(this::removeSection);
        downSection.ifPresent(this::removeSection);
    }

    private void reRegisterSection(Section upSection, Section downSection) {
        Station reUpStation = downSection.getUpStation();
        Station reDownStation = upSection.getDownStation();
        int reDistance = upSection.getDistance() + downSection.getDistance();
        sections.addSection(new Section(this, reUpStation, reDownStation, reDistance));
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
}
