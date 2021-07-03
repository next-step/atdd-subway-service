package nextstep.subway.favorite.domain;

import nextstep.subway.exception.Message;
import nextstep.subway.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.TestFixture.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FavoriteTest {

    @DisplayName("즐겨찾기의 도착역과 출발역이 동일할 경우 예외발생")
    @Test
    void 출발역_동일역_같음() {
        //Given
        Member member = new Member(EMAIL, PASSWORD, ADULT_AGE);

        //When+Then
        assertThatThrownBy(() -> new Favorite(member, 회현역, 회현역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Message.ERROR_START_AND_END_STATIONS_ARE_SAME.showText());
    }
}
