package nextstep.subway.line.domain;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST,
        CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public static Sections of() {
        return new Sections();
    }

    public void add(Section section) {
        validateDuplicate(section);
        validateAddAblePosition(section);
        updateUpStationIfSameUpStation(section);
        updateDownStationIfSameDownStation(section);
        sections.add(section);
    }

    public Integer count() {
        return sections.size();
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getStationsInOrder() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        return mapStations();
    }

    private List<Station> mapStations() {
        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Optional<Section> nextLineStation = findNextStation(downStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Station findUpStation() {
        Station firstStation = getFirstStation();
        while (firstStation != null) {
            Station finalDownStation = firstStation;
            Optional<Section> nextLineStation = sections.stream()
                .filter(it -> it.getDownStation() == finalDownStation)
                .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            firstStation = nextLineStation.get().getUpStation();
        }

        return firstStation;
    }

    private Optional<Section> findNextStation(Station finalDownStation) {
        return sections.stream()
            .filter(it -> it.isNextStation(finalDownStation))
            .findFirst();
    }

    private Station getFirstStation() {
        return sections.get(0).getUpStation();
    }

    private void validateDuplicate(Section section) {
        if (isDuplicatedSection(section)) {
            throw new InvalidParameterException("이미 등록된 구간 입니다.");
        }
    }

    private void validateAddAblePosition(Section section) {
        if (isAddAblePosition(section)) {
            throw new InvalidParameterException("등록할 수 없는 구간 입니다.");
        }
    }

    private void updateUpStationIfSameUpStation(Section section) {
        sections.stream()
            .filter(section::isSameUpStationOf)
            .findFirst()
            .ifPresent(it -> it.updateUpStationOf(section));
    }

    private void updateDownStationIfSameDownStation(Section section) {
        sections.stream()
            .filter(section::isSameDownStationOf)
            .findFirst()
            .ifPresent(it -> it.updateDownStationOf(section));
    }


    private boolean isAddAblePosition(Section section) {
        List<Station> stations = getStationsInOrder();
        return !sections.isEmpty() && stations.stream().noneMatch(section::isSameUpStation)
            && stations.stream().noneMatch(section::isSameDownStation);
    }

    private boolean isDuplicatedSection(Section section) {
        return sections.stream()
            .anyMatch(section::isSameUpStationAndDownStation);
    }
}
