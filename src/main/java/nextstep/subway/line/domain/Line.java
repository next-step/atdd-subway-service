package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.Optional;
import nextstep.subway.BaseEntity;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    private static final int MINIMUM_SECTION_COUNT = 1;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public static Line to(LineRequest request, Station upStation, Station downStation){
        return new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance());
    }

    public void update(LineRequest request){
        this.name = request.getName();
        this.color = request.getColor();
    }

    public void addSection(Station upStation, Station downStation, int distance){
        boolean isUpStationExisted = isExistsUpStation(upStation);
        boolean isDownStationExisted = isExistsDownStation(downStation);

        validateStation(isUpStationExisted, isDownStationExisted);

        if (isUpStationExisted) {
            updateUpSection(upStation, downStation, distance);
        }
        if (isDownStationExisted) {
            updateDownSection(upStation, downStation, distance);
        }

        sections.add(new Section(this, upStation, downStation, distance));
    }
    private boolean isExistsUpStation(Station upStation){
        return getStations().stream().anyMatch(it -> it == upStation);
    }

    private boolean isExistsDownStation(Station downStation){
        return getStations().stream().anyMatch(it -> it == downStation);
    }
    private void validateStation(boolean isUpStationExisted, boolean isDownStationExisted){
        if (!isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    private void updateUpSection(Station upStation, Station downStation, int distance){
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

    public void removeSection(Station station){
        validateLastSection();

        Optional<Section> upLineStation = getUpLineStation(station);
        Optional<Section> downLineStation = getDownLineStation(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            createNewSection(upLineStation.get(), downLineStation.get());
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
        return this.getSections().size() <= MINIMUM_SECTION_COUNT;
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

    private void createNewSection(Section upLineStation, Section downLineStation) {
        Station newUpStation = downLineStation.getUpStation();
        Station newDownStation = upLineStation.getDownStation();
        int newDistance = upLineStation.getDistance() + downLineStation.getDistance();
        sections.add(new Section(this, newUpStation, newDownStation, newDistance));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getStations() {
        if (hasNotSection()) {
            return Arrays.asList();
        }

        return new ArrayList<>(getOrderStations(findUpStation()));
    }

    private boolean hasNotSection(){
        return this.getSections().isEmpty();
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
}
