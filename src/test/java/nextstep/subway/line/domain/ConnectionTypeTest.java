package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("지하철 구간 연결 종류를 결정하는 클래스 테스트")
class ConnectionTypeTest {

    private Line 이호선;
    private Station 교대역;
    private Station 강남역;
    private Station 역삼역;
    private Station 선릉역;

    @BeforeEach
    void setUp() {
        이호선 = new Line("2호선", "bg-green-600", 0);
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        선릉역 = new Station("선릉역");
    }

    @Test
    void 현재_구간의_상행역과_요청_구간의_하행역이_같으면_상행_종점역() {
        Section current = new Section(이호선, 강남역, 역삼역, 10);
        Section request = new Section(이호선, 교대역, 강남역, 10);

        assertEquals(ConnectionType.FIRST, ConnectionType.match(current, request));
    }

    @Test
    void 현재_구간의_상행역과_요청_구간의_상행역이_같으면_역_사이() {
        Section current = new Section(이호선, 강남역, 선릉역, 10);
        Section request = new Section(이호선, 강남역, 역삼역, 10);

        assertEquals(ConnectionType.MIDDLE, ConnectionType.match(current, request));
    }

    @Test
    void 현재_구간의_하행역과_요청_구간의_하행역이_같으면_역_사이() {
        Section current = new Section(이호선, 강남역, 선릉역, 10);
        Section request = new Section(이호선, 역삼역, 선릉역, 10);

        assertEquals(ConnectionType.MIDDLE, ConnectionType.match(current, request));
    }

    @Test
    void 현재_구간의_하행역과_요청_구간의_상행역이_같으면_하행_종점역() {
        Section current = new Section(이호선, 강남역, 역삼역, 10);
        Section request = new Section(이호선, 역삼역, 선릉역, 10);

        assertEquals(ConnectionType.LAST, ConnectionType.match(current, request));
    }
}
