package nextstep.subway.line.domain;

import java.util.Collections;
import java.util.Optional;
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

    @Embedded
    private final Sections sections = new Sections();

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

    public void addSection(Section newSection) {

        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(station -> station == newSection.upStation());
        boolean isDownStationExisted = stations.stream().anyMatch(station -> station == newSection.downStation());

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == newSection.upStation()) &&
                stations.stream().noneMatch(it -> it == newSection.downStation())) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }

        if (stations.isEmpty()) {
            this.sections().add(Section.builder(this, newSection.upStation(), newSection.downStation(), newSection.distance())
                    .build());
            return;
        }

        if (isUpStationExisted) {
            this.sections().sections().stream()
                    .filter(section -> section.upStation() == newSection.upStation())
                    .findFirst()
                    .ifPresent(section -> section.updateUpStation(newSection.downStation(), newSection.distance()));

            this.sections().add(Section.builder(this, newSection.upStation(), newSection.downStation(), newSection.distance())
                    .build());
        } else if (isDownStationExisted) {
            this.sections().sections().stream()
                    .filter(section -> section.downStation() == newSection.downStation())
                    .findFirst()
                    .ifPresent(section -> section.updateDownStation(newSection.upStation(), newSection.distance()));

            this.sections().add(Section.builder(this, newSection.upStation(), newSection.downStation(), newSection.distance())
                    .build());
        } else {
            throw new RuntimeException();
        }
    }

    public List<Station> getStations() {
        if (this.sections().sections().isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = this.sections().sections().stream()
                    .filter(section -> section.upStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().downStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Station findUpStation() {
        Station downStation = this.sections().sections().get(0).upStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = this.sections().sections().stream()
                    .filter(section -> section.downStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().upStation();
        }

        return downStation;
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

    public Sections sections() {
        return sections;
    }
}
