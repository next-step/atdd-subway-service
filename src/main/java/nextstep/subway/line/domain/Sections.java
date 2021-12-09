package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.common.exception.SubwayErrorCode;
import nextstep.subway.common.exception.SubwayException;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    private Sections(List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    protected Sections() {
    }

    public static Sections empty() {
        return new Sections();
    }

    public static Sections from(List<Section> sections) {
        return new Sections(sections);
    }

    public boolean addSection(Section section) {
        return this.sections.add(section);
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getStations() {
        Stations stations = makeStations();

        return stations.getStations();
    }

    private Stations makeStations() {
        if (sections.isEmpty()) {
            return new Stations();
        }

        Station station = sections.get(0).getUpStation();

        Station upStation = findUpStation(station);

        List<Station> stations = makeStations(upStation);
        return new Stations(stations);
    }

    private Station findUpStation(Station downStation) {
        Optional<Section> nextLineStation = findDownLineStation(downStation);
        if (!nextLineStation.isPresent()) {
            return downStation;
        }
        return findUpStation(nextLineStation.get().getUpStation());
    }

    private List<Station> makeStations(Station downStation) {
        List<Station> stations = new ArrayList<>();
        stations.add(downStation);

        Optional<Section> nextLineStation = findUpLineStation(downStation);
        if (!nextLineStation.isPresent()) {
            return stations;
        }

        stations.addAll(makeStations(nextLineStation.get().getDownStation()));
        return stations;
    }

    public void removeStation(Station station) {
        validateRemoveStation();

        Optional<Section> upLineStation = findUpLineStation(station);
        Optional<Section> downLineStation = findDownLineStation(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Section section = Section.combine(downLineStation.get(), upLineStation.get());
            sections.add(section);
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    private Optional<Section> findDownLineStation(Station station) {
        return sections.stream()
            .filter(it -> it.hasDownStation(station))
            .findFirst();
    }

    private void validateRemoveStation() {
        if (sections.size() <= 1) {
            throw new SubwayException(SubwayErrorCode.CANNOT_DELETE_LAST_LINE);
        }
    }

    private Optional<Section> findUpLineStation(Station station) {
        return sections.stream()
            .filter(it -> it.hasUpStation(station))
            .findFirst();
    }

    public void addStation(Section section) {
        Stations stations = makeStations();

        stations.checkCanAddStation(section);

        if (stations.isNotEmpty()) {
            updateUpStationIfExists(section, stations);
            updateDownStationIfExists(section, stations);
        }

        addSection(section);
    }

    private void updateUpStationIfExists(Section section, Stations stations) {
        boolean isUpStationExisted = stations.containsStation(section.getUpStation());
        if (isUpStationExisted) {
            findUpLineStation(section.getUpStation())
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
        }
    }

    private void updateDownStationIfExists(Section section, Stations stations) {
        boolean isDownStationExisted = stations.containsStation(section.getDownStation());
        if (isDownStationExisted) {
            findDownLineStation(section.getDownStation())
                .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
        }
    }

}
