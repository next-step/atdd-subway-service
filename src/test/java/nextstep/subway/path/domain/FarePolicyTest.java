package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("요금정책 관련 기능")
public class FarePolicyTest {

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    LoginMember 어른;
    LoginMember 청소년;
    LoginMember 어린이;
    LoginMember 비회원;

    @BeforeEach
    void setUp() {
        강남역 = Station.of(1L, "강남역");
        양재역 = Station.of(2L, "양재역");
        교대역 = Station.of(3L, "교대역");

        신분당선 = Line.of("신분당선", "bg-red-600", 강남역, 양재역, 9, 900);
        이호선 = Line.of("이호선", "bg-red-600", 교대역, 강남역, 50, 0);
        삼호선 = Line.of("삼호선", "bg-red-600", 교대역, 양재역, 178, 0);

        어른 = LoginMember.of(1L, "email@gmail.com", 19);
        청소년 = LoginMember.of(1L, "email@gmail.com", 13);
        어린이 = LoginMember.of(1L, "email@gmail.com", 12);
        비회원 = LoginMember.fromGuest();
    }

    @DisplayName("노선의_추가된_요금_조회 " +
            "- 기본요금 (1250 + 900 = 2150)")
    @Test
    void 노선의_추가된_요금_조회() {
        // given
        Path path = Path.of(Arrays.asList(강남역, 양재역), 9, 신분당선.getSections());

        // when
        Fare actual = FarePolicy.of(비회원, path);

        // then
        assertAll(() -> {
            assertThat(actual).isNotNull();
            assertThat(actual).isEqualTo(Fare.from(2150));
        });
    }

    @DisplayName("총_거리가_10_50_사이인_경로의_추가요금_조회 " +
            "- 이용거리 50KM " +
            "- 기본요금 (1250 + 800 = 2,050원)")
    @Test
    void 총_거리가_10_50_사이인_경로의_추가요금_조회() {
        // given - when
        Path path = Path.of(Arrays.asList(교대역, 강남역), 50, 이호선.getSections());

        // when
        Fare actual = FarePolicy.of(비회원, path);

        // then
        assertAll(() -> {
            assertThat(actual).isNotNull();
            assertThat(actual).isEqualTo(Fare.from(2050));
        });
    }

    @DisplayName("총_거리가_50이상인_경로의_추가요금_조회 " +
            "- 이용거리 178KM " +
            "- 기본요금 (1250 + 2400 = 3,650원)")
    @Test
    void 총_거리가_50이상인_경로의_추가요금_조회() {
        // given - when
        Path path = Path.of(Arrays.asList(교대역, 양재역), 178, 삼호선.getSections());

        // when
        Fare actual = FarePolicy.of(비회원, path);

        // then
        assertAll(() -> {
            assertThat(actual).isNotNull();
            assertThat(actual).isEqualTo(Fare.from(3650));
        });
    }

    @DisplayName("로그인_사용자가_어린이이고_이용거리가_50KM_일때_추가요금_조회 " +
            "- 이용거리 50KM " +
            "- 기본요금 (1250 + 800 = 2,050원)" +
            "- 공제 (2,050 - 350 = 1,700원)" +
            "- 50% 할인 = 850원 ")
    @Test
    void 어린이의_이용거리가_50KM_일때_추가요금_조회() {
        // given
        Path path = Path.of(Arrays.asList(교대역, 강남역), 50, 이호선.getSections());

        // when
        Fare actual = FarePolicy.of(어린이, path);

        // then
        assertAll(() -> {
            assertThat(actual).isNotNull();
            assertThat(actual).isEqualTo(Fare.from(850));
        });
    }

    @DisplayName("로그인_사용자가_청소년이고_이용거리가_50KM_일때_추가요금_조회 " +
            "- 이용거리 50KM " +
            "- 기본요금 (1250 + 800 = 2,050원)" +
            "- 공제 (2,050 - 350 = 1,700원)" +
            "- 20% 할인 = 1,360원 ")
    @Test
    void 청소년의_이용거리가_50KM_일때_추가요금_조회() {
        // given
        Path path = Path.of(Arrays.asList(교대역, 강남역), 50, 이호선.getSections());

        // when
        Fare actual = FarePolicy.of(청소년, path);

        // then
        assertAll(() -> {
            assertThat(actual).isNotNull();
            assertThat(actual).isEqualTo(Fare.from(1360));
        });
    }

    @DisplayName("로그인_사용자가_비회원이고_이용거리가_50KM_일때_추가요금_조회 " +
            "- 이용거리 50KM " +
            "- 기본요금 (1250 + 800 = 2,050원)" +
            "- 공제 없음" +
            "- 할인 없음 = 2,050원 ")
    @Test
    void 비회원의_이용거리가_50KM_일때_추가요금_조회() {
        // given
        Path path = Path.of(Arrays.asList(교대역, 강남역), 50, 이호선.getSections());

        // when
        Fare actual = FarePolicy.of(비회원, path);

        // then
        assertAll(() -> {
            assertThat(actual).isNotNull();
            assertThat(actual).isEqualTo(Fare.from(2050));
        });
    }
}
