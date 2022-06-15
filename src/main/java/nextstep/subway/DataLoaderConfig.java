package nextstep.subway;

import com.google.common.collect.Lists;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.member.domain.Age;
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
    public void run(String... args) throws Exception {
        Station 강남역 = Station.builder("강남역")
                .build();
        Station 교대역 = Station.builder("교대역")
                .build();
        Station 양재역 = Station.builder("양재역")
                .build();
        Station 남부터미널역 = Station.builder("남부터미널역")
                .build();

        Line 신분당선 = Line.builder("신분당선", "red lighten-1", 강남역, 양재역, Distance.valueOf(10))
                .build();
        Line 이호선 = Line.builder("2호선", "green lighten-1", 교대역, 강남역, Distance.valueOf(10))
                .build();
        Line 삼호선 = Line.builder("3호선", "orange darken-1", 교대역, 양재역, Distance.valueOf(10))
                .build();

        lineRepository.saveAll(Lists.newArrayList(신분당선, 이호선, 삼호선));

        memberRepository.save(new Member("probitanima11@gmail.com", "11", Age.valueOf(10)));
    }
}