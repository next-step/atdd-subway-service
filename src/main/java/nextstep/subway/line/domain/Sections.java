package nextstep.subway.line.domain;

import nextstep.subway.common.ErrorCode;
import nextstep.subway.exception.NotAcceptableApiException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public List<Section> getSections() {
        return sections;
    }

    public boolean add(Section section) {
        if (sections.isEmpty()) {
            return sections.add(section);
        }
        return insertBetweenSection(section);
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public boolean hasNextSectionByUpStation(Station station) {
        return sections.stream()
                .anyMatch(section -> section.isEqualUpStation(station));
    }

    public boolean hasNextSectionByDownStation(Station station) {
        return sections.stream()
                .anyMatch(section -> section.isEqualDownStation(station));
    }

    public boolean hasDeletableSection() {
        return sections.size() > 1;
    }

    public Section getNextSectionByUpStation(Station station) {
        return sections.stream()
                .filter(section -> section.isEqualUpStation(station))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format("다음 구간이 없습니다. (sectionId: %d)", station.getId())));
    }

    public Section getNextSectionByDownStation(Station station) {
        return sections.stream()
                .filter(section -> section.isEqualDownStation(station))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format("다음 구간이 없습니다. (sectionId: %d)", station.getId())));
    }

    public Section getOldSectionByUpStation(Station station) {
        return sections.stream()
                .filter(section -> section.isEqualUpStation(station))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format("상행역이 일치하는 기존 구간이 없습니다. (upStationId: %d)", station.getId())));
    }

    public Section getOldSectionByDownStation(Station station) {
        return sections.stream()
                .filter(section -> section.isEqualDownStation(station))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format("하행역이 일치하는 기존 구간이 없습니다. (downStationId: %d)", station.getId())));
    }

    private Stations getStations() {
        Stations stations = new Stations();
        for (Section section : sections) {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        }
        return stations;
    }

    private boolean insertBetweenSection(Section section) {
        validate(section);
        return isUpdateUpStation(section) || isUpdateDownStation(section);
    }

    private void validate(Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        Stations stations = getStations();

        boolean isUpStationExisted = stations.contains(upStation);
        boolean isDownStationExisted = stations.contains(downStation);

        if (isUpStationExisted && isDownStationExisted) {
            throw new NotAcceptableApiException(ErrorCode.DUPLICATE_SECTION);
        }
        if (stations.isNotEmpty() && stations.notContains(upStation) && stations.notContains(downStation)) {
            throw new NotAcceptableApiException(ErrorCode.CAN_NOT_ADD_SECTION);
        }
    }

    private boolean isUpdateUpStation(Section section) {
        if (getStations().contains(section.getUpStation())) {
            updateUpStation(section);
            return sections.add(section);
        }
        return false;
    }

    private boolean isUpdateDownStation(Section section) {
        if (getStations().contains(section.getDownStation())) {
            updateDownStation(section);
            return sections.add(section);
        }
        return false;
    }

    private void updateUpStation(Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        int distance = section.getDistance();

        sections.stream()
                .filter(it -> it.getUpStation().equals(upStation))
                .findFirst()
                .ifPresent(it -> it.updateUpStation(downStation, distance));
    }

    private void updateDownStation(Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        int distance = section.getDistance();

        sections.stream()
                .filter(it -> it.getDownStation().equals(downStation))
                .findFirst()
                .ifPresent(it -> it.updateDownStation(upStation, distance));
    }
}
