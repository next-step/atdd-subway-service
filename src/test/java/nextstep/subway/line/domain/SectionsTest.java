package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.SectionTest.*;
import static nextstep.subway.station.domain.StationTest.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.exception.CannotRemoveException;
import nextstep.subway.line.exception.InvalidSectionException;
import nextstep.subway.station.exception.NoSuchStationException;

@DisplayName("Sections 단위 테스트")
class SectionsTest {

    Sections 강남_양재;
    Sections 강남_양재_광교중앙;
    Sections 강남_양재_광교중앙_광교;

    Sections 강남_광교중앙;

    @BeforeEach
    void setUp() {
        강남_양재 = new Sections();
        강남_양재.addSection(강남_양재_100);
        강남_양재_광교중앙 = new Sections();
        강남_양재_광교중앙.addSection(강남_양재_100);
        강남_양재_광교중앙.addSection(양재_광교중앙_30);
        강남_양재_광교중앙_광교 = new Sections();
        강남_양재_광교중앙_광교.addSection(강남_양재_100);
        강남_양재_광교중앙_광교.addSection(양재_광교중앙_30);
        강남_양재_광교중앙_광교.addSection(광교중앙_광교_30);
        강남_광교중앙 = new Sections();
        강남_광교중앙.addSection(강남_광교중앙_130);
    }

    @Test
    @DisplayName("구간들의 역을 상행역 순으로 가져온다")
    void getStationsInOrder() {
        assertThat(강남_양재_광교중앙_광교.getStations())
            .containsExactly(강남역, 양재역, 광교중앙역, 광교역);
    }

    @Test
    @DisplayName("구간을 중간에 추가하고 결과를 확인한다")
    void addSection() {
        강남_광교중앙.addSection(양재_광교중앙_30);
        assertThat(강남_광교중앙.getStations())
            .containsExactly(강남역, 양재역, 광교중앙역);
    }

    @Test
    @DisplayName("구간을 끝에 추가하고 결과를 확인한다")
    void addSection_addSide() {
        강남_양재.addSection(양재_광교중앙_30);
        assertThat(강남_양재.getStations())
            .containsExactly(강남역, 양재역, 광교중앙역);
    }

    @Test
    @DisplayName("구간 추가 실패케이스를 검증한다")
    void addSection_Failed() {
        assertThatThrownBy(() -> 강남_양재.addSection(null))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> 강남_양재.addSection(광교중앙_광교_30))
            .isInstanceOf(InvalidSectionException.class);
        assertThatThrownBy(() -> 강남_양재.addSection(강남_양재_100))
            .isInstanceOf(InvalidSectionException.class);
    }

    @Test
    @DisplayName("구간과 구간 사이의 역을 제거한다")
    void removeStation_removeMiddle() {
        강남_양재_광교중앙.removeStation(양재역);
        assertThat(강남_양재_광교중앙.getStations())
            .containsExactly(강남역, 광교중앙역);
    }

    @Test
    @DisplayName("구간들의 양 끝 역을 삭제한다")
    void removeStation_removeSide() {
        강남_양재_광교중앙_광교.removeStation(광교역);
        assertThat(강남_양재_광교중앙_광교.getStations())
            .containsExactly(강남역, 양재역, 광교중앙역);

        강남_양재_광교중앙.removeStation(강남역);
        assertThat(강남_양재_광교중앙.getStations())
            .containsExactly(양재역, 광교중앙역);
    }

    @Test
    @DisplayName("구간 삭제 실페케이스를 검증한다")
    void removeStation_Failed() {
        assertThatThrownBy(() -> 강남_양재.removeStation(강남역))
            .isInstanceOf(CannotRemoveException.class);
        assertThatThrownBy(() -> 강남_양재.removeStation(광교역))
            .isInstanceOf(NoSuchStationException.class);
    }
}
