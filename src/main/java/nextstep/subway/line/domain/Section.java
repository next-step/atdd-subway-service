package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Section {
    public static final String ERR_TEXT_NEED_TO_SHORT_DISTANCE_THAN_NOW = "역과 역 사이의 거리보다 좁은 거리를 입력해주세요";
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

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance;
    }

    public void updateUpStationByNewSection(final Section newSection) {
        final int newDistance = validateNewDistance(newSection);

        this.upStation = newSection.getDownStation();
        this.distance -= newDistance;
    }

    private int validateNewDistance(final Section newSection) {
        final int newDistance = newSection.getDistance();
        if (this.distance <= newDistance) {
            throw new IllegalArgumentException(ERR_TEXT_NEED_TO_SHORT_DISTANCE_THAN_NOW);
        }

        return newDistance;
    }

    public void updateDownStationByNewSection(final Section newSection) {
        final int newDistance = validateNewDistance(newSection);

        this.downStation = newSection.getUpStation();
        this.distance -= newDistance;
    }

    public boolean isMatchUpAndUpStation(final Section section) {
        return this.upStation == section.getUpStation();
    }

    public boolean isMatchDownAndDownStation(final Section section) {
        return this.downStation == section.getDownStation();
    }

    public boolean isMatchUpStation(final Station targetStation) {
        return this.upStation == targetStation;
    }

    public boolean isMatchDownStation(final Station targetStation) {
        return this.downStation == targetStation;
    }
}
