package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.Optional;
import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line extends BaseEntity {
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
        if (this.sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        stations.add(this.findFirstSection().getUpStation());
        for (int i = 0; i < sections.size(); i++) {
            Optional<Section> sectionByUpStation = findSectionByUpStation(stations.get(i));
            sectionByUpStation.map(Section::getDownStation)
                    .ifPresent(stations::add);
        }
        return stations;
    }

    private Optional<Section> findSectionByUpStation(Station station) {
        return sections.stream().filter(section -> section.isEqualsUpStation(station)).
                findFirst();
    }

    private Section findFirstSection() {
        List<Station> downStations = findDownStations();
        return sections.stream().filter(section -> !downStations.contains(section.getUpStation())).
                findFirst().
                orElseThrow(IllegalArgumentException::new);
    }

    private List<Station> findUpStations() {
        List<Station> result = new ArrayList<>();
        sections.stream().forEach(section -> result.add(section.getUpStation()));
        return result;
    }

    private List<Station> findDownStations() {
        List<Station> result = new ArrayList<>();
        sections.stream().forEach(section -> result.add(section.getDownStation()));
        return result;
    }

}
