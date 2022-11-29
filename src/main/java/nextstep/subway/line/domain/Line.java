package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
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

    protected Line() {
    }


    private Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        this.sections.add(Section.of(this, upStation, downStation, distance));
    }
    private Line(String name, String color) {
        this.name = name;
        this.color = color;
    }


    public static Line ofEmpty(){
        return new Line();
    }

    public static Line ofNameAndColor(String name, String color){
        return new Line(name, color);
    }
    public static Line of(String name, String color, Station upStation, Station downStation, int distance){
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

    public void addSection(Section section) {
        sections.add(section);
    }

    public void removeStation(Station station) {
        sections.deleteStation(station);
    }

    public List<Station> getOrderedStations() {
        return sections.getOrderedStations();
    }
    public boolean containSection(Section section){
        return sections.contains(section);
    }
}
