package nextstep.subway.member;

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
        ExtractableResponse<Response> createResponse = MemberTestMethod.회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // then
		MemberTestMethod.회원_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = MemberTestMethod.회원_정보_조회_요청(createResponse);
        // then
		MemberTestMethod.회원_정보_조회됨(findResponse, EMAIL, AGE);

        // when
        ExtractableResponse<Response> updateResponse = MemberTestMethod.회원_정보_수정_요청(createResponse, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        // then
		MemberTestMethod.회원_정보_수정됨(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = MemberTestMethod.회원_삭제_요청(createResponse);
        // then
		MemberTestMethod.회원_삭제됨(deleteResponse);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
		// Scenario : 나의 정보 관리 시나리오
		// Given : 회원 등록되어 있음
		ExtractableResponse<Response> createResponse = MemberTestMethod.회원_생성을_요청(EMAIL, PASSWORD, AGE);
		// And : token 정보 가지고 있음
		String token = AuthTestMethod.getToken(AuthTestMethod.login(EMAIL, PASSWORD));
		// When : 나의 정보 조회 요청
		ExtractableResponse<Response> MyInfoResponse1 = MemberTestMethod.findMyInformation(token);
		// Then : 나의 정보 조회
		assertThat(MyInfoResponse1.statusCode()).isEqualTo(HttpStatus.OK.value());
		// When : 나의 정보 업데이트 요청
		ExtractableResponse<Response> MyInfoResponse2 = MemberTestMethod.updateMyInformation(token, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
		// Then : 업데이트 된 정보 조회
		String newToken = AuthTestMethod.getToken(AuthTestMethod.login(NEW_EMAIL, NEW_PASSWORD));
		assertThat(MyInfoResponse2.statusCode()).isEqualTo(HttpStatus.OK.value());
		ExtractableResponse<Response> MyInfoResponse3 = MemberTestMethod.findMyInformation(newToken);
		assertThat(MyInfoResponse3.statusCode()).isEqualTo(HttpStatus.OK.value());
		// When : 나의 정보 삭제 요청
		ExtractableResponse<Response> MyInfoResponse4 = MemberTestMethod.deleteMyInformation(newToken);
		// Then : 나의 정보 삭제 확인
		assertThat(MyInfoResponse4.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
