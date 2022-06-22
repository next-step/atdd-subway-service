package nextstep.subway.navigation.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.navigation.dto.NavigationResponse;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static nextstep.subway.common.Messages.NOT_CONNECTED_SOURCE_TARGET_STATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class NavigationTest {

    private LoginMember 사용자;
    private Line 지하철_2호선;
    private Line 지하철_4호선;
    private Station 강남역;
    private Station 역삼역;
    private Station 사당역;
    private Station 이수역;
    private int 거리 = 10;

    @BeforeEach
    void setUp() {
        지하철_2호선 = new Line("2호선", "bg-green-600");
        지하철_4호선 = new Line("4호선", "bg-blue-600");
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        사당역 = new Station("사당역");
        이수역 = new Station("이수역");

        지하철_2호선.addSection(강남역, 역삼역, 거리);
        지하철_4호선.addSection(사당역, 이수역, 거리);

        사용자 = new LoginMember(1L, "14km@github.com", 20);
    }

    @Test
    @DisplayName("지하철 최단 경로 조회 기능 테스트")
    void findShortest() {
        List<Line> 지하철_목록 = Collections.singletonList(지하철_2호선);
        Navigation 지하철_네비게이션 = Navigation.of(지하철_목록);

        NavigationResponse 지하철_네비게이션_결과 = 지하철_네비게이션.findShortest(강남역, 역삼역, 사용자);

        Assertions.assertAll(
                () -> assertThat(지하철_네비게이션_결과.getDistance()).isEqualTo(10),
                () -> assertThat(지하철_네비게이션_결과.getStations()).contains(강남역, 역삼역),
                () -> assertThat(지하철_네비게이션_결과.getFare()).isEqualTo(1_250)
        );
    }

    @Test
    @DisplayName("지하철 최단 경로 조회시 없는 역정보 조회 실패 테스트")
    void findShortestNotConnected() {
        List<Line> 지하철_목록 = Arrays.asList(지하철_2호선, 지하철_4호선);
        Navigation 지하철_네비게이션 = Navigation.of(지하철_목록);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> 지하철_네비게이션.findShortest(강남역, 사당역, 사용자))
                .withMessageContaining(NOT_CONNECTED_SOURCE_TARGET_STATION);
    }
}
