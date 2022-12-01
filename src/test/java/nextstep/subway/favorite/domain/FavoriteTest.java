package nextstep.subway.favorite.domain;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FavoriteTest {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    private Member 회원;
    private Station 강남역;
    private Station 양재역;

    @BeforeEach
    public void setUp() {
        회원 = 회원_생성(EMAIL, PASSWORD, AGE);
        강남역 = 지하철역_생성("강남역");
        양재역 = 지하철역_생성("양재역");
    }

    @Test
    @DisplayName("즐겨찾기 생성")
    void create() {
        // when
        Favorite favorite = new Favorite(회원, 강남역, 양재역);

        // then
        assertThat(favorite).isNotNull();
    }

    private Member 회원_생성(String email, String password, Integer age) {
        return new Member.Builder()
                .email(email)
                .password(password)
                .age(age)
                .build();
    }

    private Station 지하철역_생성(String name) {
        return new Station.Builder()
                .name(name)
                .build();
    }
}