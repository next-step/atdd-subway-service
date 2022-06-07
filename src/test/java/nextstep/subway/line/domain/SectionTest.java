package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.DomainFixtureFactory.createSection;
import static nextstep.subway.line.domain.DomainFixtureFactory.createStation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.exception.NotFoundException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionTest {
    @DisplayName("상행 지하철역이 Null 일 경우 예외 테스트")
    @Test
    void createSectionByNullUpStation() {
        Line line = Line.createEmpty();
        Station downStation = createStation(1L, "새로운지하철역");
        assertThatThrownBy(() -> Section.builder(line, null, downStation, Distance.valueOf(10))
                .build())
                .isInstanceOf(NotFoundException.class)
                .hasMessage("상행역 정보가 없습니다.");
    }

    @DisplayName("하행 지하철역이 Null 일 경우 예외 테스트")
    @Test
    void createSectionByNullDownStation() {
        Line line = Line.createEmpty();
        Station upStation = createStation(1L, "지하철역");
        assertThatThrownBy(() -> Section.builder(line, upStation, null, Distance.valueOf(10))
                .build())
                .isInstanceOf(NotFoundException.class)
                .hasMessage("하행역 정보가 없습니다.");
    }

    @DisplayName("상행역이 같은 지하철 신규 구간 등록시 기존 구간 상행역 변경 및 구간거리 테스트")
    @Test
    void updateSectionEqualToUpStation() {
        Line line = Line.createEmpty();
        Station upStation = createStation("지하철역");
        Station downStation = createStation("새로운지하철역");
        Station newStation = createStation("더새로운지하철역");
        Section section = createSection(line, upStation, downStation, Distance.valueOf(10));
        Section newSection = createSection(line, upStation, newStation, Distance.valueOf(5));
        section.update(newSection);
        assertAll(
                () -> assertThat(section.upStation()).isEqualTo(newStation),
                () -> assertThat(section.distance()).isEqualTo(Distance.valueOf(5))
        );
    }

    @DisplayName("하행역이 같은 지하철 신규 구간 등록시 기존 구간 상행역 변경 및 구간거리 테스트")
    @Test
    void updateSectionEqualToDownStation() {
        Line line = Line.createEmpty();
        Station upStation = createStation("지하철역");
        Station downStation = createStation("새로운지하철역");
        Station newStation = createStation("더새로운지하철역");
        Section section = createSection(line, upStation, downStation, Distance.valueOf(10));
        Section newSection = createSection(line, newStation, downStation, Distance.valueOf(5));
        section.update(newSection);
        assertAll(
                () -> assertThat(section.downStation()).isEqualTo(newStation),
                () -> assertThat(section.distance()).isEqualTo(Distance.valueOf(5))
        );
    }
}
