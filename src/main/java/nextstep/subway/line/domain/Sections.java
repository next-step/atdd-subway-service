package nextstep.subway.line.domain;

import nextstep.subway.consts.ErrorMessage;
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

    protected Sections() {
    }

    protected Sections(Section section) {
        this.sections.add(section);
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
                    .filter(it -> it.getUpStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }
        return stations;
    }

    private Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
                    .filter(it -> it.getDownStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    public void addSection(Section newSection) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == newSection.getUpStation());
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == newSection.getDownStation());

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException(ErrorMessage.ERROR_SECTION_ALREADY_EXIST);
        }

        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == newSection.getUpStation()) &&
                stations.stream().noneMatch(it -> it == newSection.getDownStation())) {
            throw new RuntimeException(ErrorMessage.ERROR_SECTION_NOT_REGISTER);
        }

        if (stations.isEmpty()) {
            sections.add(newSection);
            return;
        }

        if (isUpStationExisted) {
            sections.stream()
                    .filter(it -> it.getUpStation() == newSection.getUpStation())
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(newSection.getDownStation(), newSection.getDistance()));

            sections.add(newSection);
        } else if (isDownStationExisted) {
            sections.stream()
                    .filter(it -> it.getDownStation() == newSection.getDownStation())
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(newSection.getUpStation(), newSection.getDistance()));

            sections.add(newSection);
        } else {
            throw new RuntimeException();
        }
    }

    public void removeStation(Station station) {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> upLineStation = sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
        Optional<Section> downLineStation = sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            sections.add(new Section(upLineStation.get().getLine(), newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }
}
