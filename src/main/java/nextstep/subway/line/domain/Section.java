package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Section {
    private static final String LONGER_EXISTING_SECTION_EXCEPTION = "역과 역 사이의 거리보다 좁은 거리를 입력해주세요";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    private int distance;

    public Section() {
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

    public Station upStation() {
        return upStation;
    }

    public Station downStation() {
        return downStation;
    }

    public int distance() {
        return distance;
    }

    public void updateUpStation(Section newSection) {
        checkDistance(newSection.distance());
        this.upStation = newSection.downStation();
        this.distance -= newSection.distance();
    }

    public void updateDownStation(Section newSection) {
        checkDistance(newSection.distance());
        this.downStation = newSection.upStation();
        this.distance -= newSection.distance();
    }

    private void checkDistance(int distance){
        if (this.distance < distance) {
            throw new IllegalArgumentException(LONGER_EXISTING_SECTION_EXCEPTION);
        }
    }

    public boolean isEqualsUpStation(Station inputUpStation) {
        return upStation.equals(inputUpStation);
    }

    public boolean isEqualsDownStation(Station inputDownStation) {
        return downStation.equals(inputDownStation);
    }
}
