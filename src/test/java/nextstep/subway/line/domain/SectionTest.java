package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nextstep.subway.exception.NotValidateException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("구간 도메인 테스트")
public class SectionTest {

    private final Station 서울역 = new Station("서울역");
    private final Station 남영역 = new Station("남영역");
    private final Station 용산역 = new Station("용산역");

    @Test
    @DisplayName("상행-하행 구분 테스트")
    void isTheUpDownLine() {

        Section 서울_남영 = Section.create(서울역, 남영역, Distance.valueOf(5));
        Section 남영_용산 = Section.create(남영역, 용산역, Distance.valueOf(10));

        assertAll(() -> {
            assertTrue(서울_남영.isTheUpLine(남영_용산));
            assertFalse(서울_남영.isTheDownLine(남영_용산));
            assertFalse(남영_용산.isTheUpLine(서울_남영));
            assertTrue(남영_용산.isTheDownLine(서울_남영));
        });
    }


    @Test
    @DisplayName("다른 구간 체크 테스트")
    void isDifferentSection() {

        Section 서울_남영 = Section.create(서울역, 남영역, Distance.valueOf(5));
        Section 남영_용산 = Section.create(남영역, 용산역, Distance.valueOf(10));

        assertAll(() -> {
            assertFalse(서울_남영.equalsStations(남영_용산));
            assertFalse(남영_용산.equalsStations(서울_남영));
        });
    }

    @Test
    @DisplayName("같은 구간 체크 테스트")
    void isSameSection() {

        Section 서울_남영_5 = Section.create(서울역, 남영역, Distance.valueOf(5));
        Section 서울_남영_10 = Section.create(서울역, 남영역, Distance.valueOf(10));

        assertAll(() -> {
            assertTrue(서울_남영_5.equalsStations(서울_남영_10));
            assertTrue(서울_남영_10.equalsStations(서울_남영_5));
        });
    }

    @Test
    @DisplayName("기존 구간의 상행역, 길이 수정")
    void updateUpStationBySection() {
        Section 서울_남영 = Section.create(서울역, 남영역, Distance.valueOf(5));
        Section 서울_용산 = Section.create(서울역, 용산역, Distance.valueOf(10));

        서울_용산.updateUpStationBySection(서울_남영);

        assertAll(() -> {
            assertThat(서울_용산.isUpStation(남영역)).isTrue();
            assertTrue(서울_용산.equalsDistance(5));
        });
    }

    @Test
    @DisplayName("기존 구간의 하행역, 길이 수정")
    void updateDownStationBySection() {
        Section 남영_용산 = Section.create(남영역, 용산역, Distance.valueOf(5));
        Section 서울_용산 = Section.create(서울역, 용산역, Distance.valueOf(10));

        서울_용산.updateDownStationBySection(남영_용산);

        assertAll(() -> {
            assertThat(서울_용산.isDownStation(남영역)).isTrue();
            assertTrue(서울_용산.equalsDistance(5));
        });
    }

    @Test
    @DisplayName("기존 구간보다 긴 길이로 수정 시 NotValidateException 발생")
    void updateDownStationBySectionLongerFail() {
        Section 남영_용산 = Section.create(남영역, 용산역, Distance.valueOf(12));
        Section 서울_용산 = Section.create(서울역, 용산역, Distance.valueOf(10));

        assertThatThrownBy(() -> 서울_용산.updateDownStationBySection(남영_용산))
            .isInstanceOf(NotValidateException.class)
            .hasMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요.");
    }

    @Test
    @DisplayName("기존 구간와 같은 길이로 수정 시 NotValidateException 발생")
    void updateDownStationBySectionSameFail() {
        Section 남영_용산 = Section.create(남영역, 용산역, Distance.valueOf(10));
        Section 서울_용산 = Section.create(서울역, 용산역, Distance.valueOf(10));

        assertThatThrownBy(() -> 서울_용산.updateDownStationBySection(남영_용산))
            .isInstanceOf(NotValidateException.class)
            .hasMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요.");
    }
}
