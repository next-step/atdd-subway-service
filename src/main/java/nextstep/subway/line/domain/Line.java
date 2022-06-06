package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    protected Line() {
    }

    private Line(LineBuilder lineBuilder) {
        this.id = lineBuilder.id;
        this.name = lineBuilder.name;
        this.color = lineBuilder.color;
        sections.add(Section.builder(this, lineBuilder.upStation, lineBuilder.downStation, lineBuilder.distance)
                .build());
    }

    public static Line createEmpty() {
        return new Line();
    }

    public static LineBuilder builder(String name, String color, Station upStation, Station downStation, Distance distance) {
        return new LineBuilder(name, color, upStation, downStation, distance );
    }

    public static class LineBuilder {
        private Long id;
        private final String name;
        private final String color;
        private final Station upStation;
        private final Station downStation;
        private final Distance distance;

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

        public Line build() {
            return new Line(this);
        }
    }

    public void update(Line line) {
        this.name = line.name();
        this.color = line.color();
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

    public List<Section> sections() {
        return sections;
    }
}
