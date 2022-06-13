package nextstep.subway.auth.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Optional;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.assertj.core.api.AbstractIntegerAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@ExtendWith(MockitoExtension.class)
public class AuthAcceptanceTest extends AcceptanceTest {
    private final TokenRequest 몬드 = new TokenRequest("mond@mond.com", "younggun");

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
     *  When 올바르지 않은 id / password로 로그인을 하면
     *  Then 로그인이 실패한다
     */
    @Test
    @DisplayName("올바르지 않은 정보로 로그인을 하면 로그인이 실패한다")
    void myInfoWithBadBearerAuth() {
        // when

        // then
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
}
