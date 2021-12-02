package nextstep.subway.common;

import nextstep.subway.common.exception.line.LineNotFoundException;
import nextstep.subway.common.exception.member.MemberNotFoundException;
import nextstep.subway.common.exception.ServiceException;
import nextstep.subway.common.exception.station.StationNotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * packageName : nextstep.subway.common
 * fileName : CustomExceptionTest
 * author : haedoang
 * date : 2021-12-02
 * description :
 */
@DisplayName("CustomException")
public class CustomExceptionTest {
    private static final Long NOT_EXIST_ID = 5L;

    @Test
    @DisplayName("not found custom 예외 처리")
    public void notFoundExceptions() {
        Assertions.assertAll(
                () -> {
                    ServiceException exception = assertThrows(LineNotFoundException.class, () -> {
                        throw new LineNotFoundException(NOT_EXIST_ID);
                    });
                    assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
                    assertTrue(exception.getMessage().startsWith(LineNotFoundException.message));
                },
                () -> {
                    ServiceException exception = assertThrows(MemberNotFoundException.class, () -> {
                        throw new MemberNotFoundException(NOT_EXIST_ID);
                    });
                    assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
                    assertTrue(exception.getMessage().startsWith(MemberNotFoundException.message));
                },
                () -> {
                    ServiceException exception = assertThrows(StationNotFoundException.class, () -> {
                        throw new StationNotFoundException(NOT_EXIST_ID);
                    });
                    assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
                    assertTrue(exception.getMessage().startsWith(StationNotFoundException.message));
                }
        );
    }
}
