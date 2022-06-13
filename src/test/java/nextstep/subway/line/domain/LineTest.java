package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class LineTest {

    private Station 강남역;
    private Station 광교역;
    private Line 신분당선;

    @BeforeEach
    public void setUp() {
        // given
        강남역 = new Station("강남역");
        광교역 = new Station("광교역");
        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 광교역, 10);
    }

    @DisplayName("신분당선의 지하철역 목록을 조회한다.")
    @Test
    void getStations() {
        // when
        List<Station> actual = 신분당선.getStations();

        // then
        assertThat(actual).containsExactly(강남역, 광교역);
    }

    @DisplayName("판교역-광교역 구간을 등록 시, 등록에 성공한다.")
    @Test
    void addSection() {
        // given
        Station 판교역 = new Station("판교역");

        // when
        신분당선.addSection(판교역, 광교역, 5);

        // then
        List<Station> stations = 신분당선.getStations();
        assertThat(stations).hasSize(3);
        assertThat(stations).containsExactly(강남역, 판교역, 광교역);
    }

    @DisplayName("강남역-광교역 구간을 등록 시, 등록에 실패한다.")
    @Test
    void invalid_addSection_existsSection() {
        // when & then
        assertThatThrownBy(() -> {
            신분당선.addSection(강남역, 광교역, 5);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 등록된 구간 입니다.");
    }

    @DisplayName("선릉역-사당역 구간을 등록 시, 등록에 실패한다.")
    @Test
    void invalid_addSection_notConnectedSection() {
        // given
        Station 선릉역 = new Station("선릉역");
        Station 사당역 = new Station("사당역");

        // when & then
        assertThatThrownBy(() -> {
            신분당선.addSection(선릉역, 사당역, 5);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("등록할 수 없는 구간 입니다.");
    }

    @DisplayName("강남역-판교역-광교역인 신분당선에서 광교역을 삭제하면, 광교역이 삭제된다.")
    @Test
    void removeStation() {
        // given
        Station 판교역 = new Station("판교역");
        신분당선.addSection(판교역, 광교역, 5);

        // when
        신분당선.removeStation(광교역);

        // then
        assertThat(신분당선.getStations())
                .hasSize(2)
                .containsExactly(강남역, 판교역);
    }

    @DisplayName("구간이 없는 노선에서 광교역을 제거하면, 제거에 실패한다.")
    @Test
    void invalid_removeStation_zeroSection() {
        // given
        Line 새로운노선 = new Line("새로운노선", "green");

        // when & then
        assertThatThrownBy(() -> {
            새로운노선.removeStation(광교역);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("구간이 없는 노선입니다.");
    }
}
