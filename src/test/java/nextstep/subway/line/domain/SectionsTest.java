package nextstep.subway.line.domain;

import nextstep.subway.fixture.StationFixture;
import nextstep.subway.line.message.SectionMessage;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionsTest {

    private Line 이호선;
    private Station 교대역;
    private Station 강남역;
    private Station 선릉역;
    private Station 역삼역;
    private Station 삼성역;
    private Station 잠실역;

    @BeforeEach
    void setUp() {
        this.이호선 = new Line("2호선", "bg-red-600");
        this.교대역 = StationFixture.교대역;
        this.강남역 = StationFixture.강남역;
        this.선릉역 = StationFixture.선릉역;
        this.역삼역 = StationFixture.역삼역;
        this.삼성역 = StationFixture.삼성역;
        this.잠실역 = StationFixture.잠실역;

        // [교대약 4 강남역] - [강남역 5 선릉역] - [선릉역 5 역삼역]
        이호선.addSection(교대역, 강남역, 4);
        이호선.addSection(강남역, 선릉역, 5);
        이호선.addSection(선릉역, 역삼역, 5);
    }

    /**
     * Given 하행역이 동일한 구간이 주어진 경우
     * When 지하철 구간 추가시
     * Then 정상적으로 추가 된다
     */
    @DisplayName("지하철 구간 목록 추가 - 하행역이 동일한 구간이 주어진 경우")
    @Test
    void add_section_with_same_up_station_test() {
        // given
        // [교대약 4 강남역] - [강남역 5 선릉역] - [선릉역 5 역삼역]

        // when
        // [교대약 4 강남역] - [강남역 5 선릉역] - [선릉역 3 삼성역] - [삼성역 2 역삼역]
        이호선.addSection(선릉역, 삼성역, 3);

        // then
        assertThat(이호선.getStations()).containsExactly(교대역, 강남역, 선릉역, 삼성역, 역삼역);
    }

    /**
     * Given 상행역이 동일한 구간이 주어진 경우
     * When 지하철 구간 추가시
     * Then 정상적으로 추가 된다
     */
    @DisplayName("지하철 구간 목록 추가 - 상행역이 동일한 구간이 주어진 경우")
    @Test
    void add_section_with_same_down_station_test() {
        // given
        // [교대약 4 강남역] - [강남역 5 선릉역] - [선릉역 5 역삼역]

        // when
        // [교대약 4 강남역] - [강남역 2 삼성역] - [삼성역 3 선릉역] -[선릉역 5 역삼역]
        이호선.addSection(삼성역, 선릉역, 3);

        // then
        assertThat(이호선.getStations()).containsExactly(교대역, 강남역, 삼성역, 선릉역, 역삼역);
    }

    /**
     * Given 라인에 등록 안된 상행역와 하행역을 가진 구간이 주어진 경우
     * When 지하철 구간 추가시
     * Then 예외처리 된다
     */
    @DisplayName("지하철 구간 목록 추가 예외처리 - 라인에 등록 안된 상행역와 하행역을 가진 구간이 주어진 경우")
    @Test
    void add_section_with_not_enrolled_stations_test() {
        // when & then
        assertThatThrownBy(() -> 이호선.addSection(삼성역, 잠실역,3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(SectionMessage.ADD_ERROR_NONE_MATCH_SECTION_STATIONS.message());
    }

    /**
     * Given 라인에 이미 등록 된 상행역과 하행역을 가진 구간이 주어진 경우
     * When 지하철 구간 추가시
     * Then 예외처리 된다
     */
    @DisplayName("지하철 구간 목록 추가 예외처리 - 라인에 이미 등록 된 상행역과 하행역을 가진 구간이 주어진 경우")
    @Test
    void add_section_with_enrolled_stations_test() {
        // when & then
        assertThatThrownBy(() -> 이호선.addSection(교대역, 강남역, 3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(SectionMessage.ADD_ERROR_ALREADY_ENROLLED_STATIONS.message());
    }

    /**
     * When 상행 종점역 제거시
     * Then 정상적으로 제거 된다
     */
    @DisplayName("지하철역 제거 - 라인에 등록 된 역을 제거하는 경우")
    @Test
    void remove_station_test() {
        // given
        // [교대약 4 강남역] - [강남역 5 선릉역] - [선릉역 5 역삼역]

        // when
        이호선.removeStation(선릉역);

        // then
        assertThat(이호선.getStations()).containsExactly(교대역, 강남역, 역삼역);
    }

    /**
     * Given 구간이 1개인 라인이 주어진 경우
     * When 역 제거시
     * Then 예외 처리 된다
     */
    @DisplayName("지하철역 제거 예외 처리- 구간이 1개인 경우")
    @Test
    void remove__station_if_has_one_section_test() {
        // given
        이호선.removeStation(선릉역);
        이호선.removeStation(강남역);

        // when & then
        assertThatThrownBy(() -> 이호선.removeStation(교대역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(SectionMessage.REMOVE_ERROR_MORE_THAN_TWO_SECTIONS.message());
    }
}
