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

    }

    public List<Section> getSections() {
        return sections;
    }

    public void add(Section section) {
        Station requestUpStation = section.getUpStation();
        Station requestDownStation = section.getDownStation();

        checkValidSection(requestUpStation, requestDownStation);

        if (getStations().isEmpty()) {
            this.sections.add(section);
            return;
        }

        if (isStationExisted(requestUpStation)) {
            sections.stream()
                    .filter(it -> it.getUpStation() == requestUpStation)
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(requestDownStation, section.getDistance()));
            this.sections.add(section);
            return;
        }

        if (isStationExisted(requestDownStation)) {
            sections.stream()
                    .filter(it -> it.getDownStation() == requestDownStation)
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(requestUpStation, section.getDistance()));
            this.sections.add(section);
            return;
        }

        throw new RuntimeException();
    }

    public void checkValidSection(Station requestUpStation, Station requestDownStation) {
        boolean isUpStationExisted = isStationExisted(requestUpStation);
        boolean isDownStationExisted = isStationExisted(requestDownStation);

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException(ErrorMessage.ALREADY_EXIST_SECTION.getMessage());
        }

        if (!getStations().isEmpty() && !isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException(ErrorMessage.NO_EXIST_STATIONS_TO_REGISTER.getMessage());
        }
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

    public Station findUpStation() {
        List<Station> upStations = sections.stream().map(Section::getUpStation).collect(Collectors.toList());
        List<Station> downStations = sections.stream().map(Section::getDownStation).collect(Collectors.toList());
        for(Station downStation : downStations) {
            upStations.remove(downStation);
        }
        return upStations.get(0);
    }

    public boolean isStationExisted(Station station) {
        return getStations().contains(station);
    }

    public void removeSectionByStation(Station station) {
        if(!isStationExisted(station)) {
            throw new RuntimeException(ErrorMessage.NO_EXIST_STATIONS_TO_DELETE.getMessage());
        }

        if (sections.size() <= 1) {
            throw new RuntimeException(ErrorMessage.DO_NOT_DELETE_UNIQUE_SECTION.getMessage());
        }

        Optional<Section> upLineStation = sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
        Optional<Section> downLineStation = sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            sections.add(new Section(upLineStation.get().getLine(),
                    downLineStation.get().getUpStation(),
                    upLineStation.get().getDownStation(),
                    upLineStation.get().getDistance().add(downLineStation.get().getDistance())));
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }
}
