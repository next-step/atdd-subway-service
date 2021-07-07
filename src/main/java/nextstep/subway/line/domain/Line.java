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
    private int additionalFare;

    @Embedded
    private Sections sections = new Sections();

    protected Line() {
        // empty
    }

    public Line(String name, String color, int additionalFare) {
        this.name = name;
        this.color = color;
        this.additionalFare = additionalFare;
    }

    private Line(final Builder builder) {
        this(builder.name, builder.color, builder.additionalFare);
        sections.addSection(this, builder.upStation, builder.downStation, builder.distance);
    }

    public void update(Line line) {
        this.name = line.name;
        this.color = line.color;
        this.additionalFare = line.additionalFare;
    }

    public List<Station> getStations() {
        return this.sections.getStations();
    }

    public Sections getSections() {
        return this.sections;
    }

    public void addSection(final Station upStation, final Station downStation, final int distance) {
        this.sections.addSection(this, upStation, downStation, distance);
    }

    public void removeSection(final Station station) {
        this.sections.removeSection(this, station);
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

    public Integer getAdditionalFare() {
        return additionalFare;
    }

    public static class Builder {
        private final String name;
        private String color;
        private Station upStation;
        private Station downStation;
        private int distance;
        private int additionalFare;

        public Builder(final String name) {
            this.name = name;
        }

        public Builder color(final String color) {
            this.color = color;
            return this;
        }

        public Builder upStation(final Station upStation) {
            this.upStation = upStation;
            return this;
        }

        public Builder downStation(final Station downStation) {
            this.downStation = downStation;
            return this;
        }

        public Builder distance(final int distance) {
            this.distance = distance;
            return this;
        }

        public Builder additionalFare(final int additionalFare) {
            this.additionalFare = additionalFare;
            return this;
        }

        public Line build() {
            return new Line(this);
        }
    }
}
