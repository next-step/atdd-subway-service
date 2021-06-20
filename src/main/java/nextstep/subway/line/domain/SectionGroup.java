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

    public void addSection(Line line, Station upStation, Station downStation, int distance) {
        if (!sections.isEmpty()) {
            boolean isUpStationExisted = isExistsStation(upStation);
            boolean isDownStationExisted = isExistsStation(downStation);

            verifyAddable(isUpStationExisted, isDownStationExisted);
            changeUpStationIfExists(upStation, downStation, distance, isUpStationExisted);
            changeDownStationIfExists(upStation, downStation, distance, isDownStationExisted);
        }

        sections.add(Section.create(line, upStation, downStation, distance));
    }

    public void removeSection(Line line, Station station) {
        verifyDeletable();

        Optional<Section> upLineStation = findDownSection(station);
        Optional<Section> downLineStation = findUpSection(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            createNewSection(line, upLineStation, downLineStation);
        }

        removeSections(upLineStation, downLineStation);
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

    private void createNewSection(Line line, Optional<Section> upLineStation, Optional<Section> downLineStation) {
        Station newUpStation = downLineStation.get().getUpStation();
        Station newDownStation = upLineStation.get().getDownStation();
        int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
        sections.add(Section.create(line, newUpStation, newDownStation, newDistance));
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

    private void changeDownStationIfExists(Station upStation, Station downStation, int distance, boolean isDownStationExisted) {
        if (isDownStationExisted) {
            findUpSection(downStation).ifPresent(it -> it.updateDownStation(upStation, distance));
        }
    }

    private void changeUpStationIfExists(Station upStation, Station downStation, int distance, boolean isUpStationExisted) {
        if (isUpStationExisted) {
            findDownSection(upStation).ifPresent(it -> it.updateUpStation(downStation, distance));
        }
    }
}
