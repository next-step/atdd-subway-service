package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.*;

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

    public void addSection(final Section section) {
        if (!Objects.equals(this, section.getLine())) {
            throw new IllegalStateException("노선 정보가 다릅니다.");
        }
        sections.add(section);
    }

    public Optional<Station> getStartStation() {
        if (sections.isEmpty()) {
            return Optional.empty();
        }
        return sections.stream().filter(this::isPreSection)
                .map(Section::getUpStation).findAny();
    }

    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return Collections.emptyList();
        }
        return insertStationBySorted();
    }

    public int isSize() {
        return this.sections.size();
    }

    private List<Station> insertStationBySorted() {
        List<Station> result = new ArrayList<>();
        Optional<Station> isStartStation = getStartStation();
        while (isStartStation.isPresent()) {
            Station station = isStartStation.get();
            result.add(station);
            isStartStation = findNextStation(station);
        }
        return result;
    }

    private Optional<Station> findNextStation(final Station station) {
        return sections.stream()
                .filter(section -> section.isMatchUpStation(station))
                .map(Section::getDownStation)
                .findAny();
    }

    private boolean isPreSection(final Section source) {
        Optional<Section> isPreSection = sections.stream()
                .filter(section -> !section.isMatchDownStation(source.getUpStation()) &&
                        section.isMatchUpStation(source.getDownStation())).findAny();
        return isPreSection.isPresent();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}
