package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

@Entity
public class Line extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    private Sections sections;

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(Long id, String name, String color, Station upStation, Station downStation, int distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        Section section = new Section(this, upStation, downStation, distance);
        List<Section> objects = new ArrayList<>();
        objects.add(section);
        this.sections = new Sections(objects);
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this(null, name, color, upStation, downStation, distance);
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

    public Stations getStations() {
        return this.sections.getStations();
    }

    public void addSection(Section newSection) {
        this.sections.add(newSection);
    }

    public void removeStation(Station station) {
        if (canRemoveStation()) {
            throw new IllegalArgumentException("삭제할 수 있는 Section이 없습니다.");
        }
        Optional<Section> upSection = findSectionByUpStation(station);
        Optional<Section> downSection = findSectionByDownStation(station);

        upSection.ifPresent(it -> this.sections.remove(it));
        downSection.ifPresent(it -> this.sections.remove(it));

        if (upSection.isPresent() && downSection.isPresent()) {
            rearrangeSection(upSection.get(), downSection.get());
        }

    }
    private boolean canRemoveStation() {
        return this.sections.size() <= 1;
    }

    private Optional<Section> findSectionByUpStation(Station station) {
        return sections.findUpStation(station);
    }

    private Optional<Section> findSectionByDownStation(Station station) {
        return sections.findDownStation(station);
    }
    private void rearrangeSection(Section upSection, Section downSection) {
        Station newUpStation = downSection.getUpStation();
        Station newDownStation = upSection.getDownStation();
        int newDistance = upSection.getDistance() + downSection.getDistance();
        this.sections.add(new Section(this, newUpStation, newDownStation, newDistance));
    }
}
