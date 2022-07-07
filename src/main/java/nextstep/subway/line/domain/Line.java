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

    private int surcharge;

    public Line() {
    }

    public static class Builder {
        private String name;
        private String color;
        private Station upStation;
        private Station downStation;
        private int distance;
        private int surcharge;

        public Builder(String name, String color, Station upStation, Station downStation, int distance) {
            this.name = name;
            this.color = color;
            this.upStation = upStation;
            this.downStation = downStation;
            this.distance = distance;
            this.surcharge = 0;
        }

        public Builder surcharge(int surcharge) {
            this.surcharge = surcharge;
            return this;
        }

        public Line build() {
            return new Line(this);
        }
    }

    private Line(Builder builder) {
        this.name = builder.name;
        this.color = builder.color;
        sections.add(new Section(this, builder.upStation, builder.downStation, builder.distance));
        this.surcharge = builder.surcharge;
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public List<Station> getStations() {
        return sections.getStations();
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

    public void addSection(Section section) {
        sections.add(section);
    }

    public void removeSectionByStation(Station station) {
        sections.removeSectionByStation(station);
    }

    public int getSurcharge() {
        return surcharge;
    }
}
