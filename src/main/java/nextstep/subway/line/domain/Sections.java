package nextstep.subway.line.domain;

import nextstep.subway.enums.ErrorMessage;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        sections.add(section);
    }

    public List<Section> getSections() {
        return this.sections;
    }

    public List<Station> getStations() {
        if (isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = this.getSections().stream()
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

    private boolean isEmpty() {
        return this.sections.isEmpty();
    }

    private Station findUpStation() {
        Station downStation = this.sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = this.getSections().stream()
                    .filter(it -> it.getDownStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    public boolean isStationExisted(Station station) {
        return this.getStations().stream().anyMatch(it -> it == station);
    }

    public boolean isStationNotExisted(Station station) {
        return this.getStations().stream().noneMatch(it -> it == station);
    }

    public void addSections(Section section) {
        isValidDuplicate(section);
        isaValidNotExist(section);

        updateUpStation(section);
        updateDownStation(section);

        this.sections.add(section);
    }

    private void updateUpStation(Section section) {
        if (isStationExisted(section.getUpStation())) {
            this.getSections().stream()
                    .filter(it -> it.getUpStation() == section.getUpStation())
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
        }
    }

    private void updateDownStation(Section section) {
        if (isStationExisted(section.getUpStation())) {
            this.getSections().stream()
                    .filter(it -> it.getDownStation() == section.getDownStation())
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
        }
    }

    private void isaValidNotExist(Section section) {
        if (!this.sections.isEmpty()
                && isStationNotExisted(section.getUpStation())
                && isStationNotExisted(section.getDownStation())) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_SECTION.getMessage());
        }

    }

    private void isValidDuplicate(Section section) {
        if (isStationExisted(section.getUpStation()) && isStationExisted(section.getDownStation())) {
            throw new IllegalArgumentException(ErrorMessage.DUPLCATED_SECTION.getMessage());
        }
    }


    public void removeLineStation(Line line, Station station) {
        isValidSectionsSize();

        Optional<Section> upLineStation = getFirstUpStation(station);
        Optional<Section> downLineStation = getFirstDownStation(sections, station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            this.sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    private Optional<Section> getFirstDownStation(List<Section> sections, Station station) {
        return sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();
    }

    private Optional<Section> getFirstUpStation(Station station) {
        return sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
    }

    private void isValidSectionsSize() {
        if (sections.size() <= 1) {
            throw new IllegalArgumentException(ErrorMessage.EMPTY_STATIONS.getMessage());
        }
    }
}
