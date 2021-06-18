package nextstep.subway.line.domain;

import nextstep.subway.line.ui.*;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public List<Section> sections() {
        return sections;
    }

    public void addSection(Section section) {
        List<Station> stations = stations();
        Station upStation = section.upStation();
        Station downStation = section.downStation();

        if (stations.isEmpty()) {
            sections.add(section);
            return;
        }

        boolean isUpStationExisted = isUpStationExisted(stations, upStation);
        boolean isDownStationExisted = isDownStationExisted(stations, downStation);

        validateTwoStationAlreadyExist(isUpStationExisted, isDownStationExisted);
        validateTwoStationNotExist(stations, upStation, downStation);

        if (isUpStationExisted) {
            changeStationInfoWhenUpStationMatch(section);
        }

        if (isDownStationExisted) {
            changeStationInfoWhenDownStationMatch(section);
        }
    }

    private void changeStationInfoWhenDownStationMatch(Section section) {
        sections.stream()
                .filter(originalSection -> originalSection.hasSameDownStation(section.downStation()))
                .findFirst()
                .ifPresent(originalSection -> originalSection.updateDownStation(section.upStation(), section.getDistance()));

        sections.add(section);
    }

    private void changeStationInfoWhenUpStationMatch(Section section) {
        sections.stream()
                .filter(originalSection -> originalSection.hasSameUpStation(section.upStation()))
                .findFirst()
                .ifPresent(originalSection -> originalSection.updateUpStation(section.downStation(), section.getDistance()));

        sections.add(section);
    }

    private void validateTwoStationNotExist(List<Station> stations, Station upStation, Station downStation) {
        if (!stations.isEmpty() && stations.stream().noneMatch(station -> station.isSameStation(upStation)) &&
                stations.stream().noneMatch(station -> station.isSameStation(downStation))) {
            throw new TwoStationNotExistException();
        }
    }

    private void validateTwoStationAlreadyExist(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new TwoStationAlreadyExistException();
        }
    }

    private boolean isDownStationExisted(List<Station> stations, Station downStation) {
        return stations.stream().anyMatch(station -> station.isSameStation(downStation));
    }

    private boolean isUpStationExisted(List<Station> stations, Station upStation) {
        return stations.stream().anyMatch(station -> station.isSameStation(upStation));
    }

    public List<Station> stations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        addNextStation(stations, downStation);

        return stations;
    }

    private void addNextStation(List<Station> stations, Station downStation) {
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
                    .filter(originalSection -> originalSection.hasSameUpStation(finalDownStation))
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().downStation();
            stations.add(downStation);
        }
    }

    private Station findUpStation() {
        Station downStation = sections.get(0).upStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
                    .filter(originalSection -> originalSection.hasSameDownStation(finalDownStation))
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().upStation();
        }

        return downStation;
    }


    public void removeSection(Station station) {
        validateSectionSize();

        Optional<Section> upLineStation = sections.stream()
                .filter(originalSection -> originalSection.hasSameUpStation(station))
                .findFirst();
        Optional<Section> downLineStation = sections.stream()
                .filter(originalSection -> originalSection.hasSameDownStation(station))
                .findFirst();

        validateNoStationInLine(upLineStation, downLineStation);

        addNewSection(upLineStation, downLineStation);

        removeEndStation(upLineStation, downLineStation);
    }

    private void addNewSection(Optional<Section> upLineStation, Optional<Section> downLineStation) {
        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            sections.remove(upLineStation.get());
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            downLineStation.get().changeDownStationWhenRemove(upLineStation.get().downStation(), newDistance);
        }
    }

    private void removeEndStation(Optional<Section> upLineStation, Optional<Section> downLineStation) {
        if (!(upLineStation.isPresent() && downLineStation.isPresent())) {
            upLineStation.ifPresent(originalSection -> sections.remove(originalSection));
            downLineStation.ifPresent(originalSection -> sections.remove(originalSection));
        }
    }

    private void validateNoStationInLine(Optional<Section> upLineStation, Optional<Section> downLineStation) {
        if (!upLineStation.isPresent() && !downLineStation.isPresent()) {
            throw new NoStationInLineException();
        }
    }

    private void validateSectionSize() {
        if (sections.size() <= 1) {
            throw new CannotDeleteOnlySectionException();
        }
    }
}
