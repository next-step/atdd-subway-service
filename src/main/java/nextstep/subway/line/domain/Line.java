package nextstep.subway.line.domain;

import java.nio.file.ProviderNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

@Entity
public class Line extends BaseEntity {
    private static final String NO_EXIST = "등록할 수 없는 구간 입니다.";
    private static final String ALREADY_EXIST = "이미 등록된 구간 입니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {}

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

    private List<Station> getStations() {
        Section nextSection = findStartSection();
        List<Station> stations = new ArrayList<>();

        if (sections.isEmpty() || nextSection == null) {
            return Arrays.asList();
        }

        stations.add(nextSection.getUpStation());
        while (nextSection != null) {
            stations.add(nextSection.getDownStation());
            nextSection = findNextSection(nextSection);
        }

        return stations;

    }

    public List<StationResponse> getStationsResponse() {
        return getStations().stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());

    }

    private Section findNextSection(Section beforSection) {
        return sections.stream().filter(section -> section.isUpStationWithDown(beforSection))
            .findFirst()
            .orElse(null);
    }

    private Section findStartSection() {
        return sections.stream()
            .filter(section -> findSectionIsAnotherDownStation(section) == null)
            .findFirst()
            .orElse(null);
    }

    private Section findSectionIsAnotherDownStation(Section beforeSection) {
        return sections.stream()
            .filter(section -> section.isDownStationWithUp(beforeSection))
            .findFirst()
            .orElse(null);
    }

    public void addSection(Section section) {
        List<Station> statoins = getStations();
        validateAlreadyOrNoExist(section, statoins);
        updateIfUpStationMatch(section);
        updateIfDownStationMatch(section);
        sections.add(section);
    }

    private void updateIfDownStationMatch(Section compareSection) {
        sections.stream()
            .filter(compareSection::sameDownStation)
            .findFirst()
            .ifPresent(findedSection -> findedSection.updateDownStation(compareSection.getUpStation(),
                compareSection.getDistance()));
    }

    private void updateIfUpStationMatch(Section compareSection) {
        sections.stream()
            .filter(compareSection::sameUpStation)
            .findFirst()
            .ifPresent(findedSection -> findedSection.updateUpStation(compareSection.getDownStation(),
                compareSection.getDistance()));
    }

    private void validateAlreadyOrNoExist(Section compareSection, List<Station> statoins) {
        boolean hasUpStation = statoins.stream().anyMatch(station -> station.equals(compareSection.getUpStation()));
        boolean hasDownStation = statoins.stream().anyMatch(station -> station.equals(compareSection.getDownStation()));

        if (!hasUpStation && !hasDownStation) {
            throw new NoSuchElementException(NO_EXIST);
        }
        if (hasUpStation && hasDownStation) {
            throw new NoSuchElementException(ALREADY_EXIST);
        }
    }

    public void removeStation(Station station) {
        validationRemove();

        Optional<Section> afterSection = findAfterSection(station);
        Optional<Section> beforeSection = findBeforeSection(station);

        if (afterSection.isPresent() && beforeSection.isPresent()) {
            Station newUpStation = beforeSection.get().getUpStation();
            Station newDownStation = afterSection.get().getDownStation();
            int newDistance = afterSection.get().getDistance() + beforeSection.get().getDistance();
            this.sections.add(new Section(this, newUpStation, newDownStation, newDistance));
        }

        afterSection.ifPresent(it -> this.sections.remove(it));
        beforeSection.ifPresent(it -> this.sections.remove(it));
    }

    private Optional<Section> findBeforeSection(Station station) {
        return this.sections.stream()
            .filter(it -> it.getDownStation() == station)
            .findFirst();
    }

    private Optional<Section> findAfterSection(Station station) {
        return this.sections.stream()
            .filter(it -> it.getUpStation() == station)
            .findFirst();
    }

    private void validationRemove() {
        if (this.sections.size() <= 1) {
            throw new ProviderNotFoundException();
        }
    }

    public int getDistanceBetweenStations(Station upStation, Station downStation) {
        int distance = 0;
        Section nextSection = findByUpstation(upStation);
        if (nextSection == null) {
            return distance;
        }
        if (nextSection.getDownStation().equals(downStation)) {
            return nextSection.getDistance();
        }
        while (nextSection != null && !nextSection.getDownStation().equals(downStation)) {
            distance += nextSection.getDistance();
            nextSection = findNextSection(nextSection);
        }

        if (nextSection != null) {
            distance += nextSection.getDistance();
        }

        return distance;
    }

    private Section findByUpstation(Station upStation) {
        return sections.stream()
            .filter(section -> section.sameUpStation(upStation))
            .findFirst()
            .orElse(null);
    }

}
