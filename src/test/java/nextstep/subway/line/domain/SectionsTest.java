package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {

    private Line line;
    private Station 강남역;
    private Station 광교역;
    private Station 판교역;

    @BeforeEach
    private void setUp() {
        강남역 = Station.of("강남역");
        광교역 = Station.of("광교역");
        판교역 = Station.of("판교역");
        line = Line.from("신분당선", "bg-red-600", 강남역, 광교역, 10);
    }

    @Test
    @DisplayName("구간 중간에 새로운 구간이 상행역 기준으로 추가되는 경우, 기존 구간의 상행역과 거리가 수정된다.")
    void 구간_중간에_새_구간_추가() {
        // given
        Sections sections = line.getSections();

        // when
        sections.connect(Section.of(line, 강남역, 판교역, 3));

        // then
        assertAll(
            () -> assertThat(sections.getSections()).hasSize(2),
            () -> assertThat(sections.getSections().get(0).getUpStation()).isEqualTo(
                Station.of("판교역")),
            () -> assertThat(sections.getSections().get(0).getDownStation()).isEqualTo(
                Station.of("광교역")),
            () -> assertThat(sections.getSections().get(0).getDistance()).isEqualTo(7),
            () -> assertThat(sections.getSections().get(1).getUpStation()).isEqualTo(
                Station.of("강남역")),
            () -> assertThat(sections.getSections().get(1).getDownStation()).isEqualTo(
                Station.of("판교역")),
            () -> assertThat(sections.getSections().get(1).getDistance()).isEqualTo(3)
        );
    }

    @Test
    @DisplayName("구간 중간에 새로운 구간이 하행역 기준으로 추가되는 경우, 기존 구간의 하행역과 거리가 수정된다.")
    void 구간_중간에_새_구간_추가2() {
        // given
        Sections sections = line.getSections();

        // when
        sections.connect(Section.of(line, 판교역, 광교역, 3));

        assertAll(
            () -> assertThat(sections.getSections()).hasSize(2),
            () -> assertThat(sections.getSections().get(0).getUpStation()).isEqualTo(
                Station.of("강남역")),
            () -> assertThat(sections.getSections().get(0).getDownStation()).isEqualTo(
                Station.of("판교역")),
            () -> assertThat(sections.getSections().get(0).getDistance()).isEqualTo(7),
            () -> assertThat(sections.getSections().get(1).getUpStation()).isEqualTo(
                Station.of("판교역")),
            () -> assertThat(sections.getSections().get(1).getDownStation()).isEqualTo(
                Station.of("광교역")),
            () -> assertThat(sections.getSections().get(1).getDistance()).isEqualTo(3)
        );
    }

    @Test
    @DisplayName("상행 종점에 새로운 구간이 추가되는 경우 추가만 된다")
    void 상행_종점에_새_구간_추가() {
        // given
        Sections sections = line.getSections();

        // when
        sections.connect(Section.of(line, 판교역, 강남역, 3));

        // then
        assertAll(
            () -> assertThat(sections.getSections()).hasSize(2),
            () -> assertThat(sections.getSections().get(0).getUpStation()).isEqualTo(
                Station.of("강남역")),
            () -> assertThat(sections.getSections().get(0).getDownStation()).isEqualTo(
                Station.of("광교역")),
            () -> assertThat(sections.getSections().get(0).getDistance()).isEqualTo(10),
            () -> assertThat(sections.getSections().get(1).getUpStation()).isEqualTo(
                Station.of("판교역")),
            () -> assertThat(sections.getSections().get(1).getDownStation()).isEqualTo(
                Station.of("강남역")),
            () -> assertThat(sections.getSections().get(1).getDistance()).isEqualTo(3)
        );
    }

    @Test
    @DisplayName("하행 종점에 새로운 구간이 추가되는 경우 추가만 된다")
    void 하행_종점에_새_구간_추가() {
        // given
        Sections sections = line.getSections();

        // when
        sections.connect(Section.of(line, 광교역, 판교역, 3));

        // then
        assertAll(
            () -> assertThat(sections.getSections()).hasSize(2),
            () -> assertThat(sections.getSections().get(0).getUpStation()).isEqualTo(
                Station.of("강남역")),
            () -> assertThat(sections.getSections().get(0).getDownStation()).isEqualTo(
                Station.of("광교역")),
            () -> assertThat(sections.getSections().get(0).getDistance()).isEqualTo(10),
            () -> assertThat(sections.getSections().get(1).getUpStation()).isEqualTo(
                Station.of("광교역")),
            () -> assertThat(sections.getSections().get(1).getDownStation()).isEqualTo(
                Station.of("판교역")),
            () -> assertThat(sections.getSections().get(1).getDistance()).isEqualTo(3)
        );
    }

    @Test
    @DisplayName("역 사이에 새로운 역을 상행역 기준으로 등록할 경우 기존 역 사이 길이보다 크거나 같으면 예외 발생")
    void 구간_추가_예외_케이스1() {
        // given
        Sections sections = line.getSections();

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
                () -> sections.connect(Section.of(line, 강남역, 판교역, 10))
            )
            .withMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }

    @Test
    @DisplayName("역 사이에 새로운 역을 하행역 기준으로 등록할 경우 기존 역 사이 길이보다 크거나 같으면 예외 발생")
    void 구간_추가_예외_케이스1_2() {
        // given
        Sections sections = line.getSections();

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
                () -> sections.connect(Section.of(line, 판교역, 광교역, 10))
            )
            .withMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }

    @Test
    @DisplayName("상행역과 하행역이 이미 노선에 모두 등록된 경우 예외 발생")
    void 구간_추가_예외_케이스2() {
        // given
        Sections sections = line.getSections();

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
                () -> sections.connect(Section.of(line, 강남역, 광교역, 5))
            )
            .withMessage("이미 등록된 구간 입니다.");
    }

    @Test
    @DisplayName("상행역과 하행역 둘 중 하나도 포함되지 않은 경우 예외 발생")
    void 구간_추가_예외_케이스3() {
        // given
        Sections sections = line.getSections();

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
                () -> sections.connect(Section.of(line, Station.of("양재역"), Station.of("앵재시민의숲역"), 5))
            )
            .withMessage("등록할 수 없는 구간 입니다.");
    }

    @Test
    @DisplayName("1->3->2 두 개의 구간이 있을 때, 3번역 삭제 테스트")
    void 구간_삭제() {
        // given
        Sections sections = line.getSections();
        sections.connect(Section.of(line, 강남역, 판교역, 3));

        // when
        sections.remove(Station.of("판교역"));

        // then
        assertThat(sections.getSections()).hasSize(1);
        assertThat(sections.getSections().get(0).getUpStation()).isEqualTo(Station.of("강남역"));
        assertThat(sections.getSections().get(0).getDownStation()).isEqualTo(Station.of("광교역"));
        assertThat(sections.getSections().get(0).getDistance()).isEqualTo(10);
    }

    @Test
    @DisplayName("1->2->3 두 개의 구간이 있을 때, 1번역 삭제 테스트")
    void 상행_종점_구간_삭제() {
        // given
        Sections sections = line.getSections();
        sections.connect(Section.of(line, 광교역, 판교역, 3));

        // when
        sections.remove(Station.of("강남역"));

        // then
        assertThat(sections.getSections()).hasSize(1);
        assertThat(sections.getSections().get(0).getUpStation()).isEqualTo(Station.of("광교역"));
        assertThat(sections.getSections().get(0).getDownStation()).isEqualTo(Station.of("판교역"));
        assertThat(sections.getSections().get(0).getDistance()).isEqualTo(3);
    }

    @Test
    @DisplayName("1->2->3 두 개의 구간이 있을 때, 3번역 삭제 테스트")
    void 하행_종점_구간_삭제() {
        // given
        Sections sections = line.getSections();
        sections.connect(Section.of(line, 광교역, 판교역, 3));

        // when
        sections.remove(Station.of("판교역"));

        // then
        assertThat(sections.getSections()).hasSize(1);
        assertThat(sections.getSections().get(0).getUpStation()).isEqualTo(Station.of("강남역"));
        assertThat(sections.getSections().get(0).getDownStation()).isEqualTo(Station.of("광교역"));
        assertThat(sections.getSections().get(0).getDistance()).isEqualTo(10);
    }

    @Test
    @DisplayName("노선에 없는 역 삭제시 예외 발생")
    void 구간_삭제_예외1() {
        // given
        Sections sections = line.getSections();

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
                () -> sections.remove(Station.of("양재역"))
            )
            .withMessage("노선에 존재하지 않는 역입니다.");
    }

    @Test
    @DisplayName("마지막 구역만 있을 때, 삭제시 예외 발생")
    void 구간_삭제_예외2() {
        // given
        Line line = Line.from("신분당선", "bg-red-600", 강남역, 광교역, 10);
        Sections sections = line.getSections();

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
                () -> sections.remove(Station.of("강남역"))
            )
            .withMessage("마지막 구간은 삭제할 수 없습니다.");
    }
}