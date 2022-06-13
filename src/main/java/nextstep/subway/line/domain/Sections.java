package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true)
    private List<Section> sectionList = new ArrayList<>();

    public Sections() {
    }

    public Sections(Section section) {
        this.sectionList.add(section);
    }

    public List<Section> getAll() {
        return this.sectionList;
    }

    public void add(Section section) {
        this.sectionList.add(section);
    }

    public List<Station> getStations() {
        if (this.sectionList.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation =
                    this.sectionList.stream().filter(it -> it.getUpStation() == finalDownStation).findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Station findUpStation() {
        Station downStation = this.sectionList.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation =
                    this.sectionList.stream().filter(it -> it.getDownStation() == finalDownStation).findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    public void removeLineStation(Line line, Station station) {
        if (this.sectionList.size() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> upLineStation =
                this.sectionList.stream().filter(it -> it.getUpStation() == station).findFirst();
        Optional<Section> downLineStation =
                this.sectionList.stream().filter(it -> it.getDownStation() == station).findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            this.sectionList.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(it -> this.sectionList.remove(it));
        downLineStation.ifPresent(it -> this.sectionList.remove(it));
    }
}
