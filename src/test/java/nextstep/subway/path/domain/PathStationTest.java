package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PathStationTest {

    @DisplayName("경로 지하철역을 생성한다.")
    @Test
    void create() {
        // when
        PathStation 강남역 = new PathStation(1L, "강남역", LocalDateTime.now());

        // then
        assertThat(강남역).isNotNull();
    }

    @DisplayName("id와 이름이 같으면 동등성을 보장한다.")
    @Test
    void equals() {
        // given
        long id = 1L;
        String name = "강남역";
        PathStation 강남역 = new PathStation(id, name, LocalDateTime.now());
        PathStation 강남역2 = new PathStation(id, name, LocalDateTime.now());

        // when
        boolean equals = 강남역.equals(강남역2);

        // then
        assertThat(equals).isTrue();
    }

}
