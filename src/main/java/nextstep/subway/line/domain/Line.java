package nextstep.subway.line.domain;

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
    private int extraCharge;

    @Embedded
    private Sections sections = new Sections();

    protected Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    private Line(String name, String color, Station upStation, Station downStation, int distance, int extraCharge) {
        this.name = name;
        this.color = color;
        sections.addFirstSection(new Section(this, upStation, downStation, distance));
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

    public int getExtraCharge() {
        return extraCharge;
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public Sections getSections() {
        return sections;
    }

    public void addSection(Section section) {
        sections.addSection(section);
    }

    public void deleteStation(Station station) {
        sections.deleteStation(station);
    }

    public static class Builder {
        private String name;
        private String color;
        private Station upStation;
        private Station downStation;
        private int distance;
        private int extraCharge;

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

        public Builder extraCharge(int extraCharge) {
            this.extraCharge = extraCharge;
            return this;
        }

        public Line build() {
            return new Line(name, color, upStation, downStation, distance, extraCharge);
        }
    }
}
