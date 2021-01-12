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
    private Integer overFare;

    @Embedded
    private Sections sections = new Sections();

    public static class Builder {
        private Long id;
        private String name;
        private String color;
        private Station upStation;
        private Station downStation;
        private int distance;
        private int overFare;

        public Builder(Long id, String name, String color) {
            this.id = id;
            this.name = name;
            this.color = color;
        }

        public Builder(String name, String color) {
            this(null, name, color);
        }

        public Builder upStation(Station val) {
            upStation = val;
            return this;
        }

        public Builder downStation(Station val) {
            downStation = val;
            return this;
        }

        public Builder distance(int val) {
            distance = val;
            return this;
        }

        public Builder overFare(int val) {
            overFare = val;
            return this;
        }

        public Line build() {
            return new Line(this);
        }
    }

    private Line(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.color = builder.color;
        this.overFare = builder.overFare;
        sections.addSection(this, builder.upStation, builder.downStation, builder.distance);
    }

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this(name, color, upStation, downStation, distance, 0);
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance, int overFare) {
        this.name = name;
        this.color = color;
        this.overFare = overFare;
        sections.addSection(this, upStation, downStation, distance);
    }

    public Line(Long id, String name, String color, Station upStation, Station downStation, int distance) {
        this(name, color, upStation, downStation, distance);
        this.id = id;
    }

    public Line(Long id, String name, String color, Station upStation, Station downStation, int distance, int overFare) {
        this(name, color, upStation, downStation, distance, overFare);
        this.id = id;
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

    public Integer getOverFare() {
        return overFare;
    }

    public Station findUpStation() {
        return sections.findUpStation();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void addLineStation(Station upStation, Station downStation, int distance) {
        sections.addLineStation(this, upStation, downStation, distance);
    }

    public void removeLineStation(Station station) {
        sections.removeLineStation(this, station);
    }

    public boolean containsAnyStation(List<Station> stations) {
        return stations.stream()
                .anyMatch(station -> getStations().contains(station));
    }

}
