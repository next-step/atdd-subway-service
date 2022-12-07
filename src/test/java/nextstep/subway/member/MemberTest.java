package nextstep.subway.member;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

public class MemberTest {

    private Station 강남역;
    private Station 역삼역;
    private Member member;

    @BeforeEach
    public void setUp() {
        this.강남역 = new Station("강남역");
        this.역삼역 = new Station("역삼역");
        this.member = new Member(1L, "email@email.com", "password", 22);
    }

    @DisplayName("멤버에서 즐겨찾기를 추가한다")
    @Test
    void addFavorite() {
        Favorite favorite = member.addFavorite(강남역, 역삼역);

        즐겨찾기가_추가됨(favorite);
    }

    @DisplayName("멤버에서 즐겨찾기를 삭제한다")
    @Test
    void removeFavorite() {
        Favorite favorite = new Favorite(강남역, 역삼역, member);

        member.removeFavorite(favorite);

        즐겨찾기가_삭제됨();
    }

    private void 즐겨찾기가_추가됨(Favorite favorite) {
        assertThat(favorite.getMember()).isEqualTo(member);
        assertThat(favorite.getSource()).isEqualTo(강남역);
        assertThat(favorite.getTarget()).isEqualTo(역삼역);
    }

    private void 즐겨찾기가_삭제됨() {
        assertThat(member.getFavorites().getFavoritePairs()).isEmpty();
    }

}
