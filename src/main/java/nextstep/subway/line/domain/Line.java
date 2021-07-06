package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import nextstep.subway.BaseEntity;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.domain.PathGraph;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;
    private int surcharge;

    @Embedded
    private Sections sections;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
    private List<LineStation> lineStations = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, int surcharge) {
        this(name, color);
        this.surcharge = surcharge;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        this.sections = new Sections(Arrays.asList(new Section(this, upStation, downStation, distance)));
        this.lineStations.add(new LineStation(this, upStation));
        this.lineStations.add(new LineStation(this, downStation));
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance, int surcharge) {
        this(name, color, surcharge);
        this.sections = new Sections(Arrays.asList(new Section(this, upStation, downStation, distance)));
        this.lineStations.add(new LineStation(this, upStation));
        this.lineStations.add(new LineStation(this, downStation));
    }

    public void update(Line line) {
        this.name = line.name;
        this.color = line.color;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public void removeSectionBy(Station station) {
        this.sections.removeStation(this, station);
    }

    public LineResponse toLineResponse() {
        List<StationResponse> stationResponses = this.sections.getStations()
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new LineResponse(id, name, color, stationResponses, getCreatedDate(), getModifiedDate());
    }

    public void addPathInfoTo(PathGraph graph) {
        this.sections.addPathInfoTo(graph);
    }

    public int getSurcharge() {
        return surcharge;
    }
}
