package nextstep.subway.line.domain;

import nextstep.subway.exception.InvalidSectionException;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineTest {
    private Station A역;
    private Station B역;
    private Station C역;
    private Station D역;
    private Station E역;
    private Station F역;

    @BeforeEach
    void setUp() {
        A역 = new Station(1L, "A역");
        B역 = new Station(2L, "B역");
        C역 = new Station(3L, "C역");
        D역 = new Station(4L, "D역");
        E역 = new Station(5L, "E역");
        F역 = new Station(6L, "F역");
    }

    @DisplayName("구간 지하철 역 응답 객체 생성")
    @Test
    void findStationResponses() {
        // given
        Line line = new Line("2호선", "green", A역, B역, 10);

        // when
        List<StationResponse> responses = line.findStationResponses();

        // then
        assertThat(responses).containsExactly(
                new StationResponse(A역.getId(), A역.getName()),
                new StationResponse(B역.getId(), B역.getName()));
    }

    @DisplayName("구간 응답 객체 생성")
    @Test
    void findLineResponse() {
        // given
        Line line = new Line("2호선", "green", A역, B역, 10);

        // when
        LineResponse lineResponse = line.findLineResponse();

        // then
        assertThat(lineResponse.getName()).isEqualTo("2호선");
        assertThat(lineResponse.getColor()).isEqualTo("green");
        assertThat(lineResponse.getStations()).containsExactly(
                new StationResponse(A역.getId(), A역.getName()),
                new StationResponse(B역.getId(), B역.getName()));
    }

    @DisplayName("구간 추가 기능 테스트")
    @Test
    void addSection() {
        // given
        Line line = new Line("2호선", "green", B역, E역, 10);

        // when
        line.addSection(new Section(A역, B역, 10));

        List<StationResponse> responses = line.findStationResponses();
        System.out.println(responses);

        line.addSection(new Section(B역, C역, 3));

        responses = line.findStationResponses();
        System.out.println(responses);

        line.addSection(new Section(D역, E역, 3));

        responses = line.findStationResponses();
        System.out.println(responses);

        line.addSection(new Section(E역, F역, 10));

        responses = line.findStationResponses();
        System.out.println(responses);

        // then
        responses = line.findStationResponses();
        assertThat(responses).containsExactly(
                new StationResponse(A역.getId(), A역.getName()),
                new StationResponse(B역.getId(), B역.getName()),
                new StationResponse(C역.getId(), C역.getName()),
                new StationResponse(D역.getId(), D역.getName()),
                new StationResponse(E역.getId(), E역.getName()),
                new StationResponse(F역.getId(), F역.getName()));
    }

    @DisplayName("중간 지하철 역 삭제")
    @Test
    void deleteStation01() {
        // given
        Line line = new Line("2호선", "초록", A역, C역, 15);

        // when
        line.addSection(new Section(B역, C역, 5));
        line.deleteStation(B역);

        // then
        Sections sections = line.getSections();
        assertThat(sections.getList()).hasSize(1);
    }

    @DisplayName("상행 종점역 지하철 역삭제")
    @Test
    void deleteStation02() {
        // given
        Line line = new Line("2호선", "초록", A역, C역, 15);
        line.addSection(new Section(B역, C역, 5));

        // when
        line.deleteStation(A역);

        // then
        Sections sections = line.getSections();
        assertThat(sections.getList()).hasSize(1);
    }

    @DisplayName("하행 종점역 지하철 역삭제")
    @Test
    void deleteStation03() {
        // given
        Line line = new Line("2호선", "초록", A역, C역, 15);
        line.addSection(new Section(B역, C역, 5));

        // when
        line.deleteStation(C역);

        // then
        Sections sections = line.getSections();
        assertThat(sections.getList()).hasSize(1);
    }

    @DisplayName("하나만 남은 구간은 삭제 불가")
    @Test
    void deleteStation04() {
        // given
        Line line = new Line("2호선", "초록", A역, C역, 15);

        // when, then
        assertThatThrownBy(() -> {
            line.deleteStation(A역);
        }).isInstanceOf(InvalidSectionException.class);
    }

    @DisplayName("하나만 남은 구간은 삭제 불가")
    @Test
    void deleteStation05() {
        // given
        Line line = new Line("2호선", "초록", A역, C역, 15);

        // when, then
        assertThatThrownBy(() -> {
            line.deleteStation(C역);
        }).isInstanceOf(InvalidSectionException.class);
    }
}
