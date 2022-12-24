package nextstep.subway.line.domain;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.exception.SubwayException;
import nextstep.subway.station.domain.Station;
import org.springframework.http.HttpStatus;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public Sections(Section section) {
        sections = Lists.newArrayList(section);
    }

    public void add(Section section) {
        validation(section);
        ifContainsStationAdd(section);
        sections.add(section);
    }

    private void validation(Section section) {
        if (section.isExistsSections(getStations())) {
            throw new SubwayException(HttpStatus.BAD_REQUEST, "이미 등록된 구간 입니다.");
        }

        if (!section.checkAnyIncludeStation(getStations())) {
            throw new SubwayException(HttpStatus.BAD_REQUEST, "등록할 수 없는 구간 입니다.");
        }
    }

    private void ifContainsStationAdd(Section newSection) {
        sections.stream()
                .filter(section -> section.containsStation(newSection))
                .findFirst()
                .ifPresent(section -> section.swapStation(newSection));
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        if(findFinalUpStation().isPresent()){
            addStations(stations);
        }

        return stations;
    }

    private void addStations(List<Station> stations){
        Station downStation = findFinalUpStation().get().getUpStation();
        stations.add(downStation);

        while (downStation != null) {
            downStation = ifPresentAddStation(downStation, stations);
        }
    }

    private Optional<Section> findFinalUpStation() {
        return sections.stream()
                .filter(section -> !section.anyDownStation(sections))
                .findFirst();
    }

    private Station ifPresentAddStation(Station upStation, List<Station> stations) {
        Optional<Section> nextLineStation = findSectionByUpStation(upStation);
        if(!nextLineStation.isPresent()){
            return null;
        }
        nextLineStation.ifPresent(section -> stations.add(section.getDownStation()));
        return nextLineStation.get().getDownStation();
    }


    private Optional<Section> findSectionByUpStation(Station upStation) {
        return sections.stream()
                .filter(it -> it.isUpStation(upStation))
                .findFirst();
    }

    private Optional<Section> findSectionByDownStation(Station downStation) {
        return sections.stream()
                .filter(it -> it.isDownStation(downStation))
                .findFirst();
    }

    public List<Section> getSections() {
        return sections;
    }

    public void removeStation(Station station) {
        validateSections();

        Optional<Section> upLineStation = findSectionByUpStation(station);
        Optional<Section> downLineStation = findSectionByDownStation(station);

        mergeSection(upLineStation,downLineStation);

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    private void mergeSection(Optional<Section> upLineStation, Optional<Section> downLineStation){
        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            sections.add(new Section(sections.get(0).getLine(), newUpStation, newDownStation, newDistance));
        }
    }

    public void validateSections() {
        if (sections.size() <= 1) {
            throw new SubwayException(HttpStatus.BAD_REQUEST, "구간이 1개밖에 없으면 삭제 할 수 없습니다");
        }
    }
}
