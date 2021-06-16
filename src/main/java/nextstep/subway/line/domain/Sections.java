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
                .filter(it -> it.downStation() == section.downStation())
                .findFirst()
                .ifPresent(it -> it.updateDownStation(section.upStation(), section.getDistance()));

        sections.add(section);
    }

    private void changeStationInfoWhenUpStationMatch(Section section) {
        sections.stream()
                .filter(it -> it.upStation() == section.upStation())
                .findFirst()
                .ifPresent(it -> it.updateUpStation(section.downStation(), section.getDistance()));

        sections.add(section);
    }

    private void validateTwoStationNotExist(List<Station> stations, Station upStation, Station downStation) {
        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == upStation) &&
                stations.stream().noneMatch(it -> it == downStation)) {
            throw new TwoStationNotExistException();
        }
    }

    private void validateTwoStationAlreadyExist(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new TwoStationAlreadyExistException();
        }
    }

    private boolean isDownStationExisted(List<Station> stations, Station downStation) {
        return stations.stream().anyMatch(it -> it == downStation);
    }

    private boolean isUpStationExisted(List<Station> stations, Station upStation) {
        return stations.stream().anyMatch(it -> it == upStation);
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
                    .filter(it -> it.upStation() == finalDownStation)
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
                    .filter(it -> it.downStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().upStation();
        }

        return downStation;
    }


    public void removeSection(Line line, Station station) {
        validateSectionSize();

        Optional<Section> upLineStation = sections.stream()
                .filter(it -> it.upStation() == station)
                .findFirst();
        Optional<Section> downLineStation = sections.stream()
                .filter(it -> it.downStation() == station)
                .findFirst();

        validateNoStationInLine(upLineStation, downLineStation);

        addNewSection(line, upLineStation, downLineStation);

        upLineStation.ifPresent(it -> line.getSections().remove(it));
        downLineStation.ifPresent(it -> line.getSections().remove(it));
    }

    private void addNewSection(Line line, Optional<Section> upLineStation, Optional<Section> downLineStation) {
        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().upStation();
            Station newDownStation = upLineStation.get().downStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            sections.add(new Section(line, newUpStation, newDownStation, newDistance));
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
