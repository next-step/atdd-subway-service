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

    public List<Section> getSections() {
        return sections;
    }

    Station findUpStation() {
        Section section = sections.get(FIRST_SECTION);
        Station downStation = section.getUpStation();

        while (section.isExists()) {
            downStation = section.getUpStation();
            section = findNextStationBackward(downStation);
        }
        return downStation;
    }

    List<Station> findStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station upStation = findUpStation();
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

        if (findStations().isEmpty()) {
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

    void remove(Station station) {
        validateCanRemove();
        Section beforeSection = findNextStationForward(station);
        Section nextSection = findNextStationBackward(station);

        addJoinedSection(beforeSection, nextSection);
        remove(beforeSection);
        remove(nextSection);
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
        return findStations().stream().anyMatch(it -> it == upStation);
    }

    private void validateCanRemove() {
        if (sections.size() <= CANNOT_REMOVE_COUNT) {
            throw new SectionRemoveFailedException("구간을 제거할 수 없습니다.");
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

    private void addJoinedSection(Section beforeSection, Section nextSection) {
        if (beforeSection.isExists() && nextSection.isExists()) {
            sections.add(new Section(beforeSection, nextSection, beforeSection.add(nextSection)));
        }
    }

    private void remove(Section section) {
        if (section.isExists()) {
            sections.remove(section);
        }
    }

    public int maxAddFare() {
        return sections.stream()
                .map(Section::getLine)
                .map(Line::getAddFare)
                .mapToInt(it -> it)
                .max()
                .orElse(0);
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
