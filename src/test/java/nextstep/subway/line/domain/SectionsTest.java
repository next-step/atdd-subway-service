package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 구간 테스트")
public class SectionsTest {

    //지하철역
    private static final Station 공덕역 = new Station("공덕역");
    private static final Station 애오개역 = new Station("애오개역");
    private static final Station 충정로역 = new Station("충정로역");
    private static final Station 서대문역 = new Station("서대문역");

    //노선
    private static final Line 오호선 = new Line("5호선", "purple");

    //구간
    private static final Section firstSection = new Section(오호선, 공덕역, 애오개역, 10);
    private static final Section secondSection = new Section(오호선, 애오개역, 충정로역, 10);
    private static final Section thirdSection = new Section(오호선, 충정로역, 서대문역, 10);

    @DisplayName("구간추가시 연결이 불가능한 구간일 경우 예외 발생")
    @Test
    void addFail() {
        // given
        Sections sections = new Sections();
        sections.add(firstSection);

        // when & then
        assertThatThrownBy(() -> sections.add(thirdSection))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
