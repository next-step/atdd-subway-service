package nextstep.subway.line.domain;

import nextstep.subway.exception.SubwayExceptionMessage;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sectionList = new ArrayList<>();

    protected Sections() {

    }

    public List<Section> getSections() {
        return new ArrayList<>(sectionList);
    }

    public void add(Section section) {
        validateNewSection(section);

        List<Station> stations = getStations();
        if (stations.isEmpty()) {
            sectionList.add(section);
            return;
        }

        Section upLineSection = findUpLineStation(section.getUpStation());
        if (upLineSection != null) {
            upLineSection.updateUpStation(section.getDownStation(), section.getDistance());
        }

        Section downLineSection = findDownLineStation(section.getDownStation());
        if (downLineSection != null) {
            downLineSection.updateDownStation(section.getUpStation(), section.getDistance());
        }

        sectionList.add(section);
    }

    private void validateNewSection(Section section) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = hasStation(section.getUpStation());
        boolean isDownStationExisted = hasStation(section.getDownStation());

        if (isUpStationExisted && isDownStationExisted) {
            throw new IllegalArgumentException(SubwayExceptionMessage.DUPLICATE_SECTION.getMessage());
        }

        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == section.getUpStation()) &&
                stations.stream().noneMatch(it -> it == section.getDownStation())) {
            throw new IllegalArgumentException(SubwayExceptionMessage.INVALID_SECTION.getMessage());
        }
    }

    public void removeSectionByStation(Station station) {
        validateNotLastSection();

        Section upLineStation = findUpLineStation(station);
        Section downLineStation = findDownLineStation(station);

        if (upLineStation != null && downLineStation != null) {
            Station newUpStation = downLineStation.getUpStation();
            Station newDownStation = upLineStation.getDownStation();
            Line line = upLineStation.getLine();
            int newDistance = upLineStation.getDistance() + downLineStation.getDistance();
            sectionList.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        sectionList.remove(upLineStation);
        sectionList.remove(downLineStation);
    }

    private void validateNotLastSection() {
        if (sectionList.size() <= 1) {
            throw new IllegalStateException(SubwayExceptionMessage.CANNOT_DELETE_LAST_SECTION.getMessage());
        }
    }

    private Section findUpLineStation(Station upStation) {
        return sectionList.stream()
                .filter(it -> it.hasSameUpStation(upStation))
                .findFirst()
                .orElse(null);
    }

    private Section findDownLineStation(Station downStation) {
        return sectionList.stream()
                .filter(it -> it.hasSameDownStation(downStation))
                .findFirst()
                .orElse(null);
    }

    public List<Station> getStations() {
        if (sectionList.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (hasNextSection(downStation)) {
            downStation = getNextSection(downStation).getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Section getNextSection(Station downStation) {
        return sectionList.stream()
                .filter(it -> it.getUpStation().equals(downStation))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(SubwayExceptionMessage.EMPTY_NEXT_SECTION.getMessage()));
    }

    private boolean hasNextSection(Station downStation) {
        return sectionList.stream()
                .anyMatch(it -> it.getUpStation().equals(downStation));
    }

    private boolean hasStation(Station station) {
        return getStations().stream().anyMatch(it -> it == station);
    }

    private Station findUpStation() {
        Station downStation = sectionList.get(0).getUpStation();

        while (hasPreviousSection(downStation)) {
            Section previousSection = getPreviousSection(downStation);
            downStation = previousSection.getUpStation();
        }

        return downStation;
    }

    private Section getPreviousSection(Station upStation) {
        return sectionList.stream()
                .filter(it -> it.getDownStation().equals(upStation))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(SubwayExceptionMessage.EMPTY_PREVIOUS_SECTION.getMessage()));
    }

    private boolean hasPreviousSection(Station upStation) {
        return sectionList.stream()
                .anyMatch(it -> it.getDownStation().equals(upStation));
    }

}
