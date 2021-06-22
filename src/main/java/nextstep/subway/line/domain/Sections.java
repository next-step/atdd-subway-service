package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new LinkedList<>();

    public Sections() {
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getSortedStation() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }
        List<Station> stations = new ArrayList<>();
        Station downStation = newFindUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Section nextLineStation = sections.stream()
                    .filter(it -> it.getUpStation().equals(finalDownStation))
                    .findFirst()
                    .orElse(null);
            if (nextLineStation == null) {
                break;
            }
            downStation = nextLineStation.getDownStation();
            stations.add(downStation);
        }
        return stations;
    }

    private Station newFindUpStation() {
        Station expectedFirstStationOfLine = sections.get(0).getUpStation();
        while (expectedFirstStationOfLine != null) {
            Station assumedFirstStation = expectedFirstStationOfLine;
            Section nextSection = getSections().stream()
                    .filter(it -> it.downStationSameWith(assumedFirstStation))
                    .findFirst()
                    .orElse(null);
            if (nextSection == null) {
                break;
            }
            expectedFirstStationOfLine = nextSection.getUpStation();
        }

        return expectedFirstStationOfLine;
    }

    public void add(Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }
        if (sections.contains(section)) {
            return;
        }
        List<Station> stations = getSortedStation();

        boolean isUpStationExisted = validateUpStation(stations, section.getUpStation());
        boolean isDownStationExisted = validateDownStation(stations, section.getDownStation());

        validateSectionBeforeAdd(isUpStationExisted, isDownStationExisted);
        if (isUpStationExisted) {
            addSectionWhenUpStationExists(section);
        }

        if (isDownStationExisted) {
            addSectionWhenDownStationExists(section);
        }
    }

    private boolean validateDownStation(List<Station> stations, Station downStation) {
        return downStation.isIncludedIn(stations);
    }

    private boolean validateUpStation(List<Station> stations, Station upStation) {
        return upStation.isIncludedIn(stations);
    }

    private void validateSectionBeforeAdd(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간입니다.");
        }

        if (!isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private void addSectionWhenDownStationExists(Section section) {
        sections.stream()
                .filter(it -> it.hasEqualDownStationWith(section))
                .findFirst()
                .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
        sections.add(section);
    }

    private void addSectionWhenUpStationExists(Section section) {
        sections.stream()
                .filter(it -> it.hasEqualUpStationWith(section))
                .findFirst()
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
        sections.add(section);
    }
}
