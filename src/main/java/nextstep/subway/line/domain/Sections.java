package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.station.domain.Station;
import nextstep.subway.utils.StreamUtils;

@Embeddable
public class Sections {
    private static final String NOT_EXIST_FIRST_SECTION = "첫 번째 구간이 존재하지 않습니다.";
    private static final String CAN_NOT_DELETE_STATION_WHEN_ONLY_ONE_SECTIONS_MESSAGE = "노선의 구간이 1개인 경우 지하철 역을 삭제 할 수 없습니다.";

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {}

    private Sections(List<Section> sections) {
        this.sections = sections;
    }

    public static Sections createEmpty() {
        return new Sections(new ArrayList<>());
    }

    public static Sections from(List<Section> sections) {
        return new Sections(sections);
    }

    public void add(Section section) {
        sections.add(section);
    }

    public void remove(Section section) {
        validateHasOnlyOneSection();
        this.sections.remove(section);
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public int size() {
        return sections.size();
    }

    public Optional<Section> findByUpStation(Station station) {
        return StreamUtils.filterAndFindFirst(sections, section -> section.isSameUpStation(station));
    }

    public Optional<Section> findByDownStation(Station station) {
        return StreamUtils.filterAndFindFirst(sections, section -> section.isSameDownStation(station));
    }

    public Section findFirstSection() {
        List<Station> downStations = findDownStations();
        return StreamUtils.filterAndFindFirst(sections, section -> !downStations.contains(section.getUpStation()))
                          .orElseThrow(() -> new IllegalStateException(NOT_EXIST_FIRST_SECTION));
    }

    private List<Station> findUpStations() {
        return StreamUtils.mapToList(sections, Section::getUpStation);
    }

    private List<Station> findDownStations() {
        return StreamUtils.mapToList(sections, Section::getDownStation);
    }

    private void validateHasOnlyOneSection() {
        if (sections.size() <= 1) {
            throw new IllegalArgumentException(CAN_NOT_DELETE_STATION_WHEN_ONLY_ONE_SECTIONS_MESSAGE);
        }
    }
}
