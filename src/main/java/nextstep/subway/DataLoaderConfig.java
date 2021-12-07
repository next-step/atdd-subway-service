package nextstep.subway;

import com.google.common.collect.Lists;
import nextstep.subway.line.domain.*;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class DataLoaderConfig implements CommandLineRunner {
    private LineRepository lineRepository;
    private MemberRepository memberRepository;

    public DataLoaderConfig(LineRepository lineRepository, MemberRepository memberRepository) {
        this.lineRepository = lineRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(String... args) {
        Station 강남역 = Station.from("강남역");
        Station 교대역 = Station.from("교대역");
        Station 양재역 = Station.from("양재역");

        Section 첫번째구간 = Section.of(강남역, 양재역, Distance.of(10));
        Section 두번째구간 = Section.of(교대역, 강남역, Distance.of(10));
        Section 세번째구간 = Section.of(교대역, 양재역, Distance.of(10));

        Line 신분당선 = Line.of("신분당선", "red lighten-1", Sections.from(첫번째구간));
        Line 이호선 = Line.of("2호선", "green lighten-1", Sections.from(두번째구간));
        Line 삼호선 = Line.of("3호선", "orange darken-1", Sections.from(세번째구간));

        lineRepository.saveAll(Lists.newArrayList(신분당선, 이호선, 삼호선));

        memberRepository.save(new Member("probitanima11@gmail.com", "11", 10));
    }
}