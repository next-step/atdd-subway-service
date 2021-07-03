package nextstep.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.utils.PrivateRestAssuredTemplate;
import nextstep.subway.utils.RestAssuredTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.PageController.URIMapping.MEMBERS;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("나의 정보를 관리한다.")
public class MemberMeAcceptanceTest extends AcceptanceTest {
    private PrivateRestAssuredTemplate restAssuredTemplate;

    @BeforeEach
    public void setup() {
        //나의 정보는 등록되어 있음
        RestAssuredTemplate.getLocationId(MemberAcceptanceTest.requestCreateMember(EMAIL, PASSWORD, AGE));
        String token = AuthAcceptanceTest.login(new TokenRequest(EMAIL, PASSWORD))
                .as(TokenResponse.class)
                .getAccessToken();

        restAssuredTemplate = new PrivateRestAssuredTemplate(token, MEMBERS + "/me");
    }

    @DisplayName("나의 정보를 조회한다.")
    @Test
    void findMyInfo() {
        // when
        ExtractableResponse<Response> response = requestFindMyInfo();

        // then
        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertAll(
            () -> assertThat(memberResponse.getId()).isNotNull(),
            () -> assertThat(memberResponse.getEmail()).isEqualTo(EMAIL),
            () -> assertThat(memberResponse.getAge()).isEqualTo(AGE)
        );
    }

    @DisplayName("나의 정보를 수정한다.")
    @Test
    void updateMyInfo() {
        // when
        ExtractableResponse<Response> response = requestUpdateInfo(NEW_EMAIL, NEW_PASSWORD, NEW_AGE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("나의 정보를 삭제한다.")
    @Test
    void deleteMyInfo() {
        // when
        ExtractableResponse<Response> response = requestDeleteMyInfo();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * @see nextstep.subway.member.ui.MemberController#findMemberOfMine
     */
    private ExtractableResponse<Response> requestFindMyInfo() {
        return restAssuredTemplate.get();
    }

    /**
     * @see nextstep.subway.member.ui.MemberController#updateMemberOfMine
     */
    private ExtractableResponse<Response> requestUpdateInfo(final String email, final String password, final Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return restAssuredTemplate.put(memberRequest);
    }

    /**
     * @see nextstep.subway.member.ui.MemberController#deleteMemberOfMine
     */
    private ExtractableResponse<Response> requestDeleteMyInfo() {
        return restAssuredTemplate.delete();
    }

}
