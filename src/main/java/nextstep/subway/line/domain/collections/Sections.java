package nextstep.subway.line.domain.collections;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Station findDepartStation() {
        Section notOrderFirstSection = sections.stream().findFirst().orElseThrow(RuntimeException::new);
        Station departStation = notOrderFirstSection.getUpStation();

        while (isExistUpSection(departStation)) {
            final Station finalDepartStation = departStation;
            Section nextUpSection = sections.stream()
                    .filter(section -> section.getDownStation().equals(finalDepartStation))
                    .findFirst()
                    .orElseThrow(RuntimeException::new);
            departStation = nextUpSection.getUpStation();
        }

        return departStation;
    }

    private boolean isExistUpSection(Station departStation) {
        return sections.stream().anyMatch(section -> section.getDownStation().equals(departStation));
    }

    public List<Station> getStations() {
        Station departStation = findDepartStation();
        return sortStationsBySection(departStation);
    }

    private List<Station> sortStationsBySection(Station upStation) {
        List<Station> stations = new ArrayList<>();
        stations.add(upStation);

        while (isExistDownStation(upStation)) {
            final Station finalUpStation = upStation;
            Section nextDownSection = sections.stream()
                    .filter(section -> section.getUpStation().equals(finalUpStation))
                    .findFirst()
                    .orElseThrow(RuntimeException::new);
            upStation = nextDownSection.getDownStation();
            stations.add(upStation);
        }

        return stations;
    }

    private boolean isExistDownStation(Station upSection) {
        return sections.stream().anyMatch(section -> section.getUpStation().equals(upSection));
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public void addNewSection(Line line, Station upStation, Station downStation, int distance) {
        sections.add(createNewSection(line, upStation, downStation, distance));
    }

    private Section createNewSection(Line line, Station upStation, Station downStation, int distance) {
        if (sections.isEmpty()) {
            return new Section(line, upStation, downStation, distance);
        }
        
        List<Station> stations = getStations();
        boolean isUpStationExisted = isStationExisted(upStation, stations);
        boolean isDownStationExisted = isStationExisted(downStation, stations);

        validateCreateSection(isUpStationExisted, isDownStationExisted);

        if (isUpStationExisted) {
            updateExistingSectionByUpStation(upStation, downStation, distance);
        }
        if (isDownStationExisted) {
            updateExistingSectionByDownStation(upStation, downStation, distance);
        }

        return new Section(line, upStation, downStation, distance);
    }

    private void updateExistingSectionByDownStation(Station upStation, Station downStation, int distance) {
        sections.stream()
                .filter(section -> section.getDownStation().equals(downStation))
                .findFirst()
                .ifPresent(section -> section.updateDownStation(upStation, distance));
    }

    private void updateExistingSectionByUpStation(Station upStation, Station downStation, int distance) {
        sections.stream()
                .filter(section -> section.getUpStation().equals(upStation))
                .findFirst()
                .ifPresent(section -> section.updateUpStation(downStation, distance));
    }

    private boolean isStationExisted(Station station, List<Station> stations) {
        return stations.stream().anyMatch(it -> it.equals(station));
    }

    public void removeSection(Line line, Station station) {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> upLineStation = sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
        Optional<Section> downLineStation = sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    private void validateCreateSection(boolean isUpStationExisted, boolean isDownStationExisted) {
        validateAlreadyExisted(isUpStationExisted, isDownStationExisted);
        validateNotFound(isUpStationExisted,isDownStationExisted);

    }

    private void validateAlreadyExisted(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    private void validateNotFound(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (!isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

}
