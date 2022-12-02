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

        List<Station> stations = getStations();
        checkValidSection(requestUpStation, requestDownStation);

        if (stations.isEmpty()) {
            this.sections.add(section);
            return;
        }

        if (isStationExisted(requestUpStation)) {
            sections.stream()
                    .filter(it -> it.getUpStation() == requestUpStation)
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(requestDownStation, section.getTempDistance()));
            this.sections.add(section);
            return;
        }

        if (isStationExisted(requestDownStation)) {
            sections.stream()
                    .filter(it -> it.getDownStation() == requestDownStation)
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(requestUpStation, section.getTempDistance()));
            this.sections.add(section);
            return;
        }

        throw new RuntimeException();
    }

    public void checkValidSection(Station requestUpStation, Station requestDownStation) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = isStationExisted(requestUpStation);
        boolean isDownStationExisted = isStationExisted(requestDownStation);

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException(ErrorMessage.ALREADY_EXIST_SECTION.getMessage());
        }

        if (!stations.isEmpty() && !isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException(ErrorMessage.NO_EXIST_STATIONS.getMessage());
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
}
