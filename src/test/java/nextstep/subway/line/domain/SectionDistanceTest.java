package nextstep.subway.line.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SectionDistanceTest {
    private SectionDistance distance;

    @BeforeEach
    public void setUp() {
        distance = new SectionDistance(10);
    }

    @DisplayName("구간거리 생성 예외")
    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void 생성자_유효성_체크(int distance) {
        // when
        // then
        assertThatThrownBy(() -> new SectionDistance(distance)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("구간거리 생성 확인")
    @ParameterizedTest
    @ValueSource(ints = {1, Integer.MAX_VALUE})
    void 생성자_생성(int distance) {
        // when
        SectionDistance sectionDistance = new SectionDistance(distance);

        // then
        assertThat(sectionDistance).isEqualTo(new SectionDistance(distance));
    }

    @DisplayName("거리 변경")
    @Test
    public void 거리_변경_확인() throws Exception {
        //when
        distance.updateDistance(3);

        //then
        assertThat(distance.getDistance()).isEqualTo(7);
    }

    @DisplayName("거리 변경 예외 - 역과 역 사이의 거리보다 넓은 거리 변경")
    @ParameterizedTest
    @ValueSource(ints = {10, 11})
    public void 거리_변경_예외1(int newDistance) throws Exception {
        //when
        //then
        assertThatThrownBy(() -> distance.updateDistance(newDistance))
                .hasMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("거리 변경 예외 - 구간 거리 0 이하로 변경")
    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    public void 거리_변경_예외2(int newDistance) throws Exception {
        //when
        //then
        assertThatThrownBy(() -> distance.updateDistance(newDistance))
                .hasMessage("구간 길이는 1 이상이어야 합니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }
}
