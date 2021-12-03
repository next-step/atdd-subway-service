package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
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

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    private Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    private Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        Section.create(this, upStation, downStation, distance);
    }

    public static Line of(String name, String color) {
        return new Line(name, color);
    }

    public static Line of(String name, String color, Station upStation, Station downStation, int distance) {
        return new Line(name, color, upStation, downStation, distance);
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

    public List<Station> getStations() {

        if (this.sections.isEmpty()) {
            return Arrays.asList();
        }

        return this.sections.getStations().get();
    }

    public void removeSection(Section section) {
        sections.remove(section);
    }

    public boolean hasSection(Section section) {
        return sections.contains(section);
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public void addLineStation(Station upStation, Station downStation, int distance) {
        Stations stations = this.sections.getStations();

        if (stations.isEmpty()) {
            Section.create(this, upStation, downStation, distance);
            return;
        }

        validateForAdded(upStation, downStation);

        sections.updateSection(upStation, downStation, distance);
        addSection(Section.create(this, upStation, downStation, distance));
    }

    private void validateForAdded(Station upStation, Station downStation) {
        Stations stations = this.sections.getStations();
        if (stations.anyMatch(upStation) && stations.anyMatch(downStation)) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!stations.isEmpty() && stations.noneMatch(upStation) && stations.noneMatch(downStation)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

}
