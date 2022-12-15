package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;
import org.springframework.util.StringUtils;

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
    private final Sections sections = new Sections();

    @Embedded
    private ExtraFee extraFee = new ExtraFee();

    protected Line() {
    }

    private Line(Builder builder) {
        this.name = builder.name;
        this.color = builder.color;
        this.extraFee = ExtraFee.from(builder.extraFee);
    }

    public Line(String name, String color, Station upStation, Station downStation, Distance distance, ExtraFee extraFee) {
        this.name = name;
        this.color = color;
        this.extraFee = extraFee;
        sections.addSection(this, upStation, downStation, distance);
    }

    public static Line of(String name, String color, Station upStation, Station downStation, Distance distance, int extraFee) {
        return new Line(name, color, upStation, downStation, distance, ExtraFee.from(extraFee));
    }

    public static class Builder {
        private String name;
        private String color;

        private int extraFee;

        public Builder() {
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder extraFee(int extraFee) {
            this.extraFee = extraFee;
            return this;
        }

        public Line build() {
            return new Line(this);
        }
    }

    public void update(String name, String color) {
        if (StringUtils.hasText(name)) {
            this.name = name;
        }

        if (StringUtils.hasText(color)) {
            this.color = color;
        }
    }

    public void addSection(Section section, Distance distance) {
        sections.addSection(this, section.getUpStation(), section.getDownStation(), distance);
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        sections.addSection(this, upStation, downStation, Distance.from(distance));
    }

    public void removeLineStation(Station station) {
        sections.removeLineStation(this, station);
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

    public int getExtraFee() {
        return extraFee.getExtraFee();
    }
}

