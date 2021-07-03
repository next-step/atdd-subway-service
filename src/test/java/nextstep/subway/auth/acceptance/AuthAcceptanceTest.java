package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.utils.PrivateRestAssuredTemplate;
import nextstep.subway.utils.RestAssuredTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.PageController.URIMapping.MEMBERS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static nextstep.subway.member.MemberAcceptanceTest.*;

@DisplayName("Bearer Auth")
public class AuthAcceptanceTest extends AcceptanceTest {
    private static RestAssuredTemplate restAssuredTemplate = new RestAssuredTemplate("/login/token");

    @DisplayName("등록된 회원이 로그인을 시도한다.")
    @Test
    void myInfoWithBearerAuth() {
        // given
        MemberAcceptanceTest.requestCreateMember(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> response = login(new TokenRequest(EMAIL, PASSWORD));

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(response.as(TokenResponse.class)).isNotNull()
        );
    }

    @DisplayName("잘못된 이메일 또는 비밀번호로 로그인을 할수없다.")
    @Test
    void myInfoWithBadBearerAuth() {
        // given
        MemberAcceptanceTest.requestCreateMember(EMAIL, PASSWORD, AGE);

        // then
        assertAll(
            () -> assertThat(login(new TokenRequest("none", PASSWORD))
                    .statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),

            () -> assertThat(login(new TokenRequest(EMAIL, "none"))
                    .statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value())
        );
    }

    @DisplayName("유효하지 않는 토큰으로는 인증 페이지를 접속할수없다.")
    @Test
    void myInfoWithWrongBearerAuth() {
        // given
        MemberAcceptanceTest.회원_생성됨(MemberAcceptanceTest.requestCreateMember(EMAIL, PASSWORD, AGE));

        //when
        PrivateRestAssuredTemplate privateRestAssuredTemplate = new PrivateRestAssuredTemplate("X.X.X", MEMBERS + "/me");
        ExtractableResponse<Response> response = privateRestAssuredTemplate.get();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * @see nextstep.subway.auth.ui.AuthController#login
     */
    public static ExtractableResponse<Response> login(TokenRequest tokenRequest) {
        return restAssuredTemplate.post(tokenRequest);
    }

}
