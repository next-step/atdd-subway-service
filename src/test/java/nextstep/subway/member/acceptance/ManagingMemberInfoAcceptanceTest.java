package nextstep.subway.member.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptance;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ManagingMemberInfoAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password157#";
    private static final int AGE = 20;

    private ExtractableResponse createResponse;
    private TokenResponse tokenResponse;

    @BeforeEach
    public void setUp() {
        super.setUp();

        createResponse = MemberAcceptance.create_member(EMAIL, PASSWORD, AGE);
        tokenResponse = AuthAcceptance.member_token_is_issued(EMAIL, PASSWORD);
    }

    /**
     * Given 회원이 등록되어 있고
     * When 내 정보를 발급된 토큰 정보와 함께 조회하면
     * Then 내 정보를 조회할 수 있다.
     */
    @DisplayName("발급된 토큰 정보와 함께 내 정보를 조회한다.")
    @Test
    void selectMyInfo() {
        // when
        MemberResponse memberResponse = MemberAcceptance.member_was_queried(tokenResponse).as(MemberResponse.class);

        // then
        assertEquals(EMAIL, memberResponse.getEmail());
    }

    /**
     * Given 회원이 등록되어 있고
     * When 유효하지 않은 토큰 정보로 내 정보를 조회하면
     * Then 내 정보를 조회할 수 없다.
     */
    @DisplayName("유효하지 않은 토큰 정보로 내 정보를 조회한다.")
    @Test
    void selectMyInfoWithInvalidToken() {
        // when
        TokenResponse invalidToken = new TokenResponse("Invalid_Token");
        ExtractableResponse<Response> response = MemberAcceptance.member_was_queried(invalidToken);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 회원이 등록되어 있고
     * When 내 정보를 발급된 토큰 번호와 함께 수정하면
     * Then 내 정보를 수정할 수 있다.
     */
    @DisplayName("발급된 토큰 정보와 함께 내 정보를 수정한다.")
    @Test
    void updateMyInfo() {
        // when
        ExtractableResponse<Response> response = MemberAcceptance.member_was_updated(tokenResponse,
                "testuser-new@test.com", PASSWORD, AGE);

        // then
        assertEquals(HttpStatus.OK.value(), response.statusCode());
    }

    /**
     * Given 회원이 등록되어 있고
     * When 유효하지 않은 토큰 정보로 내 정보를 수정하면
     * Then 내 정보를 수정할 수 없다.
     */
    @DisplayName("유효하지 않은 토큰 정보로 내 정보를 수정한다.")
    @Test
    void updateMyInfoWithInvalidToken() {
        // when
        TokenResponse invalidToken = new TokenResponse("Invalid_Token");
        ExtractableResponse<Response> response = MemberAcceptance.member_was_updated(invalidToken,
                "testuser-new@test.com", PASSWORD, AGE);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 회원이 등록되어 있고
     * When 내 정보를 발급된 토큰 번호와 함께 삭제하면
     * Then 내 정보를 삭제할 수 있다.
     */
    @DisplayName("내 정보를 삭제한다.")
    @Test
    void deleteMyInfo() {
        // when
        ExtractableResponse<Response> response = MemberAcceptance.member_was_deleted(tokenResponse);

        // then
        assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode());
    }

    /**
     * Given 회원이 등록되어 있고
     * When 유효하지 않은 토큰 정보로 내 정보를 삭제하면
     * Then 내 정보를 삭제할 수 없다.
     */
    @DisplayName("유효하지 않은 토큰 정보로 내 정보를 삭제한다.")
    @Test
    void deleteMyInfoWithInvalidToken() {
        // when
        TokenResponse invalidToken = new TokenResponse("Invalid_Token");
        ExtractableResponse<Response> response = MemberAcceptance.member_was_deleted(invalidToken);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }
}
