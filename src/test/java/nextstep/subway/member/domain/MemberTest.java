package nextstep.subway.member.domain;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class MemberTest {

    /**
     * Given Member 를 생성하고
     * When 생성된 Member 로 favorite 를 생성할때
     * Then Member 에서 해당 Favorite 를 검색할수 있다.
     */
    @DisplayName("favorite 와 연관관계 테스트")
    @Test
    void relationshipTest() {
        // Given
        Member member = new Member("email", "test", 20);

        // When
        Favorite favorite = new Favorite(member, new Station("강남역"), new Station("수원역"));

        // Then
        assertThat(member.isContain(favorite)).isTrue();
    }
}