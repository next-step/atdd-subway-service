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
public class Sections {

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

    /**
     * 구간을 추가합니다.
     * @param section
     */
    public void add(Section section) {
        this.sections.add(section);
    }

    /**
     * 노선에 있는 모든 구간의 지하철 역을 상행종점부터 하행종점까지 순서대로 반환합니다.
     * @return 정렬된 전체 지하철역
     */
    public List<Station> getStations() {
        if (this.getSections().isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = this.getSections().stream()
                    .filter(it -> it.getUpStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    /**
     * 상행종점역을 찾습니다.
     * @return 상행종점역
     */
    private Station findUpStation() {
        Station downStation = this.getSections().get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = this.getSections().stream()
                    .filter(it -> it.getDownStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    /**
     * 구간을 추가합니다.
     * @param line
     * @param upStation
     * @param downStation
     * @param distance
     */
    public void addSectionByStation(Line line, Station upStation, Station downStation, int distance) {
        List<Station> stations = this.getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == upStation);
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == downStation);

        this.existStation(isUpStationExisted, isDownStationExisted);

        this.cannotRegisterStation(upStation, downStation, stations);

        if (stations.isEmpty()) {
            this.add(new Section(line, upStation, downStation, distance));
            return;
        }

        if (isUpStationExisted) {
            this.getSections().stream()
                    .filter(it -> it.getUpStation() == upStation)
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(downStation, distance));

            this.add(new Section(line, upStation, downStation, distance));
        } else if (isDownStationExisted) {
            this.getSections().stream()
                    .filter(it -> it.getDownStation() == downStation)
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(upStation, distance));

            this.add(new Section(line, upStation, downStation, distance));
        } else {
            throw new RuntimeException();
        }
    }

    /**
     * 이미 등록된 구간의 경우 예외 처리
     * @param isUpStationExisted
     * @param isDownStationExisted
     */
    private void existStation(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    /**
     * 등록 할 수 없는 구간인지 확인 후 예외 처리
     * 1. 지하철역이 비어있지 않고
     * 2. 지하철역 목록에 상행역이 없고
     * 3. 지하철역 목록에 하행역이 없는 경우
     * @param upStation 
     * @param downStation
     * @param stations
     */
    private void cannotRegisterStation(Station upStation, Station downStation, List<Station> stations) {
        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == upStation) &&
                stations.stream().noneMatch(it -> it == downStation)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }


    /**
     * 해당 역이 있는 구간을 삭제합니다.
     * 해당 역이 있는 구간이 2개인 경우 (즉, 중간에 있는 역인 경우) 하나는 지우고, 하나는 두 구간을 합쳐서 저장합니다.
     * @param line
     * @param station
     */
    public void removeSectionByStation(Line line, Station station) {
        this.validateSectionsSize();

        Optional<Section> upLineStation = this.getSections().stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
        Optional<Section> downLineStation = this.getSections().stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();

        this.combineStations(line, upLineStation, downLineStation);

        upLineStation.ifPresent(it -> this.getSections().remove(it));
        downLineStation.ifPresent(it -> this.getSections().remove(it));
    }

    /**
     * 구간의 갯수가 2개 이상이 아닌 경우 예외처리합니다.
     */
    private void validateSectionsSize() {
        if (this.getSections().size() <= 1) {
            throw new RuntimeException();
        }
    }

    /**
     * 해당 역이 있는 구간이 2개인 경우 (즉, 중간에 있는 역인 경우) 두 구간을 합쳐서 저장합니다.
     * @param line
     * @param upLineStation
     * @param downLineStation
     */
    private void combineStations(Line line, Optional<Section> upLineStation, Optional<Section> downLineStation) {
        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            this.add(new Section(line, newUpStation, newDownStation, newDistance));
        }
    }
}
