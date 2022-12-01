package nextstep.subway.member.acceptance;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTestFixture.로그인_요청;
import static nextstep.subway.member.acceptance.MemberAcceptanceTestFixture.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.Collection;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int AGE = 20;
    public static final int NEW_AGE = 21;

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        // when
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // then
        회원_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = 회원_정보_조회_요청(createResponse);
        // then
        회원_정보_조회됨(findResponse, EMAIL, AGE);

        // when
        ExtractableResponse<Response> updateResponse = 회원_정보_수정_요청(createResponse, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        // then
        회원_정보_수정됨(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(createResponse);
        // then
        회원_삭제됨(deleteResponse);
    }

    @DisplayName("나의 정보를 관리한다.")
    @TestFactory
    Collection<DynamicTest> manageMyInfo() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        String accessToken = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class).getAccessToken();

        return Arrays.asList(
            dynamicTest("내 정보를 조회할 수 있다.", () -> {
                // when
                ExtractableResponse<Response> findResponse = 내_정보_조회_요청(accessToken);
                // then
                회원_정보_조회됨(findResponse, EMAIL, AGE);
            }),
            dynamicTest("내 정보를 수정할 수 있다.", () -> {
                // when
                ExtractableResponse<Response> updateResponse = 내_정보_수정_요청(accessToken, new MemberRequest(NEW_EMAIL, NEW_PASSWORD, NEW_AGE));
                // then
                회원_정보_수정됨(updateResponse);
            }),
            dynamicTest("내 정보를 삭제할 수 있다.", () -> {
                // given
                ExtractableResponse<Response> loginResponse = 로그인_요청(NEW_EMAIL, NEW_PASSWORD);
                // when
                ExtractableResponse<Response> deleteResponse = 내_정보_삭제_요청(loginResponse.as(TokenResponse.class).getAccessToken());
                // then
                회원_삭제됨(deleteResponse);
            })
        );
    }
}
