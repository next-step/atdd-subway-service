package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.*;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    //add
    public void addSection(Section section) {
        sections.add(section);
    }

    public void addStations(Section section) {
        List<Station> stations = getStations();
        validateAddStation(stations, section.getUpStation(), section.getDownStation());
        if (stations.isEmpty()) {
            addSection(section);
            return;
        }

        if (isStationExisted(stations, section.getUpStation())) {
            updateUpStation(section);
            addSection(section);
        } else if (isStationExisted(stations, section.getDownStation())) {
            updateDownStation(section);
            addSection(section);
        } else {
            throw new RuntimeException();
        }
    }

    //remove
    public void removeStation(Station station) {
        validateRemoveSection();
        Optional<Section> upLineSection = getUpLineSection(station);
        Optional<Section> downLineSection = getDownLineSection(station);

        if (upLineSection.isPresent() && downLineSection.isPresent()) {
            mergeSection(upLineSection.get(), downLineSection.get());
        }
        removeSection(upLineSection);
        removeSection(downLineSection);
    }

    private void removeSection(Optional<Section> section) {
        section.ifPresent(it -> sections.remove(it));
    }

    //get
    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station upStation = findUpStation();
        while (upStation != null) {
            stations.add(upStation);
            upStation = getDownStation(upStation);
        }

        return stations;
    }

    private Optional<Section> getUpLineSection(Station station) {
        return sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
    }

    private Optional<Section> getDownLineSection(Station station) {
        return sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();
    }

    private Station getUpStation(Station station) {
        return sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst()
                .map(Section::getUpStation)
                .orElse(null);
    }

    private Station getDownStation(Station station) {
        return sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst()
                .map(Section::getDownStation)
                .orElse(null);
    }

    private Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();

        Station finalDownStation = downStation;
        while (finalDownStation != null) {
            downStation = finalDownStation;
            finalDownStation = getUpStation(finalDownStation);
        }

        return downStation;
    }

    //update
    private void mergeSection(Section upLineStation, Section downLineStation) {
        Line line = upLineStation.getLine();
        Station newUpStation = downLineStation.getUpStation();
        Station newDownStation = upLineStation.getDownStation();
        int newDistance = upLineStation.getDistance() + downLineStation.getDistance();
        addSection(new Section(line, newUpStation, newDownStation, newDistance));
    }

    private void updateUpStation(Section section) {
        sections.stream()
                .filter(it -> it.getUpStation() == section.getUpStation())
                .findFirst()
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
    }

    private void updateDownStation(Section section) {
        sections.stream()
                .filter(it -> it.getDownStation() == section.getDownStation())
                .findFirst()
                .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
    }

    //validate
    private boolean isStationExisted(List<Station> stations, Station station) {
        return stations.stream().anyMatch(it -> it == station);
    }

    private void validateExistSection(List<Station> stations, Station upStation, Station downStation) {
        boolean isUpStationExisted = isStationExisted(stations, upStation);
        boolean isDownStationExisted = isStationExisted(stations, downStation);

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    private void validateAddStation(List<Station> stations, Station upStation, Station downStation) {
        validateExistSection(stations, upStation, downStation);

        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == upStation) &&
                stations.stream().noneMatch(it -> it == downStation)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private void validateRemoveSection() {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }
    }
}
