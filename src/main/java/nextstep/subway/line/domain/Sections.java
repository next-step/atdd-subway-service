package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    private static final int MINIMUM_SECTION_COUNT = 1;
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section){
        sections.add(section);
    }

    public void updateSection(Station upStation, Station downStation, int distance){
        boolean isUpStationExisted = isExistsStation(upStation);
        boolean isDownStationExisted = isExistsStation(downStation);

        validateStation(isUpStationExisted, isDownStationExisted);

        if (isUpStationExisted) {
            updateUpSection(upStation, downStation, distance);
        }
        if (isDownStationExisted) {
            updateDownSection(upStation, downStation, distance);
        }
    }

    private boolean isExistsStation(Station station){
        return getStations().stream().anyMatch(it -> it == station);
    }

    private void validateStation(boolean isUpStationExisted, boolean isDownStationExisted){
        if (!isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    public void updateUpSection(Station upStation, Station downStation, int distance){
        sections.stream()
            .filter(it -> it.getUpStation() == upStation)
            .findFirst()
            .ifPresent(it -> it.updateUpStation(downStation, distance));
    }

    private void updateDownSection(Station upStation, Station downStation, int distance){
        sections.stream()
            .filter(it -> it.getDownStation() == downStation)
            .findFirst()
            .ifPresent(it -> it.updateDownStation(upStation, distance));
    }

    public void removeSection(Line line, Station station){
        validateLastSection();

        Optional<Section> upLineStation = getUpLineStation(station);
        Optional<Section> downLineStation = getDownLineStation(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            createNewSection(line, upLineStation.get(), downLineStation.get());
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    private void validateLastSection() {
        if (isLastSection()) {
            throw new RuntimeException();
        }
    }

    private boolean isLastSection(){
        return sections.size() <= MINIMUM_SECTION_COUNT;
    }

    private void createNewSection(Line line, Section upLineStation, Section downLineStation) {
        Station newUpStation = downLineStation.getUpStation();
        Station newDownStation = upLineStation.getDownStation();
        int newDistance = upLineStation.getDistance() + downLineStation.getDistance();
        sections.add(new Section(line, newUpStation, newDownStation, newDistance));
    }

    public List<Station> getStations() {
        if (hasNotSection()) {
            return Arrays.asList();
        }

        return new ArrayList<>(getOrderStations(findUpStation()));
    }

    private boolean hasNotSection(){
        return sections.isEmpty();
    }

    private Optional<Section> getUpLineStation(Station station){
        return sections.stream()
            .filter(it -> it.getUpStation() == station)
            .findFirst();
    }

    private Optional<Section> getDownLineStation(Station station){
        return sections.stream()
            .filter(it -> it.getDownStation() == station)
            .findFirst();
    }

    private List<Station> getOrderStations(Station downStation) {
        List<Station> stations = new ArrayList<>();
        do{
            stations.add(downStation);
            Station finalDownStation = downStation;
            downStation = findNextDownStation(finalDownStation);
        } while (downStation != null);
        return stations;
    }

    private Station findNextDownStation(Station station){
        Optional<Section> nextLineStation = getUpLineStation(station);
        return nextLineStation.map(Section::getDownStation).orElse(null);
    }

    private Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = getDownLineStation(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    public List<Section> getSections() {
        return sections;
    }
}
