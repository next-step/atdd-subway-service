package nextstep.subway.auth.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Optional;
import java.util.stream.Stream;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@ExtendWith(MockitoExtension.class)
public class AuthAcceptanceTest extends AcceptanceTest {
    private static final TokenRequest 몬드 = new TokenRequest("mond@mond.com", "younggun");
    private static final TokenRequest 없다 = new TokenRequest("not@exist.com", "exist");

    @MockBean
    private MemberRepository memberRepository;

    /**
     *  Given 멤버가 주어지고
     *  When 해당 멤버 id / password로 로그인을 하면
     *  Then Bearer 토큰을 받아온다
     */
    @Test
    @DisplayName("멤버의 정보로 로그인을 하면 정상적으로 Bearer 토큰을 받아온다")
    void myInfoWithBearerAuth() {
        // given
        Member mond = new Member("mond@mond.com", "younggun", 10);
        when(memberRepository.findByEmail("mond@mond.com")).thenReturn(Optional.of(mond));

        // when
        ExtractableResponse<Response> 로그인_요청_결과 = 로그인_요청(몬드);

        // then
        로그인_요청_성공(로그인_요청_결과);
    }

    /**
     *  Given 멤버가 주어지고
     *  When 올바르지 않은 id / password로 로그인을 하면
     *  Then 로그인이 실패한다
     */
    @ParameterizedTest(name = "올바르지 않은 정보({0})로 로그인을 하면 로그인이 실패한다")
    @MethodSource("invalidTokenRequest")
    void myInfoWithBadBearerAuth(TokenRequest 올바르지_않은_유저_정보) {
        // given
        Member mond = new Member("mond@mond.com", "mondmondmond", 10);
        when(memberRepository.findByEmail("mond@mond.com")).thenReturn(Optional.of(mond));

        // when
        ExtractableResponse<Response> 로그인_요청_결과 = 로그인_요청(올바르지_않은_유저_정보);

        // then
        로그인_요청_실패(로그인_요청_결과);
    }

    public static Stream<Arguments> invalidTokenRequest() {
        return Stream.of(
                Arguments.of(몬드), Arguments.of(없다)
        );
    }

    /**
     *  When 올바르지 않은 Bearer 토큰으로 호출하면
     *  Then 조회할 수 없다
     */
    @Test
    @DisplayName("올바르지 않는 토큰으로 조회 할 수 없다")
    void myInfoWithWrongBearerAuth() {
        // when

        // then
    }

    private ExtractableResponse<Response> 로그인_요청(TokenRequest 로그인정보) {
        return RestAssured.given().log().all()
                .body(로그인정보)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    private void 로그인_요청_성공(ExtractableResponse<Response> 로그인_요청_결과) {
        TokenResponse 응답_토큰 = 로그인_요청_결과.as(TokenResponse.class);
        assertAll(
                () -> assertThat(로그인_요청_결과.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(응답_토큰.getAccessToken()).isNotNull()
        );
    }

    private void 로그인_요청_실패(ExtractableResponse<Response> 로그인_요청_결과) {
        assertThat(로그인_요청_결과.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
