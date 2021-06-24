package nextstep.subway.member.domain;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    private Station 방화역;
    private Station 청구역;
    private Station 미사역;
    private Station 하남검단산역;
    private Line 오호선;
    private Member 사용자;

    @BeforeEach
    public void setUp() {
        방화역 = new Station(1L, "방화역");
        청구역 = new Station(1L, "청구역");
        미사역 = new Station(1L, "미사역");
        하남검단산역 = new Station(1L, "하남검단산역");

        오호선 = new Line(1L, "오호선", "보라색");

        오호선.addSection(new Section(1L, 방화역, 하남검단산역, 10));
        오호선.addSection(new Section(2L, 방화역, 청구역, 3));
        오호선.addSection(new Section(3L, 청구역, 미사역, 3));

        사용자 = new Member(1L, EMAIL, PASSWORD, AGE, new ArrayList<>());
    }

    @DisplayName("즐겨찾기 목록 조회")
    @Test
    public void 즐겨찾기목록_조회_확인() throws Exception {
        //when
        Favorite favorite1 = new Favorite(1L, 사용자, 방화역, 청구역);
        Favorite favorite2 = new Favorite(2L, 사용자, 미사역, 하남검단산역);
        Favorite favorite3 = new Favorite(3L, 사용자, 방화역, 하남검단산역);

        //then
        assertThat(사용자.getFavorites()).containsExactly(favorite1, favorite2, favorite3);
    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    public void 즐겨찾기_삭제_확인() throws Exception {
        //given
        Favorite favorite1 = new Favorite(1L, 사용자, 방화역, 청구역);
        Favorite favorite2 = new Favorite(2L, 사용자, 미사역, 하남검단산역);
        Favorite favorite3 = new Favorite(3L, 사용자, 방화역, 하남검단산역);

        //when
        사용자.removeFavorite(favorite1);

        //then
        assertThat(사용자.getFavorites()).containsExactly(favorite2, favorite3);
    }
}
