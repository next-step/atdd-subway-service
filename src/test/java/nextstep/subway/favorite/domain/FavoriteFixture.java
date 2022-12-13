package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import org.springframework.test.util.ReflectionTestUtils;

public class FavoriteFixture {

    public static Favorite 즐겨찾기(Long id, Member member, Long sourceId, Long targetId) {
        Favorite favorite = new Favorite(member, sourceId, targetId);
        ReflectionTestUtils.setField(favorite, "id", id);
        return favorite;
    }
}
