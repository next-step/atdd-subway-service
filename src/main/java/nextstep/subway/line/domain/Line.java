package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.line.exception.IllegalLineArgumentException;
import nextstep.subway.station.domain.Station;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;
import java.util.Objects;

@Entity
public class Line extends BaseEntity {
    private static final String LINE_NAME_EMPTY_ERROR_MESSAGE = "노선의 이름값이 비어있습니다.";
    private static final String LINE_COLOR_EMPTY_ERROR_MESSAGE = "노선의 색상값이 비어있습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    private String color;

    @Embedded
    private SectionGroup sectionGroup;

    protected Line() {
    }

    private Line(String name, String color) {

        if (Objects.isNull(name)) {
            throw new IllegalLineArgumentException(LINE_NAME_EMPTY_ERROR_MESSAGE);
        }

        if (Objects.isNull(color)) {
            throw new IllegalLineArgumentException(LINE_COLOR_EMPTY_ERROR_MESSAGE);
        }

        this.name = name;
        this.color = color;
    }

    protected Line(String name, String color, List<Section> sections) {
        this(name, color);
        this.sectionGroup = SectionGroup.of(sections);
    }

    protected Line(String name, String color, Station upStation, Station downStation, int distance) {
        this(name, color);
        this.sectionGroup = SectionGroup.of(Section.of(this, upStation, downStation, distance));
    }

    private Line(String name, String color, Section section) {
        this(name, color);
        this.sectionGroup = SectionGroup.of(section);
    }

    public static Line of(String name, String color, List<Section> sections) {
        return new Line(name, color, sections);
    }

    public static Line of(String name, String color, Section section) {
        return new Line(name, color, section);
    }

    public static Line of(String name, String color, Station upStation, Station downStation, int distance) {
        return new Line(name, color, upStation, downStation, distance);
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateColor(String color) {
        this.color = color;
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
        return this.sectionGroup.getSections();
    }

    public List<Station> getStations() {
        return this.sectionGroup.getStations();
    }

    public void addSection(Section section) {
        sectionGroup.addSection(section);
    }

    public void removeLineStation(Station station) {
        sectionGroup.removeLineStation(this, station);
    }
}
