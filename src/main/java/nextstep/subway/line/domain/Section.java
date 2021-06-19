package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;

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
    private SectionDistance distance;

    /**
     * 생성자
     */
    protected Section() {
    }

    //테스트용
    public Section(Long id, Line line, Station upStation, Station downStation, int distance) {
        this.id = id;
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = SectionDistance.create(distance);
    }

    private Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = SectionDistance.create(distance);
    }

    /**
     * 생성 메소드
     */
    public static Section create(Line line, Station upStation, Station downStation, int distance) {
        verifyAvailable(line, upStation, downStation);
        return new Section(line, upStation, downStation, distance);
    }

    private static void verifyAvailable(Line line, Station upStation, Station downStation) {
        if (Objects.isNull(line) || Objects.isNull(upStation) || Objects.isNull(downStation)) {
            throw new IllegalArgumentException("구간 생성정보가 충분하지 않습니다.");
        }
    }

    /**
     * 비즈니스 메소드
     */
    public void updateUpStation(Station station, int newDistance) {
        distance.updateDistance(newDistance);
        this.upStation = station;
    }

    public void updateDownStation(Station station, int newDistance) {
        distance.updateDistance(newDistance);
        this.downStation = station;
    }

    /**
     * 기타 메소드
     */
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
        return distance.getDistance();
    }
}
