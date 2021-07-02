package nextstep.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.utils.RestAssuredTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.PageController.URIMapping.MEMBERS;

@DisplayName("나의 정보를 관리한다.")
public class MemberMeAcceptanceTest extends AcceptanceTest {
    public static final RestAssuredTemplate restAssuredTemplate = new RestAssuredTemplate(MEMBERS + "/me");

    @DisplayName("나의 정보를 조회한다.")
    @Test
    void findMyInfo() {

    }

    @DisplayName("나의 정보를 수정한다.")
    @Test
    void updateMyInfo() {

    }

    @DisplayName("나의 정보를 삭제한다.")
    @Test
    void deleteMyInfo() {

    }

    /**
     * @see nextstep.subway.member.ui.MemberController#findMemberOfMine
     */
    public static ExtractableResponse<Response> requestFindMyInfo(final Long id) {
        return restAssuredTemplate.get(id);
    }

    /**
     * @see nextstep.subway.member.ui.MemberController#updateMemberOfMine
     */
    public static ExtractableResponse<Response> requestUpdateInfo(final Long id, final String email, final String password, final Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return restAssuredTemplate.put(id, memberRequest);
    }

    /**
     * @see nextstep.subway.member.ui.MemberController#deleteMemberOfMine
     */
    public static ExtractableResponse<Response> requestDeleteMyInfo(final Long id) {
        return restAssuredTemplate.delete(id);
    }

}
