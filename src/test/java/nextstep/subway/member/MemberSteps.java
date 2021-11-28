package nextstep.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;

import static nextstep.subway.AcceptanceTest.*;

public class MemberSteps {

    private static final String MEMBER_URI = "/members";

    public static void 회원_등록되어_있음(String email, String password, Integer age) {
        회원_생성을_요청(email, password, age);
    }

    public static ExtractableResponse<Response> 회원_생성을_요청(String email, String password, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);
        return post(MEMBER_URI, memberRequest);
    }

    public static ExtractableResponse<Response> 회원_정보_조회_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return get(uri);
    }

    public static ExtractableResponse<Response> 회원_정보_수정_요청(ExtractableResponse<Response> response, String email, String password, Integer age) {
        String uri = response.header("Location");
        MemberRequest memberRequest = new MemberRequest(email, password, age);
        return put(uri, memberRequest);
    }

    public static ExtractableResponse<Response> 회원_정보_삭제_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return delete(uri);
    }

    public static ExtractableResponse<Response> 내_정보_조회_요청(TokenResponse accessToken) {
        return getWithAuth(MEMBER_URI + "/me", accessToken.getAccessToken());
    }

    public static ExtractableResponse<Response> 내_정보_수정_요청(TokenResponse accessToken, String email, String password, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);
        return putWithAuth(MEMBER_URI + "/me", memberRequest, accessToken.getAccessToken());
    }

    public static ExtractableResponse<Response> 내_정보_삭제_요청(TokenResponse accessToken) {
        return deleteWithAuth(MEMBER_URI + "/me", accessToken.getAccessToken());
    }
}
