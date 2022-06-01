package nextstep.subway.line.domain.collections;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.exception.SectionCreateException;
import nextstep.subway.line.exception.SectionRemoveException;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    private static final int CANT_REMOVE_SECTION_SIZE = 1;

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
        validateCantRemoveSection();

        Optional<Section> upLineStation = sections.stream()
                .filter(section -> section.getUpStation().equals(station))
                .findFirst();

        Optional<Section> downLineStation = sections.stream()
                .filter(section -> section.getDownStation().equals(station))
                .findFirst();

        validateNotFoundRemoveSection(upLineStation.isPresent(), downLineStation.isPresent());

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            betweenCaseLinkSection(line, upLineStation.get(), downLineStation.get());
        }

        upLineStation.ifPresent(section -> sections.remove(section));
        downLineStation.ifPresent(section -> sections.remove(section));
    }

    private void betweenCaseLinkSection(Line line, Section upLineStation, Section downLineStation) {
        Station newUpStation = downLineStation.getUpStation();
        Station newDownStation = upLineStation.getDownStation();
        int newDistance = upLineStation.getDistance() + downLineStation.getDistance();
        sections.add(new Section(line, newUpStation, newDownStation, newDistance));
    }

    private void validateCreateSection(boolean isUpStationExisted, boolean isDownStationExisted) {
        validateAlreadyExisted(isUpStationExisted, isDownStationExisted);
        validateNotFound(isUpStationExisted,isDownStationExisted);

    }

    private void validateAlreadyExisted(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new SectionCreateException("[ERROR] 이미 등록된 구간 입니다.");
        }
    }

    private void validateNotFound(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (!isUpStationExisted && !isDownStationExisted) {
            throw new SectionCreateException("[ERROR] 등록할 수 없는 구간 입니다.");
        }
    }

    private void validateCantRemoveSection() {
        if (sections.size() == CANT_REMOVE_SECTION_SIZE) {
            throw new SectionRemoveException("[ERROR] 구간을 삭제할 수 없습니다.");
        }
    }

    private void validateNotFoundRemoveSection(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (!isUpStationExisted && !isDownStationExisted) {
            throw new SectionRemoveException("[ERROR] 삭제할 구간이 존재하지 않습니다.");
        }
    }

}
