package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;

import java.util.List;

public interface FavoriteRepository {
    Favorite save(Favorite favorite);

    List<Favorite> findAllByMember(Member member);
}
