package nextstep.subway.favorite.domain;

import nextstep.subway.favorite.exception.FavoriteException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.Fixture.*;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteTest {

    private Station 강남역;
    private Station 양재역;
    private Member 회원;

    @BeforeEach
    public void setUp() {
        강남역 = createStation("강남역", 1l);
        양재역 = createStation("양재역", 2l);
        회원 = createMember("test@github.com","test1023",25);

    }

    @DisplayName("즐겨찾기 등록시 출발역이 없으면 예외발생")
    @Test
    void returnsExceptionWithNoneSourceStation() {
        Assertions.assertThatThrownBy(() -> new Favorite(회원,null,양재역))
                .isInstanceOf(FavoriteException.class)
                .hasMessageStartingWith("출발역을 지정해야 합니다");
    }

    @DisplayName("즐겨찾기 등록시 도착역이 없으면 예외발생")
    @Test
    void returnsExceptionWithNoneTargetStation() {
        Assertions.assertThatThrownBy(() -> new Favorite(회원,강남역,null))
                .isInstanceOf(FavoriteException.class)
                .hasMessageStartingWith("도착역을 지정해야 합니다");
    }

    @DisplayName("즐겨찾기 등록시 회원이 없으면 예외발생")
    @Test
    void returnsExceptionWithNoneMember() {
        Assertions.assertThatThrownBy(() -> new Favorite(null,강남역,양재역))
                .isInstanceOf(FavoriteException.class)
                .hasMessageStartingWith("회원정보를 입력해야 합니다");
    }

    @DisplayName("즐겨찾기 등록시 즐겨찾기 객체생성")
    @Test
    void returnsFavorite() {
        assertThat(new Favorite(회원,강남역,양재역)).isNotNull();
    }
}
