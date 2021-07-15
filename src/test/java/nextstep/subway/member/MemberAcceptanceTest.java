package nextstep.subway.member;

import static nextstep.subway.auth.acceptance.AuthTestMethod.*;
import static nextstep.subway.member.MemberTestMethod.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthTestMethod;

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
    @Test
    void manageMyInfo() {
		// Scenario : 나의 정보 관리 시나리오
		// Given
		ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
		// And
		String token = getToken(login(EMAIL, PASSWORD));
		// When
		ExtractableResponse<Response> MyInfoResponse1 = findMyInformation(token);
		// Then
		assertThat(MyInfoResponse1.statusCode()).isEqualTo(HttpStatus.OK.value());
		// When
		ExtractableResponse<Response> MyInfoResponse2 = updateMyInformation(token, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
		// Then
		String newToken = getToken(login(NEW_EMAIL, NEW_PASSWORD));
		assertThat(MyInfoResponse2.statusCode()).isEqualTo(HttpStatus.OK.value());
		ExtractableResponse<Response> MyInfoResponse3 = findMyInformation(newToken);
		assertThat(MyInfoResponse3.statusCode()).isEqualTo(HttpStatus.OK.value());
		// When
		ExtractableResponse<Response> MyInfoResponse4 = deleteMyInformation(newToken);
		// Then
		assertThat(MyInfoResponse4.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
