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
        sections.validateInsertSection(section);

        sections.insertSectionWhenSectionIsHeadOrTail(this, section);

        if (sections.containBothStation(section)) {
            return;
        }

        Optional<Section> find = sections.findSectionWithUpStation(section.getUpStation());
        find.ifPresent(frontSection -> sections.insertSectionWhenUpStationSame(this, frontSection, section));

        if (sections.containBothStation(section)) {
            return;
        }

        Optional<Section> find2 = sections.findSectionWithDownStation(section.getDownStation());
        find2.ifPresent(rearSection -> sections.insertSectionWhenDownStationSame(this, rearSection, section));
        sections.addSection(this, section);

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
