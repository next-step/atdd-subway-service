package nextstep.subway.line.domain;

import nextstep.subway.common.exception.CustomException;
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

    @Embedded
    private Distance distance;

    public Section() {
    }

    public Section(Line line, Station upStation, Station downStation, long distance) {
        validate(line, upStation, downStation);
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = new Distance(distance);
    }

    private void validate(Line line, Station upStation, Station downStation) {
        if (line == null) {
            throw new CustomException("구간의 노선 정보가 포함되어야합니다.");
        }
        if (upStation == null && downStation == null) {
            throw new CustomException("구간의 상행 또는 하행역 정보는 추가되어야합니다.");
        }
    }

    public Section merge(Section section) {
        return new Section(this.line, this.upStation, section.downStation, this.distance.plus(section.distance));
    }

    public void updateUpStation(Section section) {
        if (this.distance.isLessThanEqual(section.distance)) {
            throw new CustomException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.upStation = section.downStation;
        this.distance = new Distance(this.distance.minus(section.distance));
    }

    public void updateDownStation(Section section) {
        if (this.distance.isLessThanEqual(section.distance)) {
            throw new CustomException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.downStation = section.upStation;
        this.distance = new Distance(this.distance.minus(section.distance));
    }

    public boolean hasUpSection(Station station) {
        return this.downStation.equals(station);
    }

    public boolean hasDownSection(Station station) {
        return this.upStation.equals(station);
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

    public long getDistance() {
        return distance.getDistance();
    }
}
