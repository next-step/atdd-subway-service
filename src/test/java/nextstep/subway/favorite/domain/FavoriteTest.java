package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteTest {
    private Member 사용자;
    private Station 강남역;
    private Station 양재역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        사용자 = new Member(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("즐겨찾기 객체를 빌더를 이용하여 생성한다")
    @Test
    void 즐겨찾기_생성() {
        // given
        Favorite favorite = Favorite.builder()
                .member(사용자)
                .source(강남역)
                .target(양재역)
                .build();

        // then
        assertThat(favorite.getMember()).isEqualTo(new Member(EMAIL, PASSWORD, AGE));
        assertThat(favorite.getSource()).isEqualTo(new Station("강남역"));
        assertThat(favorite.getTarget()).isEqualTo(new Station("양재역"));
    }
}
