package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Embeddable
public class Sections {

    private static final int CANNOT_REMOVE_COUNT = 1;
    private static final int FIRST_SECTION = 0;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    Station getUpStation() {
        Section section = sections.get(FIRST_SECTION);
        Station downStation = section.getUpStation();

        while (section.isExists()) {
            downStation = section.getUpStation();
            section = findNextStationBackward(downStation);
        }
        return downStation;
    }

    List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station upStation = getUpStation();
        stations.add(upStation);

        Section section = findNextStationForward(upStation);
        Station downStation;

        while (section.isExists()) {
            downStation = section.getDownStation();
            stations.add(downStation);
            section = findNextStationForward(downStation);
        }
        return stations;
    }

    void addSection(Line line, Station upStation, Station downStation, Distance distance) {
        validateAdd(upStation, downStation);

        if (getStations().isEmpty()) {
            sections.add(new Section(line, upStation, downStation, distance));
            return;
        }
        if (isStationExisted(upStation)) {
            Section sectionUpStationMatched = findNextStationForward(upStation);
            addSectionsWhenUpStationMatched(line, upStation, downStation, distance, sectionUpStationMatched);
            return;
        }
        if (isStationExisted(downStation)) {
            Section sectionDownStationMatched = findNextStationBackward(downStation);
            addSectionsWhenDownStationMatched(line, upStation, downStation, distance, sectionDownStationMatched);
            return;
        }
        throw new SectionAddFailedException();
    }

    private void addSectionsWhenUpStationMatched(Line line, Station upStation, Station downStation, Distance distance, Section section) {
        if (section.isExists()) {
            section.updateUpStation(downStation, distance);
        }
        sections.add(new Section(line, upStation, downStation, distance));
    }

    private void addSectionsWhenDownStationMatched(Line line, Station upStation, Station downStation, Distance distance, Section section) {
        if (section.isExists()) {
            section.updateDownStation(upStation, distance);
        }
        sections.add(new Section(line, upStation, downStation, distance));
    }

    private void validateAdd(Station upStation, Station downStation) {
        if (isStationExisted(upStation) && isStationExisted(downStation)) {
            throw new SectionAddFailedException("이미 등록된 구간 입니다.");
        }
        if (!sections.isEmpty() && !isStationExisted(upStation) && !isStationExisted(downStation)) {
            throw new SectionAddFailedException("등록할 수 없는 구간 입니다.");
        }
    }

    private boolean isStationExisted(Station upStation) {
        return getStations().stream().anyMatch(it -> it == upStation);
    }

    void remove(Station station) {
        if (sections.size() <= CANNOT_REMOVE_COUNT) {
            throw new SectionRemoveFailedException("구간을 제거할 수 없습니다.");
        }

        Section upStation = findNextStationForward(station);
        Section downStation = findNextStationBackward(station);

        if (upStation.isExists() && downStation.isExists()) {
            Station newUpStation = downStation.getUpStation();
            Station newDownStation = upStation.getDownStation();
            Distance newDistance = upStation.getDistance().getAddedDistance(downStation.getDistance());
            sections.add(new Section(sections.get(FIRST_SECTION).getLine(), newUpStation, newDownStation, newDistance));
        }

        if (upStation.isExists()) {
            sections.remove(upStation);
        }
        if (downStation.isExists()) {
            sections.remove(downStation);
        }
    }

    private Section findNextStationBackward(Station station) {
        return sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst()
                .orElse(Section.EMPTY);
    }

    private Section findNextStationForward(Station station) {
        return sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst()
                .orElse(Section.EMPTY);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sections sections1 = (Sections) o;
        return Objects.equals(sections, sections1.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sections);
    }
}
