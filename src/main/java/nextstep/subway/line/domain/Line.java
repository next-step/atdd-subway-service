package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.exception.DeleteOnlySectionException;
import nextstep.subway.line.exception.DuplicatedSectionException;
import nextstep.subway.line.exception.NoneMatchSectionException;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Line extends BaseEntity {
    public static final int DIFFERENCE_SECTIONS_STATIONS_SIZE = 1;
    public static final int CANNOT_DELETE_SIZE = 1;

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

    public void removeLineStation(Station station) {
        validateOnlySection();
        Optional<Section> sameUpStation = sections.stream()
                .filter(section -> section.getUpStation()
                        .equals(station))
                .findFirst();
        Optional<Section> sameDownStation = sections.stream()
                .filter(section -> section.getDownStation()
                        .equals(station))
                .findFirst();
        if (removeMiddleStation(sameUpStation, sameDownStation)) return;
        removeEndStation(sameUpStation, sameDownStation);
    }

    private void validateOnlySection() {
        if (sections.size() == CANNOT_DELETE_SIZE) {
            throw new DeleteOnlySectionException();
        }
    }

    private void removeEndStation(Optional<Section> sameUpStation, Optional<Section> sameDownStation) {
        if (!sameUpStation.isPresent()) {
            sections.remove(sameDownStation.get());
            return;
        }
        sections.remove(sameUpStation.get());
    }

    private boolean removeMiddleStation(Optional<Section> sameUpStation, Optional<Section> sameDownStation) {
        if (sameUpStation.isPresent() && sameDownStation.isPresent()) {
            sections.remove(sameUpStation.get());
            sections.remove(sameDownStation.get());
            Section newSection = Section.mergeSections(sameUpStation.get(), sameDownStation.get());
            sections.add(newSection);
            newSection.setSameLine(sameUpStation);
            return true;
        }
        return false;
    }

    public void addSection(Section section) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == section.getUpStation());
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == section.getDownStation());

        validateDuplicatedSectionException(isUpStationExisted, isDownStationExisted);
        validateNoneMatchSectionException(section, stations);
        updateUpStationExisted(section, isUpStationExisted);
        updateDownStationExisted(section, isDownStationExisted);
        sections.add(section);
    }

    private void updateDownStationExisted(Section section, boolean isDownStationExisted) {
        if (isDownStationExisted) {
            sections.stream()
                    .filter(it -> it.getDownStation() == section.getDownStation())
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
        }
    }

    private void updateUpStationExisted(Section section, boolean isUpStationExisted) {
        if (isUpStationExisted) {
            sections.stream()
                    .filter(it -> it.getUpStation() == section.getUpStation())
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));

        }
    }

    private void validateNoneMatchSectionException(Section section, List<Station> stations) {
        if (!stations.isEmpty() && stations.stream()
                .noneMatch(it -> it == section.getUpStation()) &&
                stations.stream()
                        .noneMatch(it -> it == section.getDownStation())) {
            throw new NoneMatchSectionException("등록할 수 없는 구간 입니다.");
        }
    }

    private void validateDuplicatedSectionException(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new DuplicatedSectionException("이미 등록된 구간 입니다.");
        }
    }

}
