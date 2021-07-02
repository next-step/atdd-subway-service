package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.exception.DuplicateSectionException;
import nextstep.subway.exception.NotMatchStationException;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
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

    public void validateAndAddSection(SectionRequest request, Station upStation, Station downStation) {
        List<Station> stations = extractStations();

        boolean isUpStationExisted = isStationExisted(stations, upStation);
        boolean isDownStationExisted = isStationExisted(stations, downStation);

        validateDuplicateStations(isUpStationExisted, isDownStationExisted);
        validateNonMatchStations(upStation, downStation, stations);

        if (stations.isEmpty()) {
            addSection(new Section(this, upStation, downStation, request.getDistance()));
            return;
        }

        if (!isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException();
        }

        if (isUpStationExisted) {
            updatedUpToDownStation(request, upStation, downStation);
        }

        updatedDownToUpStation(request, upStation, downStation);

        addSection(new Section(this, upStation, downStation, request.getDistance()));
    }

    private boolean isStationExisted(List<Station> stations, Station upStation) {
        return stations.stream().anyMatch(it -> it == upStation);
    }

    private void validateNonMatchStations(Station upStation, Station downStation, List<Station> stations) {
        if (!stations.isEmpty()
                && haveStation(upStation, stations)
                && haveStation(downStation, stations)) {
            throw new NotMatchStationException();
        }
    }

    private boolean haveStation(Station station, List<Station> stations) {
        return stations.stream().noneMatch(it -> it == station);
    }

    private void validateDuplicateStations(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new DuplicateSectionException();
        }
    }

    private void updatedUpToDownStation(SectionRequest request, Station upStation, Station downStation) {
        sections.updateUpToDownStationWhenExist(request, upStation, downStation);
    }

    private void updatedDownToUpStation(SectionRequest request, Station upStation, Station downStation) {
        sections.updateDownToUpStationWhenExist(request, upStation, downStation);
    }

    public List<StationResponse> extractStationToResponse() {
        return extractStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    List<Station> extractStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.findNextSectionByUpStation(finalDownStation);
            if (nextLineStation.isEmpty()) {
                break;
            }

            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Station findUpStation() {
        Station firstUpStation = sections.getFirstUpStation();

        while (firstUpStation != null) {
            Station finalDownStation = firstUpStation;

            Optional<Section> nextLineStation = sections.findNextSectionByDownStation(finalDownStation);
            if (nextLineStation.isEmpty()) {
                break;
            }

            firstUpStation = nextLineStation.get().getUpStation();
        }

        return firstUpStation;
    }

    public void validateAndRemoveByStation(Station station) {
        sections.validateRemovalSectionsSize();
        sections.removeByStation(station, this);
    }

    void addSection(Section section) {
        sections.add(section);
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
        return sections.getSections();
    }
}
