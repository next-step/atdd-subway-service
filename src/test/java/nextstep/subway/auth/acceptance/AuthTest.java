package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberTest.*;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.dto.MemberRequest;

public class AuthTest {

    public static final LoginMember 로그인_일반_사용자 = new LoginMember(1L, EMAIL, AGE);
    public static final LoginMember 로그인_청소년_사용자 = new LoginMember(2L, EMAIL, TEENAGER);
    public static final LoginMember 로그인_어린이_사용자 = new LoginMember(3L, EMAIL, CHILD);

    public static TokenResponse 로그인_된_회원(Member member) {
        return 로그인_된_회원(member.getEmail(), member.getPassword(), member.getAge());
    }

    public static TokenResponse 로그인_된_회원(String email, String password, int age) {
        회원_생성을_요청(email, password, age);

        TokenRequest request = new TokenRequest(email, password);
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().post("/login/token")
            .then().log().all().extract().as(TokenResponse.class);
    }

    private static ExtractableResponse<Response> 회원_생성을_요청(String email, String password, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(memberRequest)
            .when().post("/members")
            .then().log().all()
            .extract();
    }
}
