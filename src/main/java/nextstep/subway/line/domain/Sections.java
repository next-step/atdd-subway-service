package nextstep.subway.line.domain;

import nextstep.subway.exception.InvalidSectionException;
import nextstep.subway.exception.SectionNotFoundException;
import nextstep.subway.exception.StationNotFoundException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> list;

    public Sections() {
        this.list = new ArrayList<>();
    }

    public Sections(List<Section> list) {
        this.list = list;
    }

    public void add(Section section) {
        this.list.add(section);
    }

    public List<Section> getList() {
        return this.list;
    }

    public List<Station> getStations() {
        Station curStation = getLineUpStation();
        Station lineDownStation = getLineDownStation();
        List<Station> stations = new ArrayList<>();
        stations.add(curStation);
        while (!curStation.equals(lineDownStation)) {
            Section section = findSectionWithUpStation(curStation).orElseThrow(SectionNotFoundException::new);
            curStation = section.getDownStation();
            stations.add(curStation);
        }
        return stations;
    }

    public void addSection(Line line, Section section) {
        list.add(section);
        section.updateLine(line);
    }

    public void insertSection(Line line, Section section) {
        insertSectionWhenSectionIsHeadOrTail(line, section);
        if (containBothStation(section)) {
            return;
        }
        insertSectionWhenSectionIsMiddle(line, section);
    }

    private void insertSectionWhenSectionIsMiddle(Line line, Section insertSection) {
        Optional<Section> find = findSectionWithUpStation(insertSection.getUpStation());
        find.ifPresent(frontSection -> insertSectionWhenUpStationSame(line, frontSection, insertSection));
        if (containBothStation(insertSection)) {
            return;
        }
        Optional<Section> find2 = findSectionWithDownStation(insertSection.getDownStation());
        find2.ifPresent(rearSection -> insertSectionWhenDownStationSame(line, rearSection, insertSection));
    }

    private void insertSectionWhenSectionIsHeadOrTail(Line line, Section insertSection) {
        if (insertSection.getDownStation().equals(getLineUpStation()) ||
                insertSection.getUpStation().equals(getLineDownStation())) {
            addSection(line, insertSection);
        }
    }

    private void insertSectionWhenUpStationSame(Line line, Section section, Section insertSection) {
        section.updateUpStation(insertSection.getDownStation(), insertSection.getDistance());
        addSection(line, insertSection);
    }

    public void insertSectionWhenDownStationSame(Line line, Section section, Section insertSection) {
        section.updateDownStation(insertSection.getUpStation(), insertSection.getDistance());
        addSection(line, insertSection);
    }

    public void validateInsertSection(Section section) {
        if (containBothStation(section)) {
            throw new InvalidSectionException("이미 노선에 포함된 구간은 추가할 수 없습니다.");
        }

        if (containNoneStation(section)) {
            throw new InvalidSectionException("구간 내 지하철 역이 하나는 등록된 상태여야 합니다.");
        }
    }

    private boolean containBothStation(Section section) {
        return containStation(section.getUpStation()) && containStation(section.getDownStation());
    }

    private boolean containNoneStation(Section section) {
        return !containStation(section.getUpStation()) && !containStation(section.getDownStation());
    }

    private boolean containStation(Station station) {
        return list.stream().anyMatch(section -> section.containsStation(station));
    }

    private Set<Station> getStationSet() {
        Set<Station> stationSet = new HashSet<>();
        for (Section section : list) {
            stationSet.add(section.getUpStation());
            stationSet.add(section.getDownStation());
        }
        return stationSet;
    }

    private Station findFirstStation(Set<Station> stationSet) {
        return stationSet.stream()
                .findFirst()
                .orElseThrow(StationNotFoundException::new);
    }

    private Station getLineUpStation() {
        Set<Station> stationSet = getStationSet();
        list.forEach(section -> stationSet.remove(section.getDownStation()));
        return findFirstStation(stationSet);
    }

    private Station getLineDownStation() {
        Set<Station> stationSet = getStationSet();
        list.forEach(section -> stationSet.remove(section.getUpStation()));
        return findFirstStation(stationSet);
    }

    private Optional<Section> findSectionWithUpStation(Station upStation) {
        return list.stream()
                .filter(section -> upStation.equals(section.getUpStation()))
                .findFirst();
    }

    private Optional<Section> findSectionWithDownStation(Station downStation) {
        return list.stream()
                .filter(section -> downStation.equals(section.getDownStation()))
                .findFirst();
    }
}
