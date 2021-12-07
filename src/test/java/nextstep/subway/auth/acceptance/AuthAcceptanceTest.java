package nextstep.subway.auth.acceptance;

import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthAcceptanceTest extends AcceptanceTest {

  @DisplayName("Bearer Auth")
  @Test
  void myInfoWithBearerAuth() {
  }

  @DisplayName("Bearer Auth 로그인 실패")
  @Test
  void myInfoWithBadBearerAuth() {
  }

  @DisplayName("Bearer Auth 유효하지 않은 토큰")
  @Test
  void myInfoWithWrongBearerAuth() {
  }

}
