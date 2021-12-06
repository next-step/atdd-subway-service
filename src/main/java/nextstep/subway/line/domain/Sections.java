package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    protected static Sections empty() {
        return new Sections();
    }

    public List<Station> getSortedStations() {
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

    public List<Section> getSections() {
        //return Collections.unmodifiableList(sections);
        return sections;
    }

    public void add(Section newSection) {
        if (sections.isEmpty()) {
            sections.add(newSection);
            return;
        }
        validationAlreadyAdded(newSection);
        validationNotAdded(newSection);

        updateStation(newSection);
        sections.add(newSection);
    }

    private void updateStation(Section newSection) {
        if (isUpStationExisted(newSection)) {
            sections.stream()
                    .filter(section -> section.equalsUpStation(newSection.getUpStation()))
                    .findFirst()
                    .ifPresent(section -> section.updateUpStation(newSection.getDownStation(), newSection.getDistance()));
        }
        if (isDownStationExisted(newSection)) {
            sections.stream()
                    .filter(section -> section.equalsDownStation(newSection.getDownStation()))
                    .findFirst()
                    .ifPresent(section -> section.updateDownStation(newSection.getUpStation(), newSection.getDistance()));
        }
    }

    private boolean isDownStationExisted(Section newSection) {
        return getSortedStations().stream().anyMatch(station -> station.equals(newSection.getDownStation()));
    }

    private boolean isUpStationExisted(Section newSection) {
        return getSortedStations().stream().anyMatch(station -> station.equals(newSection.getUpStation()));
    }

    private void validationAlreadyAdded(Section newSection) {
        if (isUpStationExisted(newSection) && isDownStationExisted(newSection)) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    private void validationNotAdded(Section newSection) {
        boolean duplicateUpStation = getSortedStations().stream()
                .noneMatch(station -> station.equals(newSection.getUpStation()));
        boolean duplicateDownStation = getSortedStations().stream()
                .noneMatch(station -> station.equals(newSection.getDownStation()));
        if (duplicateUpStation && duplicateDownStation) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }
}
