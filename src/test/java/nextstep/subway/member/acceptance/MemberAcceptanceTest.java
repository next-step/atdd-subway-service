package nextstep.subway.member.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.fixture.TestLoginFactory;
import nextstep.subway.fixture.TestMemberFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        ExtractableResponse<Response> createResponse = TestMemberFactory.회원_등록_요청(EMAIL, PASSWORD, AGE);
        // then
        TestMemberFactory.회원_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = TestMemberFactory.회원_정보_조회_요청(createResponse);
        // then
        TestMemberFactory.회원_정보_조회됨(findResponse, EMAIL, AGE);

        // when
        ExtractableResponse<Response> updateResponse = TestMemberFactory.회원_정보_수정_요청(createResponse, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        // then
        TestMemberFactory.회원_정보_수정됨(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = TestMemberFactory.회원_삭제_요청(createResponse);
        // then
        TestMemberFactory.회원_삭제됨(deleteResponse);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        // Given
        ExtractableResponse<Response> createResponse = TestMemberFactory.회원_등록_요청(EMAIL, PASSWORD, AGE);
        // And
        TestMemberFactory.회원_생성됨(createResponse);
        // And
        ExtractableResponse<Response> response = TestLoginFactory.로그인_요청(EMAIL, PASSWORD);
        // And
        String 토큰 = TestLoginFactory.로그인_되어_있음(response);

        // when
        ExtractableResponse<Response> findResponse = TestMemberFactory.본인_정보_조회_요청(토큰);
        // then
        TestMemberFactory.본인_정보_조회됨(findResponse, EMAIL, AGE);

        // when
        ExtractableResponse<Response> updateResponse = TestMemberFactory.본인_정보_수정_요청(토큰, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        // then
        TestMemberFactory.본인_정보_수정됨(updateResponse);

        // given
        ExtractableResponse<Response> loginResponse = TestLoginFactory.로그인_요청(NEW_EMAIL, NEW_PASSWORD);
        String 새로운토큰 = TestLoginFactory.로그인_되어_있음(loginResponse);

        // when
        ExtractableResponse<Response> deleteResponse = TestMemberFactory.본인_삭제_요청(새로운토큰);
        // then
        TestMemberFactory.본인_삭제됨(deleteResponse);
    }
}
