package nextstep.subway.line.domain;

import static nextstep.subway.station.domain.StationTest.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Section 단위 테스트")
public class SectionTest {

    public static final Section 강남_양재_100 = new Section(1L, 강남역, 양재역, 100);
    public static final Section 양재_광교중앙_30 = new Section(2L, 양재역, 광교중앙역, 30);
    public static final Section 광교중앙_광교_30 = new Section(3L, 광교중앙역, 광교역, 30);

    public static final Section 강남_광교중앙_130 = new Section(4L, 강남역, 광교중앙역, 130);


    @Test
    @DisplayName("두 구간의 거리를 비교한다")
    void isLongerThan() {
        // 노선순서: 강남-양재-광교중앙-광교
        assertThat(강남_양재_100.isLongerThan(양재_광교중앙_30)).isTrue();
        assertThat(양재_광교중앙_30.isLongerThan(광교중앙_광교_30)).isFalse();
    }

    @Test
    @DisplayName("역을 포함하고 있는지 확인한다")
    void contains() {
        // 노선순서: 강남-양재-광교중앙-광교
        assertThat(강남_양재_100.contains(강남역)).isTrue();
        assertThat(강남_양재_100.contains(광교역)).isFalse();
    }

    @Test
    @DisplayName("두 구간이 한쪽끝만 동일한지 확인한다")
    void matchesOnlyOneEndOf() {
        // 노선순서: 강남-양재-광교중앙-광교
        assertThat(강남_양재_100.matchesOnlyOneEndWith(강남_광교중앙_130)).isTrue();
        assertThat(강남_광교중앙_130.matchesOnlyOneEndWith(강남_양재_100)).isTrue();
        assertThat(강남_양재_100.matchesOnlyOneEndWith(강남_양재_100)).isFalse();
        assertThat(강남_양재_100.matchesOnlyOneEndWith(광교중앙_광교_30)).isFalse();
    }

    @Test
    @DisplayName("다른 구간에 의해 Shift 된 결과를 확인한다")
    void updateSection() {
        // 노선순서: 강남-양재-광교중앙-광교
        Section newSection = 강남_광교중앙_130.shiftedBy(강남_양재_100);

        assertThat(newSection.getDistance()).isEqualTo(30);
        assertThat(newSection.getUpStation()).isEqualTo(양재역);
        assertThat(newSection.getDownStation()).isEqualTo(광교중앙역);
    }

    @Test
    @DisplayName("두 구간이 병합가능한지 검증한다")
    void isMergeableWith() {
        // 노선순서: 강남-양재-광교중앙-광교
        assertThat(강남_양재_100.isMergeableWith(양재_광교중앙_30)).isTrue();
        assertThat(양재_광교중앙_30.isMergeableWith(강남_양재_100)).isTrue();
        assertThat(광교중앙_광교_30.isMergeableWith(강남_양재_100)).isFalse();
        assertThat(광교중앙_광교_30.isMergeableWith(광교중앙_광교_30)).isFalse();
    }

    @Test
    @DisplayName("두 구간을 병합하고 결과를 확인한다")
    void mergeWith() {
        // 노선순서: 강남-양재-광교중앙-광교
        Section newSection = 강남_양재_100.mergeWith(양재_광교중앙_30);

        assertThat(newSection.getDistance()).isEqualTo(130);
        assertThat(newSection.getUpStation()).isEqualTo(강남역);
        assertThat(newSection.getDownStation()).isEqualTo(광교중앙역);
    }
}
