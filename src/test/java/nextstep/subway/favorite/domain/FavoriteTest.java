package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Favorite 단위 테스트")
class FavoriteTest {

    @DisplayName("Favorite 소유자 테스력")
    @Test
    void isNotOwnerTest() {

        // given
        Long memberId = 1L;
        Member member = mock(Member.class);
        when(member.getId()).thenReturn(memberId);

        // when
        Favorite favorite = new Favorite(member, null, null);

        // then
        assertThat(favorite.isNotOwner(memberId)).isFalse();
        assertThat(favorite.isNotOwner(5L)).isTrue();
    }
}
