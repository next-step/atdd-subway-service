package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import nextstep.subway.BaseEntity;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.domain.PathGraph;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

@Entity
public class Line extends BaseEntity {
    private static final int USED_LINE_MINIMUM_STATION_COUNT = 2;
    public static final int DEFAULT_SURCHARGE = 0;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;
    private int surcharge;

    @Embedded
    private Sections sections;

    @Embedded
    private LineStations lineStations;

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, int surcharge) {
        this(name, color);
        validateSurcharge(surcharge);
        this.surcharge = surcharge;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        this.sections = new Sections(Arrays.asList(new Section(this, upStation, downStation, distance)));
        this.lineStations = new LineStations(Arrays.asList(new LineStation(this, upStation)));
        this.lineStations.add(new LineStation(this, downStation));
        this.surcharge = DEFAULT_SURCHARGE;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance, int surcharge) {
        this(name, color, upStation, downStation, distance);
        validateSurcharge(surcharge);
        this.surcharge = surcharge;
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
        this.lineStations.add(new LineStation(this, section.getUpStation()));
        this.lineStations.add(new LineStation(this, section.getDownStation()));
    }

    public void removeSectionBy(Station station) {
        this.sections.removeStation(this, station);
        this.lineStations.remove(new LineStation(this, station));
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

    public boolean isUsedBy(List<Station> stations) {
        List<Station> targetStations = this.sections.getStations();
        targetStations.retainAll(stations);
        return targetStations.size() >= USED_LINE_MINIMUM_STATION_COUNT;
    }

    public int getSurcharge() {
        return surcharge;
    }

    private void validateSurcharge(int surcharge) {
        if (surcharge < DEFAULT_SURCHARGE) {
            throw new IllegalArgumentException("추가요금으로 음수를 입력할 수 없습니다.");
        }
    }
}
