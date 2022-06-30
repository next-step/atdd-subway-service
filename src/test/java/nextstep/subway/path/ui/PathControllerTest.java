package nextstep.subway.path.ui;


import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class PathControllerTest {

    @Test
    @DisplayName("Path Request 유효성 검사 ")
    void validPathRequest() {
        //given
        PathService pathService = mock(PathService.class);
        PathController pathController = new PathController(pathService);

        assertAll(
                () -> assertThatIllegalArgumentException()
                        .isThrownBy(() -> pathController.findPaths(LoginMember.createGuestLoginMember(),null)),
                () -> assertThatIllegalArgumentException()
                        .isThrownBy(() -> pathController.findPaths(LoginMember.createGuestLoginMember(), new PathRequest(0, 1))),
                () -> assertThatIllegalArgumentException()
                        .isThrownBy(() -> pathController.findPaths(LoginMember.createGuestLoginMember(), new PathRequest(1, 0)))
        );
    }

}