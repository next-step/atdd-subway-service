package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Line extends BaseEntity {
    public static final int DIFFERENCE_SECTIONS_STATIONS_SIZE = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

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

    public static Line of(LineRequest request, Station upStation, Station downStation) {
        return new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance());
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

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        Station fistStation = findUpStation();
        stations.add(fistStation);
        Station upStation = fistStation;

        while (stations.size() != sections.size() + DIFFERENCE_SECTIONS_STATIONS_SIZE) {
            upStation = getUpStation(stations, upStation);
        }
        return stations;
    }

    private Station getUpStation(List<Station> stations, Station upStation) {
        for (Section section : sections) {
            upStation = getDownStation(stations, upStation);
        }
        return upStation;
    }

    private Station getDownStation(List<Station> stations, Station upStation) {
        Station finalUpStation = upStation;
        Station downStation = sections.stream()
                .filter(section1 -> Objects.equals(section1.getUpStation(), finalUpStation))
                .map(Section::getDownStation)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
        stations.add(downStation);
        upStation = downStation;
        return upStation;

    }

    public Station findUpStation() {
        List<Station> upStations = sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
        List<Station> downStations = sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());

        upStations.removeAll(downStations);

        return upStations.get(0);
    }
}
