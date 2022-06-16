package nextstep.subway.sections.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    private static final int LOWER_SECTION_SIZE = 1;
    private static final int FIRST_INDEX = 0;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST,
        CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public Sections(Section section) {
        this.sections.add(section);
    }

    public void add(Section section) {
        sections.add(section);
    }

    public void updateSection(Section requestSection) {
        if (orderedStations().isEmpty()) {
            sections.add(requestSection);
            return;
        }

        Station sameUpStation = findSameStation(requestSection.getUpStation());
        Station sameDownStation = findSameStation(requestSection.getDownStation());

        validate(sameUpStation, sameDownStation);

        updateSection(requestSection, sameUpStation, sameDownStation);
    }

    private void updateSection(Section requestSection, Station sameUpStation,
        Station sameDownStation) {
        if (sameUpStation != null) {
            updateStation(requestSection.getDownStation(), sameUpStation,
                requestSection.getDistance());
            sections.add(requestSection);
        }

        if (sameDownStation != null) {
            updateStation(requestSection.getUpStation(), sameUpStation,
                requestSection.getDistance());
            sections.add(requestSection);
        }
    }

    private void updateStation(Station station, Station sameUpStation, int distance) {
        sections.stream()
            .filter(it -> it.getUpStation().equals(sameUpStation))
            .findFirst()
            .ifPresent(it -> it.updateUpStation(station, distance));
    }

    private void validate(Station sameUpStation, Station sameDownStation) {
        if (sameUpStation != null && sameDownStation != null) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (sameUpStation == null && sameDownStation == null) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

    public List<Station> orderedStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Section nextSection = findHasSameUpStationSection(finalDownStation);
            if (nextSection == null) {
                break;
            }
            downStation = nextSection.getDownStation();
            stations.add(downStation);
        }

        return Collections.unmodifiableList(stations);
    }

    private Station findUpStation() {
        Station downStation = sections.get(FIRST_INDEX).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Section nextSection = findHasSameDownStationSection(finalDownStation);
            if (nextSection == null) {
                break;
            }
            downStation = nextSection.getUpStation();
        }

        return downStation;
    }

    public void delete(Station willDeleteStation) {
        if (sections.size() <= LOWER_SECTION_SIZE) {
            throw new RuntimeException("구간이 하나뿐일 때는 삭제할 수 없습니다.");
        }

        Section hasSameUpStationSection = findHasSameUpStationSection(willDeleteStation);
        Section hasSameDownStationSection = findHasSameDownStationSection(willDeleteStation);

        if (hasSameUpStationSection != null && hasSameDownStationSection != null) {
            makeConnectSection(hasSameUpStationSection, hasSameDownStationSection);
        }

        removeSection(hasSameUpStationSection, hasSameDownStationSection);
    }

    private void makeConnectSection(Section hasUpStationSection, Section hasDownStationSection) {
        Station newUpStation = hasDownStationSection.getUpStation();
        Station newDownStation = hasUpStationSection.getDownStation();
        int newDistance =
            hasUpStationSection.getDistance() + hasDownStationSection.getDistance();
        sections.add(new Section(hasUpStationSection.getLine(), newUpStation, newDownStation,
            newDistance));
    }

    private void removeSection(Section hasUpStationSection, Section hasDownStationSection) {
        if (hasUpStationSection != null) {
            sections.remove(hasUpStationSection);
        }
        if (hasDownStationSection != null) {
            sections.remove(hasDownStationSection);
        }
    }


    private Section findHasSameUpStationSection(Station station) {
        return sections.stream().filter(it -> it.getUpStation().equals(station)).findFirst()
            .orElse(null);
    }

    private Section findHasSameDownStationSection(Station station) {
        return sections.stream().filter(it -> it.getDownStation().equals(station)).findFirst()
            .orElse(null);
    }

    private Station findSameStation(Station station) {

        return orderedStations().stream().filter(it -> it.equals(station)).findFirst().orElse(null);
    }


}
