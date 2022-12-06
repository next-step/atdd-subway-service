package nextstep.subway.favorite.domain;

import static nextstep.subway.line.domain.StationFixture.강남역;
import static nextstep.subway.line.domain.StationFixture.역삼역;

import nextstep.subway.favorite.exception.NotValidMemberException;
import nextstep.subway.member.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class FavoriteTest {

    @Test
    void 자신의_즐겨찾기가_아닌경우_예외() {
        Favorite favorite = new Favorite(new Member(1L), 강남역, 역삼역);
        Assertions.assertThatThrownBy(() -> favorite.validateBeforeRemove(2L))
            .isInstanceOf(NotValidMemberException.class)
            .hasMessage("자신의 즐겨찾기가 아닌 항목은 삭제할 수 없습니다.");
    }

}