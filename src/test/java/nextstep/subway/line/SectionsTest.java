package nextstep.subway.line;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class SectionsTest {

    private Station upStation;
    private Station downStation;
    private Line line;

    @BeforeEach
    void setUp() {
        upStation = new Station("건대역");
        downStation = new Station("용마산역");
        line = new Line("7호선", "bg-red-600", upStation, downStation, 10);
    }

    @DisplayName("라인을 생성시 구간을 일급 콜렉션으로 넣어줌")
    @Test
    void createSections() {
        //then
        assertAll(
                () -> assertThat(line.getSections()).isNotNull(),
                () -> assertThat(line.getSortedStations()).isEqualTo(Arrays.asList(upStation, downStation))
        );
    }

    @DisplayName("구간 추가시 기존 구간 변경 검증")
    @Test
    void addSection() {
        Station nextDownStation = new Station(3L, "뚝섬유원지역");
        Section nextSection = new Section(line, upStation, nextDownStation, 3);

        //when
        line.addSection(nextSection);

        //then
        assertAll(
                () -> assertThat(line.getSections().size()).isEqualTo(2),
                () -> assertThat(line.getSections().get(0).getDistance()).isEqualTo(7)
        );
    }

    @DisplayName("새로운 역을 상행 종점으로 등록 검증")
    @Test
    void addAscendingStation() {
        Station newUpStation = new Station(3L, "뚝섬유원지역");
        Section nextSection = new Section(line, newUpStation, upStation, 4);

        //when
        line.addSection(nextSection);

        //then
        assertAll(
                () -> assertThat(line.getSections().size()).isEqualTo(2),
                () -> assertThat(line.getSections().get(0).getDistance()).isEqualTo(10)
        );
    }

    @DisplayName("기존 역 사이 길이보다 크거나 같으면 등록 할 수 없음")
    @Test
    void distanceErrorValid() {
        Station newDownStation = new Station(3L, "뚝섬유원지역");
        Section nextSection = new Section(line, upStation, newDownStation, 10);

        assertThatThrownBy(() -> {
            line.addSection(nextSection);
        }).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }

    @DisplayName("이미 등록되어 있어서 등록 할 수 없음")
    @Test
    void alreadyAddedValid() {
        Section nextSection = new Section(line, upStation, downStation, 10);

        assertThatThrownBy(() -> {
            line.addSection(nextSection);
        }).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("이미 등록된 구간 입니다.");
    }

    @DisplayName("상행역과 하행역 둘 중 하나도 포함되어 있지 않을때 ")
    @Test
    void validNotAdded() {
        Station newUpStation = new Station(3L, "뚝섬유원지역");
        Station newDownStation = new Station(4L, "중곡역");
        Section nextSection = new Section(line, newUpStation, newDownStation, 5);

        assertThatThrownBy(() -> {
            line.addSection(nextSection);
        }).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("등록할 수 없는 구간 입니다.");
    }

}
