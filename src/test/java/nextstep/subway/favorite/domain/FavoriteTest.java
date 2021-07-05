package nextstep.subway.favorite.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static nextstep.subway.member.domain.MemberTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FavoriteTest {
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
    
    @DisplayName("즐겨찾기 생성 - 회원, 즐겨찾기 관계 확인")
    @Test
    public void 즐겨찾기_생성_확인() throws Exception {
        //when
        Favorite favorite = new Favorite(1L, 사용자, 방화역, 하남검단산역);
        
        //then
        assertThat(사용자.getFavorites()).containsExactly(favorite);
    }

    @DisplayName("즐겨찾기 생성 - 같은 즐겨찾기는 한 번만 등록되야한다." +
            "같은 즐겨찾기는 아이디가 같은 경우 or 사용자, 출발역, 도착역이 같은 경우이다.")
    @Test
    public void 같은즐겨찾기_추가_확인() throws Exception {
        //given
        Favorite favorite1 = new Favorite(1L, 사용자, 방화역, 하남검단산역);
        Favorite favorite2 = new Favorite(1L, 사용자, 방화역, 하남검단산역);
        Favorite favorite3 = new Favorite(2L, 사용자, 방화역, 하남검단산역);

        //when
        사용자.addFavorite(favorite1);
        사용자.addFavorite(favorite2);
        사용자.addFavorite(favorite3);

        //then
        assertThat(사용자.getFavorites()).containsExactly(favorite1);
    }

    @DisplayName("즐겨찾기 생성 - 사용자 NULL 체크 예외")
    @Test
    public void 즐겨찾기_등록_예외1() throws Exception {
        //when
        //then
        assertThatThrownBy(() -> new Favorite(1L, null, 방화역, 하남검단산역))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("즐겨찾기 생성 - 출발역 NULL 체크 예외")
    @Test
    public void 즐겨찾기_등록_예외2() throws Exception {
        //when
        //then
        assertThatThrownBy(() -> new Favorite(1L, 사용자, null, 하남검단산역))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("즐겨찾기 생성 - 도착역 NULL 체크 예외")
    @Test
    public void 즐겨찾기_등록_예외3() throws Exception {
        //when
        //then
        assertThatThrownBy(() -> new Favorite(1L, 사용자, 방화역, null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
