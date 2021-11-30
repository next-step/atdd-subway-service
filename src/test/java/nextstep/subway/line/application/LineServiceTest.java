package nextstep.subway.line.application;

import static nextstep.subway.line.step.LineStep.line;
import static nextstep.subway.line.step.SectionStep.section;
import static nextstep.subway.station.step.StationStep.station;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;
import nextstep.subway.common.domain.Name;
import nextstep.subway.common.exception.DuplicateDataException;
import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.line.domain.Color;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineCreateRequest;
import nextstep.subway.line.dto.LineUpdateRequest;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("노선 서비스")
@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @Mock
    private LineRepository repository;

    @Mock
    private StationService stationService;

    @InjectMocks
    private LineService service;

    @Test
    @DisplayName("노선 저장")
    void saveLine() {
        // given
        String 신분당 = "신분당선";
        String 빨강 = "red";

        Station 강남역 = station("강남");
        Station 광교역 = station("광교");
        검색된_지하철_역_제공(1L, 강남역);
        검색된_지하철_역_제공(2L, 광교역);

        지하철_노선_이름이_중복되지_않음(신분당);
        저장된_노선_반환(신분당선());

        // when
        service.saveLine(new LineCreateRequest(신분당, 빨강,
            new SectionRequest(1L, 2L, 10)));

        // then
        지하철_노선_저장_요청됨(신분당, 빨강, 강남역, 광교역);
    }

    @Test
    @DisplayName("존재하지 않는 이름으로 저장해야 함")
    void saveLine_alreadyExistsName_thrownDuplicateDataException() {
        // given
        String 신분당 = "신분당선";
        지하철_노선_이름이_이미_존재(신분당);

        // when
        ThrowingCallable saveCallable = () -> service.saveLine(new LineCreateRequest(신분당, "red",
            new SectionRequest(1L, 2L, 10)));

        // then
        중복_예외_발생(saveCallable);
    }

    @Test
    @DisplayName("찾으려는 노선은 존재해야 함")
    void findLineResponseById_notExits_thrownNotFoundException() {
        // given
        지하철_노선이_존재하지_않음();

        // when
        ThrowingCallable findLineCallable = () -> service.findLineResponseById(Long.MAX_VALUE);

        // then
        찾을_수_없는_예외_발생(findLineCallable);
    }

    @Test
    @DisplayName("노선 이름과 색상 수정")
    void updateLine() {
        // given
        String 구분당선 = "구분당선";
        String 파랑 = "blue";
        지하철_노선_이름이_중복되지_않음(구분당선);

        Line 신분당선 = 신분당선();
        검색된_지하철_노선_제공(신분당선);

        // when
        service.updateLine(Long.MAX_VALUE, new LineUpdateRequest(구분당선, 파랑));

        // then
        assertAll(
            () -> assertThat(신분당선.name()).isEqualTo(Name.from(구분당선)),
            () -> assertThat(신분당선.color()).isEqualTo(Color.from(파랑))
        );
    }

    @Test
    @DisplayName("수정하려는 이름이 존재하지 않아야 함")
    void updateLine_duplicationName_thrownDataIntegrityViolationException() {
        // given
        String 신분당선 = "신분당선";
        지하철_노선_이름이_이미_존재(신분당선);

        // when
        ThrowingCallable updateCallable = () -> service
            .updateLine(Long.MAX_VALUE, new LineUpdateRequest(신분당선, "any"));

        // then
        중복_예외_발생(updateCallable);
    }

    @Test
    @DisplayName("삭제")
    void deleteLineById() {
        // given
        Line mockLine = mock(Line.class);
        검색된_지하철_노선_제공(mockLine);

        // when
        service.deleteLineById(Long.MAX_VALUE);

        // then
        verify(repository, times(1))
            .delete(mockLine);
    }

    @Test
    @DisplayName("구간 추가")
    void addLineStation() {
        // given
        Line mockLine = mock(Line.class);
        검색된_지하철_노선_제공(mockLine);
        검색된_지하철_역_제공(1L, station("강남"));
        검색된_지하철_역_제공(2L, station("양재"));

        // when
        service.addLineStation(
            1L, new SectionRequest(1L, 2L, 10));

        // then
        verify(mockLine, only())
            .addSection(section("강남", "양재", 10));
    }

    @Test
    @DisplayName("역 삭제")
    void removeLineStation() {
        // given
        Line mockLine = mock(Line.class);
        검색된_지하철_노선_제공(mockLine);

        Station 강남역 = Station.from(Name.from("강남"));
        검색된_지하철_역_제공(1L, 강남역);

        // when
        service.removeLineStation(1L, 1L);

        // then
        verify(mockLine, only())
            .removeStation(강남역);
    }

    private void 찾을_수_없는_예외_발생(ThrowingCallable callable) {
        assertThatExceptionOfType(NotFoundException.class)
            .isThrownBy(callable)
            .withMessageEndingWith(" 존재하지 않습니다.");
    }

    private void 중복_예외_발생(ThrowingCallable callable) {
        assertThatExceptionOfType(DuplicateDataException.class)
            .isThrownBy(callable)
            .withMessageEndingWith("이미 존재합니다.");
    }

    private void 지하철_노선_저장_요청됨(String expectedName, String expectedColor,
        Station firstExpectedStation, Station secondExpectedStation) {
        ArgumentCaptor<Line> lineArgumentCaptor = ArgumentCaptor.forClass(Line.class);
        verify(repository, times(1)).save(lineArgumentCaptor.capture());
        Line line = lineArgumentCaptor.getValue();

        assertAll(
            () -> assertThat(line.name()).isEqualTo(Name.from(expectedName)),
            () -> assertThat(line.color()).isEqualTo(Color.from(expectedColor)),
            () -> assertThat(line.sortedStations())
                .isEqualTo(Stations.from(
                    Arrays.asList(firstExpectedStation, secondExpectedStation)
                ))
        );
    }

    private void 지하철_노선이_존재하지_않음() {
        when(repository.findById(anyLong()))
            .thenReturn(Optional.empty());
    }

    private void 검색된_지하철_노선_제공(Line line) {
        when(repository.findById(anyLong()))
            .thenReturn(Optional.of(line));
    }

    private void 지하철_노선_이름이_이미_존재(String name) {
        when(repository.existsByName(Name.from(name)))
            .thenReturn(true);
    }

    private void 지하철_노선_이름이_중복되지_않음(String name) {
        when(repository.existsByName(Name.from(name)))
            .thenReturn(false);
    }

    private void 검색된_지하철_역_제공(Long id, Station station) {
        when(stationService.findById(id))
            .thenReturn(station);
    }

    private void 저장된_노선_반환(Line line) {
        when(repository.save(any(Line.class)))
            .thenReturn(line);
    }

    private Line 신분당선() {
        return line("신분당선", "red",
            section("강남", "광교", 10)
        );
    }
}
