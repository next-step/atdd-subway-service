package nextstep.subway.favorite.domain;

import nextstep.subway.exception.BadRequestException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기 관련 기능")
class FavoriteTest {

    private Station 강남역;
    private Station 양재역;
    private Station 양재시민의숲;

    private Line 신분당선;

    private Member 사용자;

    @BeforeEach
    void setUp() {
        강남역 = Station.of(1L, "강남역");
        양재역 = Station.of(2L, "양재역");
        양재시민의숲 = Station.of(3L, "양재시민의숲");

        신분당선 = Line.of("신분당선", "bg-red-600", 강남역, 양재시민의숲, 10);
        신분당선.addSection(강남역, 양재역, 3);

        사용자 = Member.of("email@email.com", "password", 20);
    }

    @Test
    void 즐겨찾기를_등록한다() {
        Favorite favorite = Favorite.of(강남역, 양재역, 사용자);

        Assertions.assertThat(favorite).isNotNull();
    }

    @Test
    void 즐겨찾기를_등록할때_상행역과_하행역이_같으면_등록_실패() {
        ThrowableAssert.ThrowingCallable throwingCallable = () -> Favorite.of(강남역, 강남역, 사용자);

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(throwingCallable)
                .withMessage("상행역과 하행역이 같으면 등록할 수 없습니다.");
    }
}
