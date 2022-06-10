package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class SectionsManager {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST,
        CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new LinkedList<>();

    public void add(Section section) {
        sections.add(section);
    }

    public List<Section> getSections() {
        return sections;
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }


    public List<Station> getStationsOrdered(Station lastStation) {
        if (this.sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = lastStation;
        stations.add(downStation);

        for (int i = 0; i < sections.size(); i++) {
            Section section = getSectionByUpStation(downStation)
                .orElseThrow(RuntimeException::new);
            stations.add(section.getDownStation());
            downStation = section.getDownStation();
        }
        return stations;
    }


    public Optional<Section> getSectionByUpStation(Station station) {
        return this.sections.stream().filter(it -> it.getUpStation().equals(station)).findFirst();
    }

    public Optional<Section> getSectionByDownStation(Station station) {
        return this.sections.stream().filter(it -> it.getDownStation().equals(station)).findFirst();
    }

    public void removeSectionWithStation(Optional<Section> upSection,
        Optional<Section> downSection) {
        validateOnlyOneSectionExist();
        validateNoSectionForRemove(downSection, upSection);

        if (downSection.isPresent() && upSection.isPresent()) {
            sections.add(Section.mergeOf(downSection.get(), upSection.get()));
        }
        sections.remove(downSection.orElse(Section.emptyOf(null)));
        sections.remove(upSection.orElse(Section.emptyOf(null)));
    }

    private void validateNoSectionForRemove(Optional<Section> downSection,
        Optional<Section> upSection) {
        if (!downSection.isPresent() && !upSection.isPresent()) {
            throw new RuntimeException();
        }
    }

    private void validateOnlyOneSectionExist() {
        if (sections.size() == 1) {
            throw new RuntimeException();
        }
    }

    public void addStation(Station upStation, Station downStation, int distance, Line line,
        Station lastStation) {
        List<Station> stations = getStationsOrdered(lastStation);
        Section appendSection = new Section(line, upStation, downStation, distance);
        if (stations.isEmpty()) {
            sections.add(appendSection);
            return;
        }

        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == upStation);
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == downStation);

        validateDuplicateSection(isUpStationExisted, isDownStationExisted);
        validateNoBaseStation(stations, isUpStationExisted, isDownStationExisted);

        if (isUpStationExisted) {
            insertSection(appendSection, upStation);
            return;
        }
        if (isDownStationExisted) {
            insertSection(appendSection, downStation);
            return;
        }
    }

    private void insertSection(Section appendSection, Station baseStation) {
        Section upStation = getSectionByUpStation(baseStation)
            .orElse(Section.emptyOf(null));
        Section downStation = getSectionByDownStation(baseStation)
            .orElse(Section.emptyOf(null));

        upStation.insert(appendSection);
        downStation.insert(appendSection);

        sections.add(appendSection);
    }

    private void validateNoBaseStation(List<Station> stations, boolean isUpStationExisted,
        boolean isDownStationExisted) {
        if (!stations.isEmpty() && !isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private void validateDuplicateSection(boolean isUpStationExisted,
        boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }
}
