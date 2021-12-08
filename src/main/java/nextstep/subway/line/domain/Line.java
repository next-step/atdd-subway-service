package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.exception.ExistedSectionException;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

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

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        this.sections.add(new Section(this, upStation, downStation, distance));
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
        return sections.list();
    }

    private Station findDownStation(Station downStation) {
        return this.sections.findDownStation(downStation);
    }

    private Station findUpStation() {
        return this.sections.findUpStation();
    }

    public void addSection(Station upStation, Station downStation, Integer distance) {
        if(this.sections.isEmpty()) {
            this.sections.add(new Section(this, upStation, downStation, distance));
            return;
        }

        this.sections.add(this, upStation, downStation, distance);
    }

    public List<Station> getStationsByOrder() {
        if (this.getSections().isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = this.findUpStation();
        Station finalDownStation = null;

        while(downStation != finalDownStation) {
            stations.add(downStation);
            finalDownStation = downStation;
            downStation = findDownStation(finalDownStation);
        }

        return stations;
    }

    public void removeSection(Station station) {
        this.sections.remove(station);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(name, line.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
