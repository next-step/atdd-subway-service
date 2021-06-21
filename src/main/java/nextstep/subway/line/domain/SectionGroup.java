package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Embeddable
public class SectionGroup {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    /**
     * 생성자
     */
    protected SectionGroup() {
    }

    public SectionGroup(List<Section> sections) {
        this.sections = sections;
    }

    /**
     * 비즈니스 메소드
     */

    public void addSection(Section section) {
        if (!sections.isEmpty()) {
            boolean isUpStationExisted = isExistsStation(section.getUpStation());
            boolean isDownStationExisted = isExistsStation(section.getDownStation());

            verifyAddable(isUpStationExisted, isDownStationExisted);
            changeUpStationIfExists(section, isUpStationExisted);
            changeDownStationIfExists(section, isDownStationExisted);
        }

        sections.add(section);
    }

    public void removeSection(Line line, Station station) {
        verifyDeletable();

        Optional<Section> upSection = findDownSection(station);
        Optional<Section> downSection = findUpSection(station);

        if (upSection.isPresent() && downSection.isPresent()) {
            createNewSection(line, upSection, downSection);
        }

        removeSections(upSection, downSection);
    }

    public List<Station> findStationsOrderUpToDown() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpEndStation();
        stations.add(downStation);
        iterateToAdd(stations, downStation);

        return stations;
    }

    public Station findUpEndStation() {
        Station downStation = sections.get(0).getUpStation();
        return iterateToFindUpEndStation(downStation);
    }

    /**
     * 기타 메소드
     */
    private Station iterateToFindUpEndStation(Station downStation) {
        Optional<Section> nextSection = findUpSection(downStation);
        Station nextStation = downStation;
        while (nextSection.isPresent()) {
            nextStation = nextSection.get().getUpStation();
            nextSection = findUpSection(nextStation);
        }
        return nextStation;
    }

    private Optional<Section> findUpSection(Station finalDownStation) {
        return sections.stream()
                .filter(it -> it.getDownStation() == finalDownStation)
                .findFirst();
    }

    private void iterateToAdd(List<Station> stations, Station downStation) {
        Optional<Section> nextSection = findDownSection(downStation);
        while (nextSection.isPresent()) {
            Station nextStation = nextSection.get().getDownStation();
            stations.add(nextStation);
            nextSection = findDownSection(nextStation);
        }
    }

    private Optional<Section> findDownSection(Station finalDownStation) {
        return sections.stream()
                .filter(it -> it.getUpStation() == finalDownStation)
                .findFirst();
    }

    private void verifyDeletable() {
        if (sections.size() <= 1) {
            throw new IllegalStateException("구간이 1개 이하 일시, 구간 제외를 할 수 없습니다.");
        }
    }

    private void createNewSection(Line line, Optional<Section> upSection, Optional<Section> downSection) {
        Station newUpStation = downSection.get().getUpStation();
        Station newDownStation = upSection.get().getDownStation();
        int newDistance = upSection.get().getDistance() + downSection.get().getDistance();
        sections.add(new Section(line, newUpStation, newDownStation, newDistance));
    }

    private void removeSections(Optional<Section> upLineStation, Optional<Section> downLineStation) {
        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    private boolean isExistsStation(Station station) {
        List<Station> stations = findStationsOrderUpToDown();
        return stations.stream().anyMatch(it -> it == station);
    }

    private void verifyAddable(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new IllegalStateException("이미 등록된 구간 입니다.");
        }

        if (!isUpStationExisted && !isDownStationExisted) {
            throw new IllegalStateException("등록할 수 없는 구간 입니다.");
        }
    }

    private void changeDownStationIfExists(Section section, boolean isDownStationExisted) {
        if (isDownStationExisted) {
            findUpSection(section.getDownStation())
                    .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
        }
    }

    private void changeUpStationIfExists(Section section, boolean isUpStationExisted) {
        if (isUpStationExisted) {
            findDownSection(section.getUpStation())
                    .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
        }
    }
}
