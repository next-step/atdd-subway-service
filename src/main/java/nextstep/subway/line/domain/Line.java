package nextstep.subway.line.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nextstep.subway.BaseEntity;
import nextstep.subway.line.dto.LineRequest;
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
    private Fare fare = Fare.of(0);

    @Embedded
    private Sections sections;

    protected Line() {
    }


    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    private Line(Builder builder) {
        this(builder.name, builder.color);
        Section section = new Section(this, builder.upStation, builder.downStation, builder.distance);
        this.sections = new Sections(section);
        this.fare = builder.fare;
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public static Line of(LineRequest request, Station upStation, Station downStation) {
        return new Line.Builder()
                .color(request.getColor())
                .name(request.getName())
                .distance(request.getDistance())
                .fare(request.getFare())
                .upStation(upStation)
                .downStation(downStation)
                .build();
    }


    public void changeFare(int fare) {
        this.fare = Fare.of(fare);
    }

    public void addSection(Section section) {
        sections.addSection(section);
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

    public Fare getFare() {
        return fare;
    }

    public Stations getStations() {
        return Stations.of(sections.getOrderStations());
    }

    public Sections getSections() {
        return sections;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Line)) {
            return false;
        }
        Line line = (Line) o;
        return Objects.equals(getName(), line.getName()) && Objects.equals(getColor(), line.getColor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getColor());
    }

    public static class Builder {
        private String name;
        private String color;
        private Fare fare = Fare.of(0);
        private Station upStation;
        private Station downStation;

        private int distance;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder fare(Fare fare) {
            this.fare = fare;
            return this;
        }

        public Builder fare(int fare) {
            this.fare = Fare.of(fare);
            return this;
        }

        public Builder upStation(Station upStation) {
            this.upStation = upStation;
            return this;
        }

        public Builder downStation(Station downStation) {
            this.downStation = downStation;
            return this;
        }

        public Builder distance(int distance) {
            this.distance = distance;
            return this;
        }

        public Line build() {
            return new Line(this);
        }
    }
}
