package nextstep.subway.line.domain.section;

import nextstep.subway.exception.error.ErrorCode;
import nextstep.subway.exception.SectionException;
import nextstep.subway.line.domain.line.Line;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    private static final int SECTION_MIN_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public void addLineStation(Section section) {
        addValidation(section);

        ifEqualsUpdateUpStation(section);
        ifEqualsUpdateDownStation(section);

        sections.add(section);
    }

    private void ifEqualsUpdateDownStation(Section section) {
        sections.stream()
                .filter(it -> it.getDownStation() == section.getDownStation())
                .findFirst()
                .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
    }

    private void ifEqualsUpdateUpStation(Section section) {
        sections.stream()
                .filter(it -> it.getUpStation() == section.getUpStation())
                .findFirst()
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
    }

    private void addValidation(Section section) {
        validateDuplicateSection(section);
        validateNoneMatchSection(section);
    }

    private void validateDuplicateSection(Section section) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == section.getUpStation());
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == section.getDownStation());

        if (!stations.isEmpty() && isUpStationExisted && isDownStationExisted) {
            throw new SectionException(ErrorCode.ALREADY_EXIST_ENTITY, "이미 등록된 구간 입니다.");
        }
    }

    private void validateNoneMatchSection(Section section) {
        List<Station> stations = getStations();
        boolean isMatchUpStation = stations.stream().noneMatch(it -> it == section.getUpStation());
        boolean isMatchDownStation = stations.stream().noneMatch(it -> it == section.getDownStation());

        if (!stations.isEmpty() && isMatchUpStation && isMatchDownStation) {
            throw new SectionException(ErrorCode.ALREADY_EXIST_ENTITY, "연결되는 구간이 없습니다.");
        }
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }
        Set<Station> result = new LinkedHashSet<>();

        Optional<Section> first = sections.stream()
                .filter(f -> !firstStation(f.getUpStation()))
                .findFirst();

        while (first.isPresent()) {
            Section station = first.get();
            result.add(first.get().getUpStation());
            result.add(first.get().getDownStation());
            first = sections.stream()
                    .filter(f -> f.getUpStation() == station.getDownStation())
                    .findFirst();
        }
        return new ArrayList<>(result);
    }

    private boolean firstStation(Station station) {
        return sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList())
                .contains(station);
    }

    public List<Section> getSections() {
        return sections;
    }

    public void removeLineStation(Line line, Station station) {
        removeValidation(station);

        Section downLineStation = removeUpStation(station).orElse(Section.ofDummy());
        Section upLineStation = removeDownStation(station).orElse(Section.ofDummy());

        if (!upLineStation.isDummy() && !downLineStation.isDummy()) {
            line.getSections().add(newSection(line, upLineStation, downLineStation));
        }
    }

    private void removeValidation(Station station) {
        validateMinSize();
        validateNoneMatchStation(station);
    }

    private void validateMinSize() {
        if (sections.size() <= SECTION_MIN_SIZE) {
            throw new SectionException(ErrorCode.ALREADY_EXIST_ENTITY, "마지막 노선은 삭제 할 수 없습니다.");
        }
    }

    private void validateNoneMatchStation(Station station) {
        boolean isNoneMatch = getStations().stream().noneMatch(it -> it == station);
        if (isNoneMatch) {
            throw new SectionException(ErrorCode.ALREADY_EXIST_ENTITY, "삭제 대상 역이 없습니다.");
        }
    }

    private Optional<Section> removeUpStation(Station station) {
        Optional<Section> downLineStation = sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();

        downLineStation.ifPresent(it -> sections.remove(it));
        return downLineStation;
    }

    private Optional<Section> removeDownStation(Station station) {
        Optional<Section> upLineStation = sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();

        upLineStation.ifPresent(it -> sections.remove(it));
        return upLineStation;
    }

    private Section newSection(Line line, Section upLineStation, Section downLineStation) {
        Station newUpStation = downLineStation.getUpStation();
        Station newDownStation = upLineStation.getDownStation();
        int newDistance = Distance.concat(upLineStation.getDistance(), downLineStation.getDistance());
        return new Section(line, newUpStation, newDownStation, newDistance);
    }

}
