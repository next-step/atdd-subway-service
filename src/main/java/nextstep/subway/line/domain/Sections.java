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
    public static final String MINIMUM_SECTIONS_SIZE_EXCEPTION_MESSAGE = "하나의 구간만 있을 경우 구간을 제거할 수 없다.";
    public static final int MINIMUM_SECTIONS_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        validate(section);

        if (getStations().isEmpty()) {
            this.sections.add(new Section(section.getLine(), section.getUpStation(), section.getDownStation(), section.getDistance()));
            return;
        }

        addSection(section);
    }

    public int size() {
        return this.sections.size();
    }

    public List<Section> getSections() {
        return this.sections;
    }

    public void removeLineStation(Line line, Station station) {
        if (this.sections.size() <= MINIMUM_SECTIONS_SIZE) {
            throw new RuntimeException(MINIMUM_SECTIONS_SIZE_EXCEPTION_MESSAGE);
        }

        boolean hasPreviousLineStation = hasPreviousSection(station);
        boolean hasNextLineStation = hasNextSection(station);

        if (hasPreviousLineStation && hasNextLineStation) {
            Section previousSection = findPreviousSection(station).orElseThrow(NoSuchElementException::new);
            Section nextSection = findNextSection(station).orElseThrow(NoSuchElementException::new);
            this.sections.add(new Section(line, previousSection.getUpStation(), nextSection.getDownStation(), previousSection.sumDistance(nextSection)));
        }

        if (hasPreviousLineStation) {
            this.sections.remove(findPreviousSection(station).orElseThrow(NoSuchElementException::new));
        }

        if (hasNextLineStation) {
            this.sections.remove(findNextSection(station).orElseThrow(NoSuchElementException::new));
        }
    }

    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return Collections.emptyList();
        }
        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (hasNextSection(downStation)) {
            downStation = findUpStationSection(downStation).getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private boolean hasNextSection(Station station) {
        if (station == null) {
            return false;
        }
        return findNextSection(station).isPresent();
    }

    private boolean hasPreviousSection(Station station) {
        if (station == null) {
            return false;
        }
        return findPreviousSection(station).isPresent();
    }

    private Optional<Section> findNextSection(Station station) {
        return this.sections.stream()
                .filter(it -> it.getUpStation().equals(station))
                .findFirst();
    }

    private Section findUpStationSection(Station station) {
        return this.sections.stream()
                .filter(it -> it.getUpStation().equals(station))
                .findFirst().orElseThrow(NoSuchElementException::new);
    }

    private void validate(Section section) {
        List<Station> stations = getStations();

        boolean isUpStationExisted = stations.stream().anyMatch(station -> Objects.equals(station, section.getUpStation()));
        boolean isDownStationExisted = stations.stream().anyMatch(it -> Objects.equals(it, section.getDownStation()));

        validateExist(isUpStationExisted, isDownStationExisted);
        validateNotExist(section, stations);
    }

    private Station findUpStation() {
        Station downStation = this.sections.get(0).getUpStation();

        while (hasPreviousSection(downStation)) {
            downStation = findPreviousSection(downStation).orElseThrow(NoSuchElementException::new).getUpStation();
        }

        return downStation;
    }

    private Optional<Section> findPreviousSection(Station station) {
        return this.sections.stream()
                .filter(it -> it.getDownStation().equals(station))
                .findFirst();
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

    private void addSection(Section section) {

        boolean isUpStationExisted = isUpStationExisted(section);
        boolean isDownStationExisted = isDownStationExisted(section);

        if (!isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException();
        }

        addBetweenDownSection(section, isUpStationExisted);
        addBetweenUpSection(section, isDownStationExisted);
    }

    private void addBetweenUpSection(Section section, boolean isDownStationExisted) {
        if (isDownStationExisted) {
            this.sections.stream()
                    .filter(it -> it.getDownStation().equals(section.getDownStation()))
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
            this.sections.add(new Section(section.getLine(), section.getUpStation(), section.getDownStation(), section.getDistance()));
        }
    }

    private void addBetweenDownSection(Section section, boolean isUpStationExisted) {
        if (isUpStationExisted) {
            this.sections.stream()
                    .filter(it -> it.getUpStation().equals(section.getUpStation()))
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
            this.sections.add(new Section(section.getLine(), section.getUpStation(), section.getDownStation(), section.getDistance()));
        }
    }

    private boolean isDownStationExisted(Section section) {
        return getStations().stream().anyMatch(it -> Objects.equals(it, section.getDownStation()));
    }

    private boolean isUpStationExisted(Section section) {
        return getStations().stream().anyMatch(station -> Objects.equals(station, section.getUpStation()));
    }
}
