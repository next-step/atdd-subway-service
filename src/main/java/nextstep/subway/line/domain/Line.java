package nextstep.subway.line.domain;

import java.util.List;
import java.util.Objects;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nextstep.subway.BaseEntity;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.station.domain.Station;
import org.apache.commons.lang3.StringUtils;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;
    @Embedded
    @AttributeOverride(name = "fare", column = @Column(name = "additional_fare"))
    private Fare additionalFare;
    @Embedded
    private final Sections sections = new Sections();

    protected Line() {
    }

    private Line(LineBuilder lineBuilder) {
        this.id = lineBuilder.id;
        this.name = lineBuilder.name;
        this.color = lineBuilder.color;
        this.additionalFare = lineBuilder.additionalFare;
        sections.addSection(Section.builder(this, lineBuilder.upStation, lineBuilder.downStation, lineBuilder.distance)
                .build());
    }

    public static LineBuilder builder(String name, String color, Station upStation, Station downStation,
                                      Distance distance) {
        return new LineBuilder(name, color, upStation, downStation, distance);
    }

    public static class LineBuilder {
        private Long id;
        private final String name;
        private final String color;
        private final Station upStation;
        private final Station downStation;
        private final Distance distance;
        private Fare additionalFare = Fare.valueOf(0);

        private LineBuilder(String name, String color, Station upStation, Station downStation, Distance distance) {
            validateParameter(name, color);
            this.name = name;
            this.color = color;
            this.upStation = upStation;
            this.downStation = downStation;
            this.distance = distance;
        }

        private void validateParameter(String name, String color) {
            validateNameNotNull(name);
            validateColorNotNull(color);
        }

        private void validateNameNotNull(String name) {
            if (StringUtils.isEmpty(name)) {
                throw new NotFoundException("이름 정보가 없습니다.");
            }
        }

        private void validateColorNotNull(String color) {
            if (StringUtils.isEmpty(color)) {
                throw new NotFoundException("칼라 정보가 없습니다.");
            }
        }

        public LineBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public LineBuilder additionalFare(Fare additionalFare) {
            this.additionalFare = additionalFare;
            return this;
        }

        public Line build() {
            return new Line(this);
        }
    }

    public void update(Line line) {
        this.name = line.name();
        this.color = line.color();
    }

    public void addSection(Section newSection) {
        sections.addSection(newSection);
    }

    public void deleteSection(Station station) {
        sections.deleteSection(station);
    }

    public List<Station> orderedStations() {
        return sections.orderedStations();
    }

    public Sections sections() {
        return sections;
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String color() {
        return color;
    }

    public Fare additionalFare() {
        return additionalFare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Line line = (Line) o;
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
