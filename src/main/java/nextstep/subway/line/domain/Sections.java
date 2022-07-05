package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.SectionsNotAddedException;
import nextstep.subway.SectionsNotRemovedException;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    private final int ONE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public List<Section> getSections() {
        return sections;
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public void add(Section section) {
        sections.add(section);
    }

    public void requestAddSection(Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }
        checkExistsStation(section, section.getUpStation(), section.getDownStation());
    }

    private void checkExistsStation(Section section, Station upStation, Station downStation) {
        boolean upMatchesLineUp = stationExistsInUp(upStation);
        boolean downMatchsLineDown = stationExistsInDown(downStation);
        boolean matchesAtFront = stationExistsInUp(downStation);
        boolean matchesAtBack = stationExistsInDown(upStation);
        if (!upMatchesLineUp && !matchesAtFront && !matchesAtBack && !downMatchsLineDown) {
            throw new SectionsNotAddedException("구간 중 어떠한 역도 현재 구간에 없습니다.");
        }
        if (upMatchesLineUp && downMatchsLineDown) {
            throw new SectionsNotAddedException("이미 등록된 구간 입니다.");
        }

        addSectionToMatchStation(section, upStation, downStation, upMatchesLineUp, downMatchsLineDown, matchesAtFront,
                matchesAtBack);

    }

    private void addSectionToMatchStation(Section section, Station upStation, Station downStation,
                                          boolean upMatchesLineUp, boolean downMatchsLineDown, boolean matchesAtFront,
                                          boolean matchesAtBack) {
        if (upMatchesLineUp) {
            addUpStationExists(section, upStation, downStation, section.getDistance());
            return;
        }
        if (downMatchsLineDown) {
            addDownStationExists(section, upStation, downStation, section.getDistance());
            return;
        }

        if (matchesAtFront || matchesAtBack) {
            sections.add(section);
        }
    }

    private boolean stationExistsInUp(Station station) {
        return sections.stream().anyMatch(section -> section.getUpStation().equals(station));
    }

    private boolean stationExistsInDown(Station station) {
        return sections.stream().anyMatch(section -> section.getDownStation().equals(station));
    }


    private void addUpStationExists(Section section, Station upStation, Station downStation, int distance) {
        sections.stream().filter(sectionIterate -> upStation.equals(sectionIterate.getUpStation())).findFirst()
                .ifPresent(sectionIterate -> sectionIterate.updateUpStation(downStation, distance));
        sections.add(section);
    }

    private void addDownStationExists(Section section, Station upStation, Station downStation, int distance) {
        sections.stream().filter(sectionIterate -> downStation.equals(sectionIterate.getDownStation())).findFirst()
                .ifPresent(it -> it.updateDownStation(upStation, distance));
        sections.add(section);
    }

    public void removeLineStation(Station station, Line line) {
        if (sections.size() <= ONE) {
            throw new SectionsNotRemovedException("현재 구간이 하나밖에 없습니다.");
        }

        Optional<Section> upLineStation = sections.stream().filter(it -> it.getUpStation() == station).findFirst();
        Optional<Section> downLineStation = sections.stream().filter(it -> it.getDownStation() == station).findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();

            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }
        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

}