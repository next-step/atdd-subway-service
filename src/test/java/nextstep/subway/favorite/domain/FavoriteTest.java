package nextstep.subway.favorite.domain;

import nextstep.subway.exception.InputDataErrorCode;
import nextstep.subway.exception.InputDataErrorException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FavoriteTest {
    @Test
    @DisplayName("즐겨찾기 저장 테스트")
    void saveFavoriteTest() {
        Member member = new Member("abc@nate.com", "123", 12);
        Favorite createdFavorite = new Favorite(member, new Station("당산역"), new Station("선유도역"));
        assertThat(createdFavorite.getMember()).isEqualTo(member);
        assertThat(createdFavorite.getSource().getName()).isEqualTo("당산역");
        assertThat(createdFavorite.getTarget().getName()).isEqualTo("선유도역");
    }

    @Test
    @DisplayName("입력값중에 입력하지 않을 경우 테스트")
    void inputDataNullTest() {
        assertThatThrownBy(() -> {
            new Favorite(null, new Station("당산역"), new Station("선유도역"));
        }).isInstanceOf(InputDataErrorException.class)
                .hasMessageContaining(InputDataErrorCode.THE_MEMBER_OR_SOURCE_OR_TARGET_IS_NULL.errorMessage());
    }

    @Test
    @DisplayName("출발역과 도착역이 같을 경우 테스트")
    void inputDataSameStationsTest() {
        Station dangsanStation = new Station("당산역");
        assertThatThrownBy(() -> {
            new Favorite(new Member("abc@nate.com", "123", 12), dangsanStation, dangsanStation);
        }).isInstanceOf(InputDataErrorException.class)
                .hasMessageContaining(InputDataErrorCode.THERE_IS_SAME_STATIONS.errorMessage());
    }

}
