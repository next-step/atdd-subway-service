package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public List<Section> getSections() {
        return sections;
    }

    public void add(Section section) {
        boolean isUpStationExisted = isStationExisted(section.getUpStation());
        boolean isDownStationExisted = isStationExisted(section.getDownStation());

        raiseIfNotValidAddSection(isUpStationExisted, isDownStationExisted);

        if (isEmpty()) {
            sections.add(section);
            return;
        }

        if (isUpStationExisted) {
            getNextSectionByEqualUpStation(section.getUpStation())
                    .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));

            sections.add(section);
            return;
        }

        if (isDownStationExisted) {
            getNextSectionByEqualDownStation(section.getDownStation())
                    .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));

            sections.add(section);
            return;
        }
        throw new RuntimeException();
    }

    private boolean isStationExisted(Station station) {
        return sections.stream()
                .anyMatch(it -> it.getUpStation() == station || it.getDownStation() == station);
    }

    private void raiseIfNotValidAddSection(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!sections.isEmpty() && !isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    public void remove(Section section) {
        sections.remove(section);
    }

    public int size() {
        return sections.size();
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public Section get(int index) {
        return sections.get(index);
    }

    public Optional<Section> getNextSectionByEqualUpStation(Station station) {
        return sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
    }

    public Optional<Section> getNextSectionByEqualDownStation(Station station) {
        return sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();
    }

}
