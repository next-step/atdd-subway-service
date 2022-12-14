package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.line.exception.InvalidRemoveException;
import nextstep.subway.line.exception.NoRelateStationException;
import nextstep.subway.line.exception.SectionAlreadyExistException;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    public static Sections of(Section... sections) {
        return new Sections(Arrays.asList(sections));
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }
        List<Station> stations = new ArrayList<>();
        stations.add(findFirstStation());
        stations.addAll(downStationsInOrder());
        return stations;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void add(Line line, Station upStation, Station downStation, Distance distance) {
        boolean isUpStationExisted = sections.stream().map(Section::getUpStation)
            .anyMatch(it -> it == upStation);
        boolean isDownStationExisted = sections.stream().map(Section::getDownStation)
            .anyMatch(it -> it == downStation);

        validateNewSection(upStation, downStation, isUpStationExisted, isDownStationExisted);

        updateBetweenSection(upStation, downStation, distance, isUpStationExisted,
            isDownStationExisted);

        sections.add(new Section(line, upStation, downStation, distance));
    }

    private void updateBetweenSection(Station upStation, Station downStation, Distance distance,
        boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted) {
            findFromUpStation(upStation)
                .ifPresent(it -> it.updateUpStation(downStation, distance));
        }

        if (isDownStationExisted) {
            findFromDownStation(downStation)
                .ifPresent(it -> it.updateDownStation(upStation, distance));
        }
    }

    private void validateNewSection(Station upStation, Station downStation,
        boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new SectionAlreadyExistException();
        }

        List<Station> stations = getStations();
        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == upStation) && stations
            .stream().noneMatch(it -> it == downStation)) {
            throw new NoRelateStationException();
        }
    }

    public void remove(Line line, Station station) {
        validateSizeLessThanOne();

        Optional<Section> upLineStation = findFromUpStation(station);
        Optional<Section> downLineStation = findFromDownStation(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            mergeSection(line, upLineStation.get(), downLineStation.get());
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    private void mergeSection(Line line, Section newUpStation, Section newDownStation) {
        Distance newDistance = newUpStation.plusDistance(newDownStation.getDistance());
        sections.add(new Section(line, newDownStation.getUpStation(), newUpStation.getDownStation(),
            newDistance));
    }

    private void validateSizeLessThanOne() {
        if (sections.size() <= 1) {
            throw new InvalidRemoveException("해당 정류장 제거시 구간이 모두 사라집니다.");
        }
    }

    private Station findFirstStation() {
        List<Station> downStations = downStationsInOrder();
        return sections.stream()
            .map(Section::getUpStation)
            .filter(station -> !downStations.contains(station))
            .findFirst()
            .orElse(null);
    }

    private List<Station> downStationsInOrder() {
        return sections.stream()
            .sorted()
            .map(Section::getDownStation)
            .collect(Collectors.toList());
    }

    private Optional<Section> findFromUpStation(final Station station) {
        return sections.stream()
            .filter(it -> it.getUpStation() == station)
            .findFirst();
    }

    private Optional<Section> findFromDownStation(final Station station) {
        return sections.stream()
            .filter(it -> it.getDownStation() == station)
            .findFirst();
    }

    public boolean contains(Section section) {
        return sections.contains(section);
    }
}
