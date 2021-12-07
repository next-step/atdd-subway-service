package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();
    
    protected Sections() {
    }

    private Sections(List<Section> sections) {
        this.sections = sections;
    }
    
    public static Sections from(Section...sections) {
        return new Sections(Stream
                .of(sections)
                .collect(Collectors.toList()));
    }
    
    public List<Section> getSections() {
        return sections;
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
    
    void add(Section newSection) {
        checkValidStations(newSection);
        if (addIfSameUpStations(newSection)) {
            return;
        }
        if (addIfSameDownStations(newSection)) {
            return;
        }
        sections.add(newSection);
    }
    
    void remove(Station station) {
        checkRemovableStation(station);
        
        Optional<Section> upLineStation = getSections().stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
        Optional<Section> downLineStation = getSections().stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            Distance newDistance = upLineStation.get().getDistance().plus(downLineStation.get().getDistance());
            getSections().add(Section.of(upLineStation.get().getLine(), newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(it -> getSections().remove(it));
        downLineStation.ifPresent(it -> getSections().remove(it));
        
        /*        if (removeIfCombinableStation(station)) {
            return;
        }
        
        removeEdgeStation(station);*/
    }
    
    int count() {
        return sections.size();
    }
    
    private void checkValidStations(Section section) {
        if (sections.isEmpty()) {
            return;
        }

        if (isUpStationExisted(section) && isDownStationExisted(section)) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!isUpStationExisted(section) && !isDownStationExisted(section)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }
    
    private boolean addIfSameUpStations(Section newSection) {
        if (isUpStationExisted(newSection)) {
            getSections().stream()
                .filter(it -> it.getUpStation() == newSection.getUpStation())
                .findFirst()
                .ifPresent(it -> it.updateUpStation(newSection.getDownStation(), newSection.getDistance()));
            sections.add(newSection);
            return true;
        }
        return false;
    }
    
    private boolean addIfSameDownStations(Section newSection) {
        if (isDownStationExisted(newSection)) {
            getSections().stream()
                .filter(it -> it.getDownStation() == newSection.getDownStation())
                .findFirst()
                .ifPresent(it -> it.updateDownStation(newSection.getUpStation(), newSection.getDistance()));
            sections.add(newSection);
            return true;
        }
        return false;
    }
    
    private boolean isUpStationExisted(Section section) {
        return getStations().stream().anyMatch(it -> it.equals(section.getUpStation()));
    }
    
    private boolean isDownStationExisted(Section section) {
        return getStations().stream().anyMatch(it -> it.equals(section.getDownStation()));
    }
    
    private void checkRemovableStation(Station station) {
        if (sections.size() <= 1) {
            throw new IllegalArgumentException("구간은 하나이상 존재해야 합니다.");
        }
        
        if (!getStations().contains(station)) {
            throw new IllegalArgumentException("노선에 등록된 역만 제거할 수 있습니다.");
        }
    }
    
    private boolean removeIfCombinableStation(Station station) {
        Optional<Section> upSection = sections.stream()
                .filter(section -> station.equals(section.getDownStation()))
                .findFirst();

        Optional<Section> downSection = sections.stream()
                        .filter(section -> station.equals(section.getUpStation()))
                        .findFirst();
        
        if (upSection.isPresent() && downSection.isPresent()) {
            upSection.get().combine(downSection.get());
            sections.removeIf(section -> section.equals(downSection.get()));
            return true;
        }
        return false;
    }
    
    private void removeEdgeStation(Station station) {
        Optional<Section> edgeSection = sections.stream()
                .filter(section -> section.isContainStation(station))
                .findFirst();
        
        if (edgeSection.isPresent()) {
            sections.removeIf(section -> section.equals(edgeSection.get()));
        }
    }
    
}
