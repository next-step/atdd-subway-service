package nextstep.subway.path.applicaiton;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathServiceTest {

    @DisplayName("출발역과 도착역으로 최단거리를 조회할 수 있다.")
    @Test
    void shortest_path() {
        //given

        //when

        //then

    }

    @DisplayName("출발역과 도착역이 같은 경우 IllegalArgumentException 이 발생한다.")
    @Test
    void same_source_target() {
        //given

        //when

        //then
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 IllegalArgumentException 이 발생한다.")
    @Test
    void not_linked_line() {
        //given

        //when

        //then

    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우 IllegalArgumentException 이 발생한다.")
    @Test
    void none_station() {
        //given

        //when

        //then

    }
}
