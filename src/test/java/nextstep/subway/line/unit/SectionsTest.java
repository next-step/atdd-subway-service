package nextstep.subway.line.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SectionsTest {

    @DisplayName("노선에 최초의 구간을 추가할 수 있다.")
    @Test
    void addSection_new_test() {
        // given

        // when

        // then
    }

    @DisplayName("노선에 상행종점 구간을 추가할 수 있다.")
    @Test
    void addSection_firstSection_test() {
        // given

        // when

        // then
    }

    @DisplayName("노선에 하행종점 구간을 추가할 수 있다.")
    @Test
    void addSection_lastSection_test() {
        // given

        // when

        // then
    }


    @DisplayName("노선에 중간 구간을 추가할 수 있다.")
    @Test
    void addSection_middleSection_test() {
        // given

        // when

        // then
    }


    @DisplayName("구간을 추가할때 상행역, 하행역이 전부 노선에 있으면 IllegalArgumentException 발생한다.")
    @Test
    void addSection_duplicated_exception() {
        // given

        // when

        // then
    }


    @DisplayName("구간을 추가할때 상행역, 하행역이 전부 노선에 없으면 IllegalArgumentException 발생한다.")
    @Test
    void addSection_notExists_exception() {
        // given

        // when

        // then
    }

    @DisplayName("노선의 상행종점을 삭제할 수 있다.")
    @Test
    void deleteStation_firstStation_test() {
        // given

        // when

        // then
    }

    @DisplayName("노선의 중간역을 삭제하면 해당 역을 상행역으로 갖는 구간이 삭제된다.")
    @Test
    void deleteStation_middleStation_test() {
        // given

        // when

        // then
    }

    @DisplayName("노선의 역을 삭제할때, 현재 구간이 한개라면 IllegalStatementException 발생한다.")
    @Test
    void deleteStation_onlyOneSection_exception() {
        // given

        // when

        // then
    }

}
