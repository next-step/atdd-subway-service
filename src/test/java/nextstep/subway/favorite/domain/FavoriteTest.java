package nextstep.subway.favorite.domain;

import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FavoriteTest {
    private Member member;
    private Station source;
    private Station target;

    @BeforeEach
    void setUp() {
        this.member = new Member(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD, MemberAcceptanceTest.AGE);
        this.source = Station.from("강남역");
        this.target = Station.from("교대역");
    }

    @DisplayName("즐겨찾기 생성")
    @Test
    void create_favorite() {
        // when
        Favorite newFavorite = Favorite.of(this.member, this.source, this.target);

        // then
        assertThat(newFavorite).isNotNull();
    }

    @DisplayName("사용자가 없으면 즐겨찾기를 생성할 수 없다.")
    @Test
    void create_null_member_favorite() {
        // when & then
        assertThatThrownBy(() -> Favorite.of(null, this.source, this.target))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("출발역이 없으면 즐겨찾기를 생성할 수 없다.")
    @Test
    void create_null_source_favorite() {
        // when & then
        assertThatThrownBy(() -> Favorite.of(this.member, null, this.target))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("도착역이 없으면 즐겨찾기를 생성할 수 없다.")
    @Test
    void create_null_target_favorite() {
        // when & then
        assertThatThrownBy(() -> Favorite.of(this.member, this.source, null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}