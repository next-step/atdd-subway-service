package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class LineTest {

    private Line line;
    private Station upStation;
    private Station downStation;

    @BeforeEach
    public void setup() {
        upStation = new Station(1L, "당고개역");
        downStation = new Station(2L, "오이도역");

        line = new Line("4호선", "하늘색", upStation, downStation, 20, 0);
    }

    @DisplayName("노선명, 노선색갈 수정")
    @Test
    public void update() {
        String name = "2호선";
        String color = "파란색";

        line.update(new Line(name, color, 0));

        assertAll(
                () -> assertThat(line.getName().getValue()).isEqualTo(name),
                () -> assertThat(line.getColor().getValue()).isEqualTo(color)
        );
    }

    @DisplayName("역 사이 구간추가")
    @Test
    public void addLineSection_역_사이_구간_추가() {
        Station station = new Station(3L, "상록수역");
        int distance = line.getDistance();

        line.addLineSection(station, downStation, new Distance(10));

        assertAll(
                () -> assertThat(line.getSections().getValues()).hasSize(2),
                () -> assertThat(line.getDistance()).isEqualTo(distance)
        );
    }

    @DisplayName("상행역 구간 추가")
    @Test
    public void addLineSection_상행역_구간_추가() {
        Station station = new Station(3L, "상록수역");
        int newDistance = 10;
        int distance = line.getDistance() + newDistance;

        line.addLineSection(station, upStation, new Distance(newDistance));

        assertAll(
                () -> assertThat(line.getSections().getValues()).hasSize(2),
                () -> assertThat(line.getDistance()).isEqualTo(distance)
        );
    }

    @DisplayName("하행역 구간 추가")
    @Test
    public void addLineSection_하행역_구간_추가() {
        Station station = new Station(3L, "상록수역");
        int newDistance = 10;
        int distance = line.getDistance() + newDistance;

        line.addLineSection(station, upStation, new Distance(newDistance));

        assertAll(
                () -> assertThat(line.getSections().getValues()).hasSize(2),
                () -> assertThat(line.getDistance()).isEqualTo(distance)
        );
    }

    @DisplayName("구간추가 기존구간보다 거리가 크거나 같은 경우 에러발생")
    @ParameterizedTest
    @ValueSource(ints = {20, 21})
    public void addLineSection_구간거리_보다_크거나_같은_경우(int distance) {
        Station station = new Station(3L, "상록수역");

        assertThatThrownBy(() -> line.addLineSection(station, downStation, new Distance(distance)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상행역과 하행역 모두 등록되어 있는 경우 에러발생")
    @Test
    public void addLineSection_상행역_하행역_모두_등록되어_있는_경우() {
        assertThatThrownBy(() -> line.addLineSection(upStation, downStation, new Distance(10)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상행역과 하행역 모두 포함되지 않는 경우")
    @Test
    public void addLineSection_상행역_하행역_모두_포함되지_않는_경우() {
        Station upStation = new Station(3L, "상록수역");
        Station downStation = new Station(4L, "한대앞역");

        assertThatThrownBy(() -> line.addLineSection(upStation, downStation, new Distance(10)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("중간구간 제거")
    @Test
    public void removeStation_중간구간_제거() {
        Station station = new Station(3L, "상록수역");
        line.addLineSection(station, downStation, new Distance(10));

        line.removeStation(3L);

        assertAll(
                () -> assertThat(line.getStations()).hasSize(2),
                () -> assertThat(line.getSections().getValues()).hasSize(1),
                () -> assertThat(line.getDistance()).isEqualTo(20)
        );
    }

    @DisplayName("상행구간 제거")
    @Test
    public void removeStation_상행구간_제거() {
        Station station = new Station(3L, "상록수역");
        line.addLineSection(station, downStation, new Distance(10));

        line.removeStation(upStation.getId());

        assertAll(
                () -> assertThat(line.getStations()).hasSize(2),
                () -> assertThat(line.getSections().getValues()).hasSize(1),
                () -> assertThat(line.getDistance()).isEqualTo(10)
        );

    }

    @DisplayName("하행구간 제거")
    @Test
    public void removeStation_하행구간_제거() {
        Station station = new Station(3L, "상록수역");
        line.addLineSection(station, downStation, new Distance(10));

        line.removeStation(downStation.getId());

        assertAll(
                () -> assertThat(line.getStations()).hasSize(2),
                () -> assertThat(line.getSections().getValues()).hasSize(1),
                () -> assertThat(line.getDistance()).isEqualTo(10)
        );
    }

    @DisplayName("노선에 등록되지 않은 역 제거 시 애러발생")
    @Test
    public void removeStation_노선에_등록되지_않은_경우_에러발생() {
        assertThatThrownBy(() -> line.removeStation(3L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("구간이 하나인 경우 구간제거 시 에러발생")
    @Test
    public void removeStation_구간이_하나인_경우_구간제거_시_에러발생() {
        assertThatThrownBy(() -> line.removeStation(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
