package nextstep.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberAcceptanceTest extends AcceptanceTest {

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        // when
        ExtractableResponse<Response> createResponse = MemberFixture
            .회원_생성을_요청(MemberFixture.EMAIL, MemberFixture.PASSWORD, MemberFixture.AGE);
        // then
        MemberFixture.회원_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = MemberFixture.회원_정보_조회_요청(createResponse);
        // then
        MemberFixture.회원_정보_조회됨(findResponse, MemberFixture.EMAIL, MemberFixture.AGE);

        // when
        ExtractableResponse<Response> updateResponse = MemberFixture
            .회원_정보_수정_요청(createResponse, MemberFixture.NEW_EMAIL, MemberFixture.NEW_PASSWORD, MemberFixture.NEW_AGE);
        // then
        MemberFixture.회원_정보_수정됨(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = MemberFixture.회원_삭제_요청(createResponse);
        // then
        MemberFixture.회원_삭제됨(deleteResponse);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {

    }

}
