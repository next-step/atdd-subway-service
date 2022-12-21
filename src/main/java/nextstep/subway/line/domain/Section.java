package nextstep.subway.line.domain;

import java.util.List;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    private int distance;

    protected Section() {
    }

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Line getLine() {
        return line;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Long getUpStationId() {
        return upStation.getId();
    }

    public Long getDownStationId() {
        return downStation.getId();
    }

    public int getDistance() {
        return distance;
    }


    public boolean isDownStation(Station downStation) {
        return this.downStation.equals(downStation);
    }

    public boolean isUpStation(Station upStation) {
        return this.upStation.equals(upStation);
    }

    public boolean anyDownStation(List<Section> sections) {
        return sections.stream().anyMatch(it -> it.isDownStation(upStation));
    }

    public boolean isExistsSections(List<Station> stations) {
        return stations.contains(upStation) && stations.contains(downStation);
    }

    public boolean checkAnyIncludeStation(List<Station> stations) {
        return stations.contains(upStation) || stations.contains(downStation);
    }

    public boolean containsStation(Section newSection) {
        return upStation.equals(newSection.upStation) || downStation.equals(newSection.downStation);
    }

    public void swapStation(Section newSection) {
        validateDistance(newSection);
        this.distance -= newSection.distance;

        if (upStation.equals(newSection.upStation)) {
            this.upStation = newSection.downStation;
            return;
        }
        this.downStation = newSection.upStation;
    }

    private void validateDistance(Section newSection) {
        if (newSection.distance >= this.distance) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
    }
}
