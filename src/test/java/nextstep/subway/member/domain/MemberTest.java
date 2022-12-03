package nextstep.subway.member.domain;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.exception.NotFoundFavoriteException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class MemberTest {

    public static final String EMAIL = "email@mail.com";
    public static final String PASSWORD = "pass";
    public static final int AGE = 10;

    public static final Station SOURCE_STATION = new Station("station1");
    public static final Station TARGET_STATION = new Station("station2");

    @Autowired
    MemberRepository members;

    @Test
    void getFavorites() {
        Member member = saveMember();

        assertThat(member.getFavorites()).hasSize(1);
        assertThat(member.getFavorites())
                .extracting(Favorite::getId)
                .isNotNull();
    }

    @Test
    void removeFavoriteById() {
        Member member = saveMember();
        Favorite favorite = member.getFavorites().get(0);

        member.removeFavorite(favorite.getId());
        members.save(member);

        Member savedMember = members.findById(member.getId()).get();
        assertThat(savedMember.getFavorites()).isEmpty();
    }

    @Test
    void testRemoveFavorite() {
        Member member = saveMember();
        Favorite favorite = member.getFavorites().get(0);

        member.removeFavorite(favorite);
        members.save(member);

        Member savedMember = members.findById(member.getId()).get();
        assertThat(savedMember.getFavorites()).isEmpty();
    }

    @Test
    void findFavorite() {
        Member member = saveMember();
        Favorite favorite = member.getFavorites().get(0);

        Favorite foundFavorite = member.findFavorite(favorite.getId());

        assertThat(foundFavorite.getId()).isNotNull();
    }

    @Test
    void testNotFoundFavoriteException() {
        Member member = saveMember();
        long notExistsFavoriteId = Long.MAX_VALUE;

        assertThatThrownBy(() -> member.findFavorite(notExistsFavoriteId))
                .isInstanceOf(NotFoundFavoriteException.class);
    }

    private Member saveMember() {
        Member member = members.save(new Member(EMAIL, PASSWORD, AGE));
        member.addFavorite(new Favorite(member, SOURCE_STATION, TARGET_STATION));

        members.save(member);
        return member;
    }
}
