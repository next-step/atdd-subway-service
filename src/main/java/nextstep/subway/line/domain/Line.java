package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.line.exeption.CanNotDeleteStateException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.exeption.CanNotAddStationException;
import nextstep.subway.station.exeption.RegisteredStationException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Entity
public class Line extends BaseEntity {

    public static final int FIRST_INDEX = 0;
    public static final int MIN_SIZE = 1;

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
        List<Section> searchList = createSearchSections();
        Set<Station> stations = new LinkedHashSet<>();
        for (Section section : searchList) {
            sections.stream().filter(s -> isConnectUpStation(s, section))
                    .findFirst()
                    .ifPresent(s -> stations.addAll(Arrays.asList(s.getUpStation(), s.getDownStation())));
            stations.addAll(Arrays.asList(section.getUpStation(), section.getDownStation()));
        }
        return new ArrayList<>(stations);
    }

    private List<Section> createSearchSections() {
        if (sections.isEmpty()) {
            return new ArrayList<>();
        }

        ArrayList<Section> searchList = new ArrayList<>(this.sections);
        Section findSection = findFirstSection(sections.get(FIRST_INDEX));
        searchList.remove(findSection);
        searchList.add(FIRST_INDEX, findSection);

        return searchList;
    }

    private Section findFirstSection(Section section) {
        Section findSection = sections.stream().filter(s -> isConnectUpStation(s, section)).findFirst().orElse(null);
        return Objects.isNull(findSection) ? section : findFirstSection(findSection);
    }

    private boolean isConnectUpStation(Section target, Section compare) {
        return !target.equals(compare) && target.getDownStation().equals(compare.getUpStation());
    }

    public void addStation(Station upStation, Station downStation, int distance) {
        validateStation(upStation, downStation);

        sections.stream()
                .filter(s -> s.getUpStation().equals(upStation) || s.getDownStation().equals(downStation))
                .findFirst()
                .ifPresent(s -> {
                    s.updateStation(upStation, downStation, distance);
                });
        sections.add(new Section(this, upStation, downStation, distance));
    }

    private void validateStation(Station upStation, Station downStation) {
        if (!sections.isEmpty() && containsStation(upStation) && containsStation(downStation)) {
            throw new RegisteredStationException();
        }
        if (!sections.isEmpty() &&!containsStation(upStation) && !containsStation(downStation)) {
            throw new CanNotAddStationException();
        }
    }
    public boolean containsStation(Station station) {
        return sections.stream().anyMatch(s -> s.containStation(station));
    }

    public void removeStation(Station station) {
        if (canNotDelete()) {
            throw new CanNotDeleteStateException();
        }

        Section upLineStation = sections.stream().filter(s -> s.getDownStation().equals(station))
                                    .findFirst().map(s -> {
                                        sections.remove(s);
                                        return s;
                                    }).orElse(null);
        Section downLineStation = sections.stream().filter(s -> s.getUpStation().equals(station))
                                    .findFirst().map(s -> {
                                        sections.remove(s);
                                        return s;
                                    }).orElse(null);

        if (existSections(upLineStation, downLineStation)) {
            int newDistance = upLineStation.getDistance() + downLineStation.getDistance();
            Section section = new Section(this, upLineStation.getUpStation(), downLineStation.getDownStation(), newDistance);
            sections.add(section);
        }
    }

    private boolean canNotDelete() {
        return sections.size() == MIN_SIZE;
    }

    private boolean existSections(Section ... sections) {
        return Arrays.stream(sections).noneMatch(Objects::isNull);
    }
}
