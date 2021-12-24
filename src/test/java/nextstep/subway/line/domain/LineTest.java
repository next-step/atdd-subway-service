package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("노선 관련 기능")
public class LineTest {

    @DisplayName("노선 생성")
    @Test
    public void 노선_생성() {
        Line 신분당선 = new Line("신분당선", "bg-red-600");

        assertThat(신분당선).isNotNull();
    }

    @DisplayName("구간포함된 라인 생성")
    @Test
    public void 구간포함된_라인_생성() {
        // given
        Station 강남역 = new Station("강남역");
        Station 교대역 = new Station("교대역");

        // when
        Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 교대역, 10);

        // then
        assertThat(신분당선).isNotNull();
    }

    @DisplayName("노선에 구간 추가")
    @Test
    public void 노선에_구간_추가() {
        // given
        Station 강남역 = new Station("강남역");
        Station 교대역 = new Station("교대역");
        Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 교대역, 10);


        // when
        Station 양재역 = new Station("양재역");
        Section 새로운구간 = Section.of(신분당선, 강남역, 양재역, Distance.of(3));
        신분당선.addSection(새로운구간);

        // then
        assertAll(
                () -> assertThat(신분당선.getSections().getSections()).hasSize(2),
                () -> assertThat(신분당선.getSections().getSections()
                        .stream()
                        .anyMatch(section -> section.getUpStation().equals(양재역) || section.getDownStation().equals(양재역)))
        );
    }

    @DisplayName("노선에 구간 추가_실패")
    @Test
    public void 노선에_구간_추가_실패() {
        // given
        Station 강남역 = new Station("강남역");
        Station 교대역 = new Station("교대역");
        Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 교대역, 10);


        // when
        Section 새로운구간 = Section.of(신분당선, 강남역, 교대역, Distance.of(15));

        // then
        assertThatThrownBy(() -> 신분당선.addSection(새로운구간))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("연결할 수 없는 역이 포함되어 있습니다.");
    }

    @DisplayName("노선에서 역 제거")
    @Test
    public void 노선에서_역_제거() {
        // given
        Station 강남역 = new Station("강남역");
        Station 교대역 = new Station("교대역");
        Station 양재역 = new Station("양재역");
        Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 교대역, 10);
        Section 새로운구간 = Section.of(신분당선, 강남역, 양재역, Distance.of(3));
        신분당선.addSection(새로운구간);

        // when
        신분당선.deleteStation(양재역);

        // then
        assertAll(
                () -> assertThat(신분당선.getSections().getSections()).hasSize(1),
                () -> assertThat(신분당선.getSections().getSections()
                        .stream()
                        .noneMatch(section -> section.getUpStation().equals(양재역) || section.getDownStation().equals(양재역)))
        );
    }

    @DisplayName("노선에서 역 제거 실패")
    @Test
    public void 노선에서_역_제거_실패() {
        // given
        Station 강남역 = new Station("강남역");
        Station 교대역 = new Station("교대역");
        Station 양재역 = new Station("양재역");
        Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 교대역, 10);
        Section 새로운구간 = Section.of(신분당선, 강남역, 양재역, Distance.of(3));
        신분당선.addSection(새로운구간);

        // when
        // then
        assertThatThrownBy(() -> 신분당선.deleteStation(강남역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("삭제할 수 없는 역입니다.");
    }

}
