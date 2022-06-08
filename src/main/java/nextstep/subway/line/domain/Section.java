package nextstep.subway.line.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.subway.line.domain.exception.CannotAddSectionException;
import nextstep.subway.station.domain.Station;

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

    public static Section createMergedSection(Section firstSection, Section secondSection) {
        if(firstSection.downStation != secondSection.upStation){
            throw new IllegalArgumentException("인접한 두 역끼리만 합칠 수 있습니다.");
        }
        if(firstSection.line != secondSection.line){
            throw new IllegalArgumentException("동일한 노선에 속한 구간만 합칠 수 있습니다.");
        }
        return new Section(firstSection.line, firstSection.upStation, secondSection.downStation, firstSection.distance + secondSection.distance);
    }

    public boolean isPrevOf(Section another){
        return this.downStation == another.upStation;
    }

    public boolean isNextOf(Section another){
        return this.upStation == another.downStation;
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

    public int getDistance() {
        return distance;
    }

    void updateUpStation(Station station, int newDistance) {
        if (this.distance <= newDistance) {
            throw new CannotAddSectionException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.upStation = station;
        this.distance -= newDistance;
    }

    void updateDownStation(Station station, int newDistance) {
        if (this.distance <= newDistance) {
            throw new CannotAddSectionException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.downStation = station;
        this.distance -= newDistance;
    }
}
