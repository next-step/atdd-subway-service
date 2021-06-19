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
public class SectionList {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    /**
     * 생성자
     */
    protected SectionList() {
    }

    public SectionList(List<Section> sections) {
        this.sections = sections;
    }

    /**
     * 비즈니스 메소드
     */
    public void addToCreateLine(Section section) {
        sections.add(section);
    }

    public void addSection(Line line, Station upStation, Station downStation, int distance) {
        boolean isUpStationExisted = isExistsStation(upStation);
        boolean isDownStationExisted = isExistsStation(downStation);

        verifyAddable(isUpStationExisted, isDownStationExisted);

        if (isUpStationExisted) {
            changeUpStation(upStation, downStation, distance);
        }

        if (isDownStationExisted) {
            changeDownStation(upStation, downStation, distance);
        }

        sections.add(Section.create(line, upStation, downStation, distance));
    }

    public void removeSection(Line line, Station station) {
        verifyDeletable();

        Optional<Section> upLineStation = sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
        Optional<Section> downLineStation = sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();

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
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
                    .filter(it -> it.getDownStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }
        return downStation;
    }

    private void iterateToAdd(List<Station> stations, Station downStation) {
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
                    .filter(it -> it.getUpStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }
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

    private void changeDownStation(Station upStation, Station downStation, int distance) {
        sections.stream()
                .filter(it -> it.getDownStation() == downStation)
                .findFirst()
                .ifPresent(it -> it.updateDownStation(upStation, distance));
    }

    private void changeUpStation(Station upStation, Station downStation, int distance) {
        sections.stream()
                .filter(it -> it.getUpStation() == upStation)
                .findFirst()
                .ifPresent(it -> it.updateUpStation(downStation, distance));
    }
}
