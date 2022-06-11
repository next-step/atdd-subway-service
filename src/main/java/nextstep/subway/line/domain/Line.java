package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void addSection(Section section) {
        List<Station> stations = getSections().getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == section.getUpStation());
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == section.getDownStation());

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == section.getUpStation()) &&
                stations.stream().noneMatch(it -> it == section.getDownStation())) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }

        if (stations.isEmpty()) {
            sections.addSection(this, section);
            return;
        }

        if (isUpStationExisted) {
            Optional<Section> find = sections.findSectionWithUpStation(section.getUpStation());
            find.ifPresent(frontSection -> sections.addSectionWhenUpStationSame(this, frontSection, section));
            sections.addSection(this, section);
        } else if (isDownStationExisted) {
            Optional<Section> find = sections.findSectionWithDownStation(section.getDownStation());
            find.ifPresent(rearSection -> sections.addSectionWhenDownStationSame(this, rearSection, section));
            sections.addSection(this, section);
        } else {
            throw new RuntimeException();
        }
    }

    public List<StationResponse> findStationResponses() {
        return getSections().getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse findLineResponse() {
        return LineResponse.of(this, findStationResponses());
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

    public Sections getSections() {
        return sections;
    }
}
