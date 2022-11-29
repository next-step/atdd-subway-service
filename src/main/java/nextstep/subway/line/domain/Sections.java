package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
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
            return Arrays.asList();
        }
        List<Station> stations = new ArrayList<>();
        stations.add(findFirstStation());
        stations.addAll(downStationsInOrder());
        return stations;
    }

    public void add(Line line, Station upStation, Station downStation, Distance distance) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == upStation);
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == downStation);

        if (isUpStationExisted && isDownStationExisted) {
            throw new SectionAlreadyExistException();
        }

        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == upStation) &&
            stations.stream().noneMatch(it -> it == downStation)) {
            throw new NoRelateStationException();
        }

        if (isUpStationExisted) {
            findFromUpStation(upStation)
                .ifPresent(it -> it.updateUpStation(downStation, distance));
        }

        if (isDownStationExisted) {
            findFromDownStation(downStation)
                .ifPresent(it -> it.updateDownStation(upStation, distance));
        }

        sections.add(new Section(line, upStation, downStation, distance));
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
}
