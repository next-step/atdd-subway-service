package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
public class Sections {

    public static final String ALREADY_EXIST_SECTION_EXCEPTION_MESSAGE = "이미 등록된 구간 입니다.";
    public static final String NOT_EXIST_EXCEPTION_MESSAGE = "등록할 수 없는 구간 입니다.";
    public static final String MININUM_SECTIONS_SIZE_EXCEPTION_MESSAGE = "하나의 구간만 있을 경우 구간을 제거할 수 없다.";

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        validate(section);

        boolean isUpStationExisted = isUpStationExisted(section);
        boolean isDownStationExisted = isDownStationExisted(section);

        if (getStations().isEmpty()) {
            this.sections.add(new Section(section.getLine(), section.getUpStation(), section.getDownStation(), section.getDistance()));
            return;
        }

        if (isUpStationExisted) {
            getSections().stream()
                    .filter(it -> it.getUpStation().equals(section.getUpStation()))
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));

            this.sections.add(new Section(section.getLine(), section.getUpStation(), section.getDownStation(), section.getDistance()));
        } else if (isDownStationExisted) {
            getSections().stream()
                    .filter(it -> it.getDownStation().equals(section.getDownStation()))
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));

            this.sections.add(new Section(section.getLine(), section.getUpStation(), section.getDownStation(), section.getDistance()));
        } else {
            throw new RuntimeException();
        }
    }

    private boolean isDownStationExisted(Section section) {
        return getStations().stream().anyMatch(it -> Objects.equals(it, section.getDownStation()));
    }

    private boolean isUpStationExisted(Section section) {
        return getStations().stream().anyMatch(station -> Objects.equals(station, section.getUpStation()));
    }

    private void validate(Section section) {
        List<Station> stations = getStations();

        boolean isUpStationExisted = stations.stream().anyMatch(station -> Objects.equals(station, section.getUpStation()));
        boolean isDownStationExisted = stations.stream().anyMatch(it -> Objects.equals(it, section.getDownStation()));

        validateExist(isUpStationExisted, isDownStationExisted);
        validateNotExist(section, stations);
    }

    private static void validateNotExist(Section section, List<Station> stations) {
        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it.equals(section.getUpStation())) &&
                stations.stream().noneMatch(it -> it.equals(section.getDownStation()))) {
            throw new RuntimeException(NOT_EXIST_EXCEPTION_MESSAGE);
        }
    }

    private static void validateExist(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException(ALREADY_EXIST_SECTION_EXCEPTION_MESSAGE);
        }
    }

    public int size() {
        return this.sections.size();
    }

    public List<Section> getSections() {
        return this.sections;
    }

    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return Collections.emptyList();
        }
        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = getSections().stream()
                    .filter(it -> it.getUpStation().equals(finalDownStation))
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Station findUpStation() {
        Station downStation = getSections().get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = getSections().stream()
                    .filter(it -> it.getDownStation().equals(finalDownStation))
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    public void removeLineStation(Line line, Station station) {
        if (getSections().size() <= 1) {
            throw new RuntimeException(MININUM_SECTIONS_SIZE_EXCEPTION_MESSAGE);
        }

        Optional<Section> upLineStation = getSections().stream()
                .filter(it -> it.getUpStation().equals(station))
                .findFirst();
        Optional<Section> downLineStation = getSections().stream()
                .filter(it -> it.getDownStation().equals(station))
                .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            this.sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(it -> getSections().remove(it));
        downLineStation.ifPresent(it -> getSections().remove(it));
    }
}
