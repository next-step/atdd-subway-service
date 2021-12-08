package nextstep.subway.favorite.domain;

import static nextstep.subway.member.fixture.MemberFixture.사용자1;
import static nextstep.subway.member.fixture.MemberFixture.사용자2;
import static nextstep.subway.station.domain.StationFixture.강남역;
import static nextstep.subway.station.domain.StationFixture.신촌역;

import nextstep.subway.common.CanNotDeleteException;
import nextstep.subway.member.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class FavoriteTest {

    @Test
    void 유저가_동일하다면_즐겨찾기_삭제가능() {
        // given
        Member 사용자1 = 사용자1();
        Member 사용자2 = 사용자2();
        Favorite favorite = new Favorite(1L, 사용자1.getId(), 강남역(), 신촌역());

        Assertions.assertThatThrownBy(() -> {
            favorite.canDeleted(new MemberId(사용자2.getId()));
        }).isInstanceOf(CanNotDeleteException.class);
    }
}