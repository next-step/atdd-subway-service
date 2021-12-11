package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.common.ErrorCode;
import nextstep.subway.exception.NotAcceptableApiException;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Arrays;
import java.util.NoSuchElementException;

@Entity
public class Line extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String color;

    @Embedded
    private final Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        sections.add(Section.of(this, upStation, downStation, distance));
    }

    public static Line of(String name, String color, Station upStation, Station downStation, int distance) {
        return new Line(name, color, upStation, downStation, distance);
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public LineResponse toResponse() {
        return LineResponse.of(this);
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

    public Sections getSections() {
        return sections;
    }

    public Stations getStations() {
        Stations stations = new Stations();
        if (sections.isEmpty()) {
            return stations;
        }

        Station station = findUpStation();
        stations.add(station);

        while (sections.hasSectionByUpStation(station)) {
            Section nextSection = sections.getSectionByUpStation(station);
            station = nextSection.getDownStation();
            stations.add(station);
        }
        return stations;
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        Section newSection = Section.of(this, upStation, downStation, distance);
        getSections().add(newSection);
    }

    public void deleteSection(Station station) {
        validateDeleteStation(station);

        if (isFirstStation(station)) {
            deleteFirstStation(station);
            return;
        }

        if (isLastStation(station)) {
            deleteLastStation(station);
            return;
        }

        deleteBetweenStation(station);
    }

    private boolean isFirstStation(Station station) {
        Station upStation = findUpStation();
        return station.equals(upStation);
    }

    private boolean isLastStation(Station station) {
        Station downStation = findDownStation();
        return station.equals(downStation);
    }

    private Station findUpStation() {
        Station station = getFirstSection().getUpStation();

        while (sections.hasSectionByDownStation(station)) {
            Section nextSection = sections.getSectionByDownStation(station);
            station = nextSection.getUpStation();
        }
        return station;
    }

    private Station findDownStation() {
        Station station = getFirstSection().getDownStation();

        while (sections.hasSectionByUpStation(station)) {
            Section nextSection = sections.getSectionByUpStation(station);
            station = nextSection.getDownStation();
        }
        return station;
    }

    private Section getFirstSection() {
        if (sections.isEmpty()) {
            throw new NoSuchElementException("구간 목록이 비어있습니다.");
        }
        return sections.getSections().get(0);
    }

    private void validateDeleteStation(Station station) {
        if (!getSections().hasDeletableSection()) {
            throw new NotAcceptableApiException(ErrorCode.CAN_NOT_REMOVE_SECTION);
        }
        if (getStations().notContains(station)) {
            throw new NotAcceptableApiException(ErrorCode.NOT_REGISTERED_STATION_TO_LINE);
        }
    }

    private void deleteFirstStation(Station station) {
        sections.getSections().stream()
                .filter(section -> section.isEqualUpStation(station))
                .findFirst()
                .ifPresent(findSection -> sections.getSections().remove(findSection));
    }

    private void deleteLastStation(Station station) {
        sections.getSections().stream()
                .filter(section -> section.isEqualDownStation(station))
                .findFirst()
                .ifPresent(findSection -> sections.getSections().remove(findSection));
    }

    private void deleteBetweenStation(Station station) {
        Section oldSection = sections.getSectionByDownStation(station);
        Section nextOldSection = sections.getSectionByUpStation(station);

        Station newUpStation = oldSection.getUpStation();
        Station newDownStation = nextOldSection.getDownStation();
        int newDistance = oldSection.getDistance() + nextOldSection.getDistance();
        Section newSection = Section.of(this, newUpStation, newDownStation, newDistance);

        sections.getSections().removeAll(Arrays.asList(oldSection, nextOldSection));
        sections.add(newSection);
    }
}
