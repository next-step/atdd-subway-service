package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    public int sectionCount() {
        return this.sections.size();
    }

    public boolean removeSection(Section section) {
        return this.sections.remove(section);
    }

	public boolean isSectionEmpty() {
		return this.sections.isEmpty();
	}

    public boolean addSection(Section section) {
        boolean isUpStationExisted = this.hasStation(section.getUpStation());
        boolean isDownStationExisted = this.hasStation(section.getDownStation());

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!this.sections.isEmpty() && !isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }

        if (this.sections.isEmpty()) {
            return this.sections.add(new Section(this, section.getUpStation(), section.getDownStation(), section.getDistance()));
        }

        if (isUpStationExisted) {
            this.getSections().stream()
                    .filter(it -> it.getUpStation().equals(section.getUpStation()))
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));

            return this.sections.add(new Section(this, section.getUpStation(), section.getDownStation(), section.getDistance()));
        }

        if (isDownStationExisted) {
            this.getSections().stream()
                    .filter(it -> it.getDownStation().equals(section.getDownStation()))
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));

            return this.sections.add(new Section(this, section.getUpStation(), section.getDownStation(), section.getDistance()));
        }

        throw new RuntimeException();
    }

    public List<Station> findStations() {
        if (this.isSectionEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();

        Station upStation = findUpTerminalStation();
        stations.add(upStation);

        Optional<Section> matchingStation = findSectionMatchingUpStation(upStation);
        Station finalDownStation;

        while (matchingStation.isPresent()) {
            finalDownStation = matchingStation.get().getDownStation();
            stations.add(finalDownStation);
            matchingStation = findSectionMatchingUpStation(finalDownStation);
        }

        return stations;
    }

    public Station findUpTerminalStation() {
        Station upStation = this.sections.get(0).getUpStation();

        Optional<Section> firstSection = Optional.of(this.sections.get(0));

        while (firstSection.isPresent()) {
            upStation = firstSection.get().getUpStation();
            firstSection = findSectionMatchingDownStation(upStation);
        }

        return upStation;
    }

    private Optional<Section> findSectionMatchingUpStation(Station finalUpStation) {
        return this.sections.stream()
                            .filter(section -> section.getUpStation().equals(finalUpStation))
                            .findFirst();
    }

    private Optional<Section> findSectionMatchingDownStation(Station nextSectionUpStation) {
        return this.sections.stream()
                            .filter(section -> section.getDownStation().equals(nextSectionUpStation))
                            .findFirst();
    }

    public boolean hasStation(Station station) {
        return this.sections.stream()
                                .anyMatch(section -> section.getUpStation().equals(station)
                                                    || section.getDownStation().equals(station));
    }

    public void deleteStation(Station station) {

        if (this.sectionCount() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> downSectionAtMatchingStation = this.sections.stream()
                                                                        .filter(it -> it.getUpStation().equals(station))
                                                                        .findFirst();

        Optional<Section> upSectionAtMatchingStation = this.sections.stream()
                                                                    .filter(it -> it.getDownStation().equals(station))
                                                                    .findFirst();

        if (downSectionAtMatchingStation.isPresent() && upSectionAtMatchingStation.isPresent()) {
            Station newUpStation = upSectionAtMatchingStation.get().getUpStation();
            Station newDownStation = downSectionAtMatchingStation.get().getDownStation();

            int newDistance = downSectionAtMatchingStation.get().getDistance() + upSectionAtMatchingStation.get().getDistance();

            this.sections.add(new Section(this, newUpStation, newDownStation, newDistance));
        }

        downSectionAtMatchingStation.ifPresent(this::removeSection);
        upSectionAtMatchingStation.ifPresent(this::removeSection);
    }
}
