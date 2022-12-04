package nextstep.subway.line.domain;

import nextstep.subway.ErrorMessage;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    protected Sections() {
        this.sections = new ArrayList<>();
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
        List<Station> upStations = sections.stream().map(Section::getUpStation).collect(Collectors.toList());
        List<Station> downStations = sections.stream().map(Section::getDownStation).collect(Collectors.toList());
        for(Station downStation : downStations) {
            upStations.remove(downStation);
        }
        return upStations.get(0);
    }

    public void add(Section section) {
        checkValidSection(section.getUpStation(), section.getDownStation());
        addStation(section);
    }

    private void addStation(Section section) {
        if (getStations().isEmpty()) {
            initSection(section);
            return;
        }

        if (isStationExisted(section.getUpStation())) {
            updateLowerSection(section);
            return;
        }

        if (isStationExisted(section.getDownStation())) {
            updateUpperSection(section);
            return;
        }

        throw new RuntimeException(ErrorMessage.UNKNOWN_ERROR.getMessage());
    }

    private void checkValidSection(Station requestUpStation, Station requestDownStation) {
        boolean isUpStationExisted = isStationExisted(requestUpStation);
        boolean isDownStationExisted = isStationExisted(requestDownStation);

        if (isUpStationExisted && isDownStationExisted) {
            throw new IllegalArgumentException(ErrorMessage.ALREADY_EXIST_SECTION.getMessage());
        }

        if (!getStations().isEmpty() && !isUpStationExisted && !isDownStationExisted) {
            throw new IllegalArgumentException(ErrorMessage.NO_EXIST_STATIONS_TO_REGISTER.getMessage());
        }
    }

    private boolean isStationExisted(Station station) {
        return getStations().contains(station);
    }

    private void initSection(Section section) {
        this.sections.add(section);
    }

    private void updateLowerSection(Section section) {
        sections.stream()
                .filter(it -> it.getUpStation() == section.getUpStation())
                .findFirst()
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
        this.sections.add(section);
    }

    private void updateUpperSection(Section section) {
        sections.stream()
                .filter(it -> it.getDownStation() == section.getDownStation())
                .findFirst()
                .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
        this.sections.add(section);
    }

    public void removeSectionByStation(Station station) {
        checkRemovableStation(station);
        removeStation(station);
    }

    private void checkRemovableStation(Station station) {
        if(!isStationExisted(station)) {
            throw new IllegalArgumentException(ErrorMessage.NO_EXIST_STATIONS_TO_DELETE.getMessage());
        }

        if (sections.size() <= 1) {
            throw new IllegalArgumentException(ErrorMessage.DO_NOT_DELETE_UNIQUE_SECTION.getMessage());
        }
    }

    private void removeStation(Station station) {
        Optional<Section> upperSection = findOptionalUpperSection(station);
        Optional<Section> lowerSection = findOptionalLowerSection(station);

        if (upperSection.isPresent() && lowerSection.isPresent()) {
            sections.add(new Section(upperSection.get().getLine(),
                    lowerSection.get().getUpStation(),
                    upperSection.get().getDownStation(),
                    upperSection.get().getDistance().add(lowerSection.get().getDistance())));
        }

        upperSection.ifPresent(it -> sections.remove(it));
        lowerSection.ifPresent(it -> sections.remove(it));
    }

    private Optional<Section> findOptionalUpperSection(Station station) {
        return sections.stream()
                .filter(section -> section.getUpStation() == station)
                .findFirst();
    }

    private Optional<Section> findOptionalLowerSection(Station station) {
        return sections.stream()
                .filter(section -> section.getDownStation() == station)
                .findFirst();
    }

}
