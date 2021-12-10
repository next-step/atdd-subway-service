package nextstep.subway.member.step;

import static nextstep.subway.member.step.MemberAcceptanceStep.로그인_토큰발급;
import static nextstep.subway.member.step.MemberAcceptanceStep.회원_생성을_요청;


public class FavoriteAuthAcceptanceFixtures {

    public static String 로그인_토큰_발급(String email, String password, Integer age) {
        회원_생성을_요청(email, password, age);
        return 로그인_토큰발급(email, password);
    }
}
