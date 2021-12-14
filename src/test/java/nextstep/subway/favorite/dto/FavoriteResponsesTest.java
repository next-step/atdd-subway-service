package nextstep.subway.favorite.dto;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

public class FavoriteResponsesTest {
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Member member;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        member = new Member("email@email.com", "password", 20);
    }

    @DisplayName("즐겨찾기 응답 생성")
    @Test
    void from() {
        Favorite favorite1 = new Favorite(강남역, 양재역, member);
        Favorite favorite2 = new Favorite(교대역, 남부터미널역, member);
        List<Favorite> favorites = Arrays.asList(favorite1, favorite2);

        FavoriteResponses favoriteResponses = FavoriteResponses.from(favorites);

        assertThat(favoriteResponses).isEqualTo(FavoriteResponses.from(favorites));
    }
}
