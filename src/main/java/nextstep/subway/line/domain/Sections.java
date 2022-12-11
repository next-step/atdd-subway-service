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
    public static final int SECTION_DELETE_MIN_SIZE = 1;
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
            Optional<Section> nextLineStation = findUpNextLineStation(downStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }
        return stations;
    }

    private Optional<Section> findUpNextLineStation(Station downStation) {
        return sections.stream()
                .filter(it -> it.getUpStation() == downStation)
                .findFirst();
    }

    private Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Optional<Section> nextLineStation = findDownNextLineStation(downStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    private Optional<Section> findDownNextLineStation(Station downStation) {
        return sections.stream()
                .filter(it -> it.getDownStation() == downStation)
                .findFirst();
    }

    public void addSection(Section newSection) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == newSection.getUpStation());
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == newSection.getDownStation());

        validateSectionAlreadyExist(isUpStationExisted, isDownStationExisted);

        validateNewSectionRegister(stations, newSection);

        if (stations.isEmpty()) {
            sections.add(newSection);
            return;
        }

        if (isUpStationExisted) {
            addSectionUpStationExisted(newSection);
            return;
        }

        if (isDownStationExisted) {
            addSectionDownStationExisted(newSection);
            return;
        }
        throw new RuntimeException();

    }

        private void addSectionDownStationExisted(Section newSection) {
        sections.stream()
                .filter(it -> it.getDownStation() == newSection.getDownStation())
                .findFirst()
                .ifPresent(it -> it.updateDownStation(newSection.getUpStation(), newSection.getDistance()));

        sections.add(newSection);
    }

    private void addSectionUpStationExisted(Section newSection) {
        sections.stream()
                .filter(it -> it.getUpStation() == newSection.getUpStation())
                .findFirst()
                .ifPresent(it -> it.updateUpStation(newSection.getDownStation(), newSection.getDistance()));

        sections.add(newSection);

    }

    private void validateNewSectionRegister(List<Station> stations, Section newSection) {
        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == newSection.getUpStation()) &&
                stations.stream().noneMatch(it -> it == newSection.getDownStation())) {
            throw new RuntimeException(ErrorMessage.ERROR_SECTION_NOT_REGISTER);
        }
    }

    private void validateSectionAlreadyExist(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException(ErrorMessage.ERROR_SECTION_ALREADY_EXIST);
        }
    }

    public void removeStation(Station station) {
        validateSectionSize();

        Optional<Section> upLineStation = findSectionbyUpStation(station);
        Optional<Section> downLineStation = findSectionbyDownStation(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            sections.add(new Section(upLineStation.get().getLine(), newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    private void validateSectionSize() {
        if (sections.size() <= SECTION_DELETE_MIN_SIZE) {
            throw new IllegalArgumentException(ErrorMessage.ERROR_SECTION_DELETE_MIN_SIZE);
        }
    }

    private Optional<Section> findSectionbyUpStation(Station station) {
        return sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
    }

    private Optional<Section> findSectionbyDownStation(Station station) {
        return sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();
    }
}
