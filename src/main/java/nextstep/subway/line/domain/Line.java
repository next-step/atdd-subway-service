package nextstep.subway.line.domain;

import java.util.Collections;
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

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        this.sections = new Sections(Collections.singletonList(new Section(this, upStation, downStation, distance)));
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
        return sections.getSections();
    }


    public void addSection(Section newSection) {
        this.sections.add(newSection);
    }

    public boolean sectionAnyMatch(Station newStation) {
        return this.sections.anyMatch(newStation);
    }

    public Stations getStations() {
        return this.sections.getStations();
    }

    public boolean canRemoveStation() {
        return this.sections.size() <= 1;
    }

    public Optional<Section> findSectionByUpStation(Station station) {
        return sections.findUpStation(station);
    }

    public Optional<Section> findSectionByDownStation(Station station) {
        return sections.findDownStation(station);
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

    private void rearrangeSection(Section upSection, Section downSection) {
        Station newUpStation = downSection.getUpStation();
        Station newDownStation = upSection.getDownStation();
        int newDistance = upSection.getDistance() + downSection.getDistance();
        this.sections.add(new Section(this, newUpStation, newDownStation, newDistance));
    }
}
