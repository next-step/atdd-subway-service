package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
public class Sections {
    private static final int LAST_SECTION = 1;
    private static final List<Station> CACHE = new ArrayList<>();

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> elements = new ArrayList<>();

    public void add(Section section) {
        validateAddition(section);

        repairSection(section);
        elements.add(section);
    }

    private void repairSection(Section section) {
        for (Section element : elements) {
            element.repair(section);
        }
    }

    private void validateAddition(Section section) {
        if (Objects.isNull(section)) {
            throw new IllegalArgumentException("구간이 없습니다.");
        }

        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(station -> station.match(section.getUpStation()));
        boolean isDownStationExisted = stations.stream().anyMatch(station -> station.match(section.getDownStation()));

        validateDuplicate(isUpStationExisted, isDownStationExisted);
        validateNoneMatch(isUpStationExisted, isDownStationExisted);
    }

    private void validateDuplicate(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }
    }

    private void validateNoneMatch(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (elements.isEmpty()) {
            return;
        }

        if (!isUpStationExisted && !isDownStationExisted) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }
    }

    public List<Section> getElements() {
        return elements;
    }

    public List<Station> getStations() {
        if (elements.isEmpty()) {
            return CACHE;
        }

        Stations stations = new Stations();
        stations.connectStation(getElements());

        return stations.getElements();
    }

    public void remove(Station station) {
        validateRemoval();

        Optional<Section> upSection = findUpSection(station);
        Optional<Section> downSection = findDownSection(station);

        if (upSection.isPresent() && downSection.isPresent()) {
            addReConstructedStation(upSection.get(), downSection.get());
        }

        upSection.ifPresent(elements::remove);
        downSection.ifPresent(elements::remove);
    }

    private void validateRemoval() {
        if (isOneSectionLeft()) {
            throw new IllegalStateException("하나 남은 구간은 삭제할 수 없습니다.");
        }
    }

    private boolean isOneSectionLeft() {
        return elements.size() <= LAST_SECTION;
    }

    private Optional<Section> findUpSection(Station station) {
        return elements.stream()
                .filter(section -> section.isSameDownStation(station))
                .findFirst();
    }

    private Optional<Section> findDownSection(Station station) {
        return elements.stream()
                .filter(section -> section.isSameUpStation(station))
                .findFirst();
    }

    private void addReConstructedStation(Section upSection, Section downSection) {
        upSection.reLocateDownStation(downSection);
        elements.add(upSection);
    }
}
