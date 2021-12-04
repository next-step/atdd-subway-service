package nextstep.subway.line.domain;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    public static final int SECTION_LIMIT_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections(Section... sections) {
        if (isContains(sections)) {
            return;
        }
        this.sections.addAll(asList(sections));
    }

    protected Sections() {
    }

    private boolean isContains(Section... newSections) {
        List<Section> sections = asList(newSections);
        return this.sections.stream()
            .anyMatch(savedSection -> sections.stream()
                .anyMatch(section -> Objects.equals(section, savedSection)));
    }

    public void addSection(Section section) {
        if (isContains(section)) {
            return;
        }
        sections.add(section);
    }

    public void addLineStation(Section section) {
        List<Station> stations = getStations();
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        int distance = section.getDistance();

        boolean isUpStationExisted = matchStation(stations, upStation);
        boolean isDownStationExisted = matchStation(stations, downStation);

        validationAlreadyRegisteredSection(isUpStationExisted, isDownStationExisted);
        validationIsNotRegisteredSection(stations, downStation, upStation);

        changeUpStation(downStation, upStation, distance, isUpStationExisted);
        changeDownStation(downStation, upStation, distance, isDownStationExisted);
        sections.add(section);
    }

    private boolean matchStation(List<Station> stations, Station upStation) {
        return stations.stream().anyMatch(it -> it.equals(upStation));
    }

    private boolean matchNotStation(List<Station> stations, Predicate<Station> predicate) {
        return stations.stream().noneMatch(predicate);
    }

    private void validationAlreadyRegisteredSection(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    private void validationIsNotRegisteredSection(List<Station> stations, Station downStation, Station upStation) {
        if (!stations.isEmpty()
            && matchNotStation(stations, isMatchUpStation(upStation))
            && matchNotStation(stations, isMatchDownStation(downStation))) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private Predicate<Station> isMatchUpStation(Station upStation) {
        return station -> station.equals(upStation);
    }

    private Predicate<Station> isMatchDownStation(Station downStation) {
        return station -> station.equals(downStation);
    }

    private void changeUpStation(Station downStation, Station upStation, int distance,
        boolean isUpStationExisted) {
        if (isUpStationExisted) {
            findSections(isUpStation(upStation))
                .ifPresent(it -> it.updateUpStation(downStation, distance));
        }
    }

    private Predicate<Section> isUpStation(Station upStation) {
        return it -> it.getUpStation().equals(upStation);
    }

    private void changeDownStation(Station downStation, Station upStation, int distance,
        boolean isDownStationExisted) {
        if (isDownStationExisted) {
            findSections(isDownStation(downStation))
                .ifPresent(it -> it.updateDownStation(upStation, distance));
        }
    }

    private Predicate<Section> isDownStation(Station downStation) {
        return it -> it.getDownStation().equals(downStation);
    }

    public void removeLineStation(Station station) {
        validateLimitSize();

        Optional<Section> upLineStation = findSections(isUpStation(station));
        Optional<Section> downLineStation = findSections(isDownStation(station));

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance =
                upLineStation.get().getDistance() + downLineStation.get().getDistance();
            sections.add(new Section(newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    private void validateLimitSize() {
        if (sections.size() <= SECTION_LIMIT_SIZE) {
            throw new RuntimeException();
        }
    }

    private Optional<Section> findSections(Predicate<Section> sectionPredicate) {
        return sections.stream()
            .filter(sectionPredicate)
            .findFirst();
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation =
                findSections(isUpStation(finalDownStation));
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = findSections(isDownStation(finalDownStation));
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }
        return downStation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sections sections1 = (Sections) o;
        return Objects.equals(sections, sections1.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sections);
    }

}
