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

    public static class Builder {
        private String name;
        private String color;
        private Station upStation;
        private Station downStation;
        private int distance;

        public Builder(String name, String color) {
            this.name = name;
            this.color = color;
        }

        public Builder upStation(Station station) {
            this.upStation = station;
            return this;
        }

        public Builder downStation(Station station) {
            this.downStation = station;
            return this;
        }

        public Builder distance(int distance) {
            this.distance = distance;
            return this;
        }

        public Line build() {
            return new Line(name, color, upStation, downStation, distance);
        }
    }

    public Line() {
    }


    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;

        sections.add(
                new Section.Builder()
                        .line(this)
                        .upStation(upStation)
                        .downStation(downStation)
                        .distance(distance)
                        .build()
        );
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        sections.add(
                new Section.Builder()
                        .line(this)
                        .upStation(upStation)
                        .downStation(downStation)
                        .distance(distance)
                        .build()
        );
    }

    public void removeStation(Station station) {
        sections.removeStation(station);
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
