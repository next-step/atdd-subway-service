package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(section::isSameUpStation);
        boolean isDownStationExisted = stations.stream().anyMatch(section::isSameDownStation);

        validateExistStations(isUpStationExisted, isDownStationExisted);
        validateNotMatchedStations(section, stations);

        if (stations.isEmpty()) {
            sections.add(section);
            return;
        }

        if (isUpStationExisted) {
            connectSectionIfHasSameDownStation(section);
            return;
        }

        connectSectionIfHasSameUpStation(section);
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
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

    public List<Section> getSections() {
        return sections;
    }

    private void connectSectionIfHasSameDownStation(Section section) {
        sections.stream()
                .filter(it -> section.isSameDownStation(it.getDownStation()))
                .findFirst()
                .ifPresent(it -> it.updateDownStationBySection(section));
        sections.add(section);
    }

    private void connectSectionIfHasSameUpStation(Section section) {
        sections.stream()
                .filter(it -> section.isSameUpStation(it.getUpStation()))
                .findFirst()
                .ifPresent(it -> it.updateUpStationBySection(section));
        sections.add(section);
    }

    private void validateNotMatchedStations(Section section, List<Station> stations) {
        if (!stations.isEmpty() && stations.stream().noneMatch(section::isSameUpStation) &&
                stations.stream().noneMatch(section::isSameDownStation)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private void validateExistStations(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
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
}
