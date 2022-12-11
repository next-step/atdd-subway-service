package nextstep.subway.member.domain;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class MemberTest {

    @DisplayName("이메일이 없으면 사용자 생성에 실패한다")
    @Test
    void 이메일이_없으면_사용자_생성에_실패한다() {
        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Member(null, "password", 30));
    }

    @DisplayName("비밀번호가 없으면 사용자 생성에 실패한다")
    @Test
    void 비밀번호가_없으면_사용자_생성에_실패한다() {
        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Member("email@email.com", null, 30));
    }

    @DisplayName("나이가 없으면 사용자 생성에 실패한다")
    @Test
    void 나이가_없으면_사용자_생성에_실패한다() {
        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Member("email@email.com", "password", null));
    }

    @DisplayName("사용자 생성 성공")
    @Test
    void 사용자_생성_성공() {
        // given
        Member 사용자 = new Member("email@email.com", "password", 30);

        // when, then
        assertThat(사용자.getEmail()).isEqualTo("email@email.com");
        assertThat(사용자.getPassword()).isEqualTo("password");
        assertThat(사용자.getAge()).isEqualTo(30);
    }

    @DisplayName("사용자 비밀번호가 일치하지 않으면 예외가 발생한다.")
    @Test
    void 사용자_비밀번호_비교() {
        // given
        Member 사용자 = new Member("email@email.com", "password", 30);

        // when, then
        assertThatThrownBy(() -> 사용자.checkPassword("wrong_password"))
                .isInstanceOf(AuthorizationException.class);
    }

    @DisplayName("사용자 정보를 수정한다.")
    @Test
    void 사용자_정보_수정() {
        // given
        Member 사용자 = new Member("email@email.com", "password", 30);

        // when
        사용자.update(new Member("new_email@email.com", "new_password", 40));

        // then
        assertThat(사용자.getEmail()).isEqualTo("new_email@email.com");
        assertThat(사용자.getPassword()).isEqualTo("new_password");
        assertThat(사용자.getAge()).isEqualTo(40);
    }

    @DisplayName("사용자의 즐겨찾기를 추가한다.")
    @Test
    void 사용자_즐겨찾기_추가() {
        // given
        Member 사용자 = new Member("email@email.com", "password", 30);
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");

        // when
        Favorite 즐겨찾기 = 사용자.addFavorite(강남역, 양재역);

        // then
        assertThat(사용자.getFavorites().values()).hasSize(1);
        assertThat(사용자.getFavorites().values()).containsExactly(즐겨찾기);
    }

    @DisplayName("등록되지 않은 즐겨찾기를 삭제하면 예외가 발생한다.")
    @Test
    void 사용자_즐겨찾기_삭제_실패() {
        // given
        Member 사용자 = new Member("email@email.com", "password", 30);
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Favorite 즐겨찾기 = Favorite.builder()
                .source(강남역)
                .target(양재역)
                .build();

        // when, then
        assertThatThrownBy(() -> 사용자.removeFavorite(즐겨찾기))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("사용자의 즐겨찾기를 삭제한다.")
    @Test
    void 사용자_즐겨찾기_삭제() {
        // given
        Member 사용자 = new Member("email@email.com", "password", 30);
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Favorite 즐겨찾기 = 사용자.addFavorite(강남역, 양재역);

        // when
        사용자.removeFavorite(즐겨찾기);

        // then
        assertThat(사용자.getFavorites().values()).hasSize(0);
    }
}
