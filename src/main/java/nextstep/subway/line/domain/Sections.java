package nextstep.subway.line.domain;

import nextstep.subway.constant.ErrorMessage;
import nextstep.subway.exception.BadRequestException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
        this.sections = new ArrayList<>();
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Section> toList() {
        return sections;
    }

    public void add(Section section) {
        sections.add(section);
    }

    private void validateAddLineStations(Stations stations, boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new BadRequestException(ErrorMessage.ALREADY_EXIST_SECTION);
        }

        if (!stations.isEmpty() && !isUpStationExisted && !isDownStationExisted) {
            throw new BadRequestException(ErrorMessage.CAN_NOT_ADD_SECTION);
        }

        if (!isUpStationExisted && !isDownStationExisted) {
            throw new BadRequestException(ErrorMessage.NOT_FOUND_SECTION_STATION);
        }
    }

    public void addLineStation(Line line, Section section) {
        Stations stations = new Stations(getStations());
        boolean isUpStationExisted = stations.isContainStations(section.getUpStation());
        boolean isDownStationExisted = stations.isContainStations(section.getDownStation());

        validateAddLineStations(stations, isUpStationExisted, isDownStationExisted);

        addSections(line, section, stations, isUpStationExisted, isDownStationExisted);
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        return findStations();
    }

    private List<Station> findStations() {
        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (Optional.ofNullable(downStation).isPresent()) {
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

    private void addSections(
            Line line,
            Section section,
            Stations stations,
            boolean isUpStationExisted,
            boolean isDownStationExisted
    ) {
        section.toLine(line);

        if (stations.isEmpty()) {
            addSection(section);
            return;
        }

        if (isUpStationExisted) {
            sections.stream()
                    .filter(it -> it.getUpStation() == section.getUpStation())
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(section));

            addSection(section);
        } else if (isDownStationExisted) {
            sections.stream()
                    .filter(it -> it.getDownStation() == section.getDownStation())
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(section));

            addSection(section);
        }
    }

    private void addSection(Section section) {
        sections.add(section);
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

    public void removeLineStation(Line line, Station station) {
        if (sections.size() <= 1) {
            throw new BadRequestException(ErrorMessage.EMPTY_SECTION);
        }

        Optional<Section> upLineStation = sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
        Optional<Section> downLineStation = sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance().toInt() + downLineStation.get().getDistance().toInt();

            Section section = new Section(line, newUpStation, newDownStation, new Distance(newDistance));
            addSection(section);
        }
    }

    public int size() {
        return sections.size();
    }

    public Section get(int i) {
        return sections.get(i);
    }
}
