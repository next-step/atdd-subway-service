package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.common.domain.Name;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DataJpaTest
@DisplayName("지하철 노선 저장소")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class LineRepositoryTest {

    private Station 역삼역;
    private Station 강남역;

    @Autowired
    private LineRepository repository;

    @BeforeEach
    void setUp(@Autowired StationRepository repository) {
        강남역 = repository.save(Station.from(Name.from("강남")));
        역삼역 = repository.save(Station.from(Name.from("역삼")));
    }

    @Test
    @DisplayName("저장")
    void 지하철_노선_등록() {
        // given
        Name 신분당 = Name.from("신분당선");
        Color 빨강 = Color.from("red");

        // when
        Line 신분당선 = 지하철_노선_등록(신분당, 빨강);

        // then
        지하철_노선_저장됨(신분당선, 신분당, 빨강);
    }

    @Test
    @DisplayName("중복된 이름으로 저장")
    void save_duplicationName_thrownDataIntegrityViolationException() {
        // given
        Name 신분당 = Name.from("신분당선");
        지하철_노선_이름이_등록되어_있음(신분당, Color.from("red"));

        // when
        ThrowingCallable saveCall = () -> 지하철_노선_등록(신분당, Color.from("red"));

        // then
        assertThatExceptionOfType(DataIntegrityViolationException.class)
            .isThrownBy(saveCall);
    }

    @ParameterizedTest(name = "[{index}] \"{0}\"은 이미 존재하는 이름이라는 사실이 {1}")
    @DisplayName("이미 존재하는 이름인지 확인")
    @CsvSource({"신분당선,true", "1호선,false"})
    void existsByName(String name, boolean expected) {
        // given
        지하철_노선_이름이_등록되어_있음(Name.from("신분당선"), Color.from("red"));

        // when
        boolean existsByName = 존재하는_이름(name);

        // then
        assertThat(existsByName)
            .isEqualTo(expected);
    }

    private void 지하철_노선_저장됨(Line actualLine, Name expectedName, Color expectedColor) {
        assertAll(
            () -> assertThat(actualLine.id()).isNotNull(),
            () -> assertThat(actualLine.name()).isEqualTo(expectedName),
            () -> assertThat(actualLine.color()).isEqualTo(expectedColor)
        );
    }

    private void 지하철_노선_이름이_등록되어_있음(Name name, Color color) {
        지하철_노선_등록(name, color);
    }

    private Line 지하철_노선_등록(Name name, Color color) {
        return repository.save(Line.of(name, color, Sections.from(강남_광교_구간())));
    }

    private boolean 존재하는_이름(String name) {
        return repository.existsByName(Name.from(name));
    }

    private Section 강남_광교_구간() {
        return Section.of(강남역, 역삼역, Distance.from(10));
    }
}
