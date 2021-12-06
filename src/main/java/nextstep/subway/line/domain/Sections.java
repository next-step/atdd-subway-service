package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public void add(Section section) {
        sections.add(section);
    }

    public List<Section> getSections() {
        return sections;
    }

    List<Station> getStationsInOrder() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station station = findFirstUpStation();
        stations.add(station);

        while (isExistNextSection(station)) {
            Section nextSection = findNextSection(station);
            station = nextSection.getDownStation();
            stations.add(station);
        }
        return stations;
    }

    private Station findFirstUpStation() {
        Station upStation = sections.get(0).getUpStation();
        while (isExistPreSection(upStation)) {
            Section preSection = findPreSection(upStation);
            upStation = preSection.getUpStation();
        }
        return upStation;
    }

    private boolean isExistPreSection(Station upStation) {
        return sections.stream()
                .anyMatch(section -> section.equalsDownStation(upStation));
    }

    private Section findPreSection(Station upStation) {
        return sections.stream()
                .filter(section -> section.equalsDownStation(upStation))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("이전 구간 없습니다."));
    }

    private boolean isExistNextSection(Station downStation) {
        return sections.stream()
                .anyMatch(section -> section.equalsUpStation(downStation));
    }

    private Section findNextSection(Station downStation) {
        return sections.stream()
                .filter(section -> section.equalsUpStation(downStation))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("다음 구간 없습니다."));
    }

    void addLineSection(Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        int distance = section.getDistance();
        
        List<Station> stations = getStationsInOrder();
        boolean isUpStationExisted = isStationExisted(upStation, stations);
        boolean isDownStationExisted = isStationExisted(downStation, stations);

        validateStation(isUpStationExisted, isDownStationExisted);

        if (isUpStationExisted) {
            updateUpStation(upStation, downStation, distance);
        }

        if (isDownStationExisted) {
            updateDownStation(upStation, downStation, distance);
        }

        sections.add(section);
    }

    private boolean isStationExisted(Station upStation, List<Station> stations) {
        return stations.stream().anyMatch(it -> it == upStation);
    }

    private void validateStation(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private void updateUpStation(Station upStation, Station downStation, int distance) {
        sections.stream()
                .filter(it -> it.equalsUpStation(upStation))
                .findFirst()
                .ifPresent(it -> it.updateUpStation(downStation, distance));
    }

    private void updateDownStation(Station upStation, Station downStation, int distance) {
        sections.stream()
                .filter(it -> it.equalsDownStation(downStation))
                .findFirst()
                .ifPresent(it -> it.updateDownStation(upStation, distance));
    }
}
