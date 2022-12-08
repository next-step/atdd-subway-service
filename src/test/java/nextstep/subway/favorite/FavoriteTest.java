package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

class FavoriteTest {
    private final Station 강남역 = new Station("강남역");
    private final Station 광교역 = new Station("광교역");
    private final String EMAIL = "email@email.com";
    private final String OTHER_EMAIL = "otherEmail@email.com";
    private final String PASSWORD = "password";
    private final String OTHER_PASSWORD = "otherPassword";
    private final Integer AGE = 30;
    private final Member 사용자 = new Member(EMAIL, PASSWORD, AGE);
    private final Member 다른_사용자 = new Member(OTHER_EMAIL, OTHER_PASSWORD, AGE);

    @Test
    void 생성() {
        Favorite favorite = new Favorite(강남역, 광교역, 사용자);
        assertAll(
                () -> assertThat(favorite.getSource()).isEqualTo(강남역),
                () -> assertThat(favorite.getTarget()).isEqualTo(광교역),
                () -> assertThat(favorite.getMember()).isEqualTo(사용자)
        );
    }

    @Test
    void 역_데이터가_없는_경우_유효성_검증() {
        assertAll(
                () -> assertThatThrownBy(
                        () -> new Favorite(null, 광교역, 사용자)
                )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("역 데이터가 존재하지 않습니다."),

                () -> assertThatThrownBy(
                        () -> new Favorite(강남역, null, 사용자)
                )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("역 데이터가 존재하지 않습니다.")
        );
    }

    @Test
    void 사용자_데이터가_없는_경우_유효성_검증() {
        assertThatThrownBy(
                () -> new Favorite(강남역, 광교역, null)
        )
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("사용자 데이터가 존재하지 않습니다.");
    }

    @Test
    void 삭제_요청에서_사용자_검증() {
        Favorite favorite = new Favorite(강남역, 광교역, 사용자);
        assertThatThrownBy(() -> favorite.validateSameMember(다른_사용자)).isInstanceOf(RuntimeException.class);
    }
}