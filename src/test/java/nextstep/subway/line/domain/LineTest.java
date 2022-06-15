package nextstep.subway.line.domain;

import nextstep.subway.exception.InvalidDistanceException;
import nextstep.subway.exception.InvalidSectionException;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.station.dto.StationResponses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        StationResponses responses = new StationResponses(line);

        // then
        assertThat(responses.getList()).containsExactly(
                new StationResponse(A역.getId(), A역.getName()),
                new StationResponse(B역.getId(), B역.getName()));
    }

    @DisplayName("구간 응답 객체 생성")
    @Test
    void findLineResponse() {
        // given
        Line line = new Line("2호선", "green", A역, B역, 10);

        // when
        LineResponse lineResponse = LineResponse.of(line);

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
        line.addSection(new Section(B역, C역, 3));
        line.addSection(new Section(D역, E역, 3));
        line.addSection(new Section(E역, F역, 10));

        // then
        StationResponses responses = new StationResponses(line);
        assertThat(responses.getList()).containsExactly(
                new StationResponse(A역.getId(), A역.getName()),
                new StationResponse(B역.getId(), B역.getName()),
                new StationResponse(C역.getId(), C역.getName()),
                new StationResponse(D역.getId(), D역.getName()),
                new StationResponse(E역.getId(), E역.getName()),
                new StationResponse(F역.getId(), F역.getName()));
    }

    @DisplayName("중간 지하철 역 삭제")
    @Test
    void deleteStation_middle() {
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
    void deleteStation_head() {
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
    void deleteStation_tail() {
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
    void deleteStation_exception01() {
        // given
        Line line = new Line("2호선", "초록", A역, C역, 15);

        // when, then
        assertThatThrownBy(() -> {
            line.deleteStation(A역);
        }).isInstanceOf(InvalidSectionException.class);
    }

    @DisplayName("하나만 남은 구간은 삭제 불가")
    @Test
    void deleteStation_exception02() {
        // given
        Line line = new Line("2호선", "초록", A역, C역, 15);

        // when, then
        assertThatThrownBy(() -> {
            line.deleteStation(C역);
        }).isInstanceOf(InvalidSectionException.class);
    }

    @DisplayName("기존 구간의 길이를 넘어서는 구간은 추가 불가")
    @Test
    void addSection_exception01() {
        // given
        Line line = new Line("2호선", "초록", A역, C역, 10);

        // when
        assertThatThrownBy(() -> {
            line.addSection(new Section(B역, C역, 13));
        }).isInstanceOf(InvalidDistanceException.class);
    }

    @DisplayName("노선에 이미 추가된 지하철 역 구간 추가")
    @Test
    void addSection_exception02() {
        // given
        Line line = new Line("2호선", "초록", A역, C역, 10);

        // when
        assertThatThrownBy(() -> {
            line.addSection(new Section(A역, C역, 5));
        }).isInstanceOf(InvalidSectionException.class);
    }

    @DisplayName("노선에 모두 추가 안 된 지하철 역 구간 추가")
    @Test
    void addSection_exception03() {
        // given
        Line line = new Line("2호선", "초록", A역, B역, 10);

        // when
        assertThatThrownBy(() -> {
            line.addSection(new Section(C역, D역, 10));
        }).isInstanceOf(InvalidSectionException.class);
    }
}
