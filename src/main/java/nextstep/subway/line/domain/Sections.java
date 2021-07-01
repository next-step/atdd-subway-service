package nextstep.subway.line.domain;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.error.CustomException;
import nextstep.subway.error.ErrorMessage;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

@Embeddable
public class Sections {
    public static final int SECTION_DELETABLE_MIN_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        sections.add(section);
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<StationResponse> stationResponse() {
        return getStations().toResponse();
    }

    public void addSection(Section newSection) {
        Stations stations = getStations();
        boolean isUpStationExisted = isExistUpStation(newSection);
        boolean isDownStationExisted = isExistDownStation(newSection);

        checkValidStations(isUpStationExisted, isDownStationExisted);

        if (stations.isEmpty()) {
            add(newSection);
            return;
        }
        if (isUpStationExisted) {
            updateUpStation(newSection);
            add(newSection);
            return;
        }
        if (isDownStationExisted) {
            updateDownStation(newSection);
            add(newSection);
            return;
        }
        throw new RuntimeException();
    }

    private void updateUpStation(Section newSection) {
        sections.stream()
                .filter(section -> section.isUpStation(newSection))
                .findFirst()
                .ifPresent(section -> section.connectUpStation(newSection));
    }

    private void updateDownStation(Section newSection) {
        sections.stream()
                .filter(section -> section.isDownStation(newSection))
                .findFirst()
                .ifPresent(section -> section.connectDownStation(newSection));
    }

    public Stations getStations() {
        if (sections.isEmpty()) {
            return new Stations();
        }

        Stations stations = new Stations();
        Station firstStation = findFirstStation();
        stations.add(firstStation);

        while (hasSectionByUpStation(firstStation)) {
            Station tempStation = firstStation;
            Section nextLineSection = findSectionByUpStation(tempStation)
                    .orElseThrow(() -> new CustomException(ErrorMessage.NOT_FOUND_SECTION));
            firstStation = nextLineSection.getDownStation();
            stations.add(firstStation);
        }
        return stations;
    }

    public void removeStation(Line line, Station station) {
        checkDeletable();

        Optional<Section> upSection = findSectionByUpStation(station);
        Optional<Section> downSection = findSectionByDownStation(station);

        if (upSection.isPresent() && downSection.isPresent()) {
            mergeSection(line, upSection.get(), downSection.get());
        }

        upSection.ifPresent(it -> sections.remove(it));
        downSection.ifPresent(it -> sections.remove(it));
    }

    public int size() {
        return sections.size();
    }

    private void mergeSection(Line line, Section upSection, Section downSection) {
        Station newUpStation = downSection.getUpStation();
        Station newDownStation = upSection.getDownStation();
        Distance newDistance = upSection.addDistance(downSection);
        sections.add(new Section(line, newUpStation, newDownStation, newDistance));
    }

    private Station findFirstStation() {
        Station firstStation = sections.get(0).getUpStation();
        while (hasSectionByDownStation(firstStation)) {
            Station tempStation = firstStation;
            Section nextLineSection = findSectionByDownStation(tempStation)
                    .orElseThrow(() -> new CustomException(ErrorMessage.NOT_FOUND_SECTION));
            firstStation = nextLineSection.getUpStation();
        }
        return firstStation;
    }

    private void checkValidStations(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new CustomException(ErrorMessage.SECTION_IS_ALREADY_ADD);
        }

        if (!sections.isEmpty() && !isUpStationExisted && !isDownStationExisted) {
            throw new CustomException(ErrorMessage.CANT_ADD_THIS_SECTION);
        }
    }

    private boolean hasSectionByDownStation(Station station) {
        return sections.stream()
                .anyMatch(section -> section.isDownStation(station));
    }

    private boolean hasSectionByUpStation(Station station) {
        return sections.stream()
                .anyMatch(section -> section.isUpStation(station));
    }

    private Optional<Section> findSectionByUpStation(Station station) {
        return sections.stream()
                .filter(section -> section.isUpStation(station))
                .findFirst();
    }

    private void checkDeletable() {
        if (sections.size() <= SECTION_DELETABLE_MIN_SIZE) {
            throw new CustomException(ErrorMessage.SECTIONS_HAVE_ONLY_ONE);
        }
    }

    private boolean isExistUpStation(Section targetSection) {
        return sections.stream()
                .anyMatch(section -> section.isExistsUpStation(targetSection));
    }

    private boolean isExistDownStation(Section targetSection) {
        return sections.stream()
                .anyMatch(section -> section.isExistsDownStation(targetSection));
    }

    private Optional<Section> findSectionByDownStation(Station station) {
        return sections.stream()
                .filter(section -> section.isDownStation(station))
                .findFirst();
    }
}
