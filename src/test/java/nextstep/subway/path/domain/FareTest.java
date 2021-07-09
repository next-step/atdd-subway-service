package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class FareTest {
    private LoginMember loginMember;
    private Fare fare;
    private Path path;

    @BeforeEach
    public void setUp() {
        Line line = new Line(1L, "일호선", "남색", 0);
        ShortestDistance distance = new ShortestDistance(5);
        loginMember = new LoginMember(1L, "test@test.com", 21);
        fare = new Fare();
        Station downStation = new Station(1L, "하행역");
        Station upStation = new Station(2L, "상행역");
        Section section = new Section(1L, upStation, downStation, 5);
        line.addSection(section);
        path = new Path(Arrays.asList(upStation, downStation), distance);
    }

    @DisplayName("요금 계산 - 10km 이하")
    @ParameterizedTest
    @ValueSource(ints = {0, 10})
    public void 기본운임10km이하_요금계산_확인(int distance) throws Exception {
        //given
        ShortestDistance shortestDistance = new ShortestDistance(distance);

        //when
        fare.calculate(path, loginMember);

        //then
        assertThat(fare).isEqualTo(new Fare());
    }

    @DisplayName("요금 계산 - 10km 초과 50km 이하")
    @ParameterizedTest
    @CsvSource(value = {"11,1350", "15,1350", "50,2050"})
    public void 추가운임10km초과50km이하_요금계산_확인(int distance, int expected) throws Exception {
        //given
        ShortestDistance shortestDistance = new ShortestDistance(distance);
        Path path = this.path.changeDistance(shortestDistance);

        //when
        fare.calculate(path, loginMember);

        //then
        assertThat(fare).isEqualTo(new Fare(expected));
    }

    @DisplayName("요금 계산 - 50km 초과")
    @ParameterizedTest
    @CsvSource(value = {"51,2150", "58,2150", "59,2250"})
    public void 추가운임150km초과_요금계산_확인(int distance, int expected) throws Exception {
        //given
        ShortestDistance shortestDistance = new ShortestDistance(distance);
        Path path = this.path.changeDistance(shortestDistance);

        //when
        fare.calculate(path, loginMember);

        //then
        assertThat(fare).isEqualTo(new Fare(expected));
    }

    @DisplayName("요금 계산 - 노선 추가 운임 - 노선이 하나인 경우")
    @Test
    public void 노선추가운임_요금계산_확인() throws Exception {
        //given
        Line line = new Line(1L, "일호선", "남색", 500);
        Station downStation = new Station(1L, "하행역");
        Station upStation = new Station(2L, "상행역");
        Section section = new Section(1L, upStation, downStation, 5);
        line.addSection(section);
        Path path = this.path.changeStations(Arrays.asList(upStation, downStation));

        //when
        fare.calculate(path, loginMember);

        //then
        assertThat(fare).isEqualTo(new Fare(1_750));
    }

    @DisplayName("요금 계산 - 청소년 할인")
    @ParameterizedTest
    @ValueSource(ints = {13, 18})
    public void 청소년할인_요금계산_확인(int age) throws Exception {
        //given
        LoginMember loginMember = new LoginMember(1L, "test@test.com", age);

        //when
        fare.calculate(path, loginMember);

        //then
        assertThat(fare).isEqualTo(new Fare(720));
    }

    @DisplayName("요금 계산 - 어린이 할인")
    @ParameterizedTest
    @ValueSource(ints = {6, 12})
    public void 어린이할인_요금계산이확인(int age) throws Exception {
        //given
        LoginMember loginMember = new LoginMember(1L, "test@test.com", age);

        //when
        fare.calculate(path, loginMember);

        //then
        assertThat(fare).isEqualTo(new Fare(450));
    }
}
