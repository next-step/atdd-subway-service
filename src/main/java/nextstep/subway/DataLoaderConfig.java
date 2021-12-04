package nextstep.subway;

import org.springframework.boot.*;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;

import com.google.common.collect.*;

import nextstep.subway.line.domain.*;
import nextstep.subway.member.domain.*;
import nextstep.subway.station.domain.*;

@Component
@Profile("!test")
public class DataLoaderConfig implements CommandLineRunner {
    private final LineRepository lineRepository;
    private final MemberRepository memberRepository;

    public DataLoaderConfig(LineRepository lineRepository, MemberRepository memberRepository) {
        this.lineRepository = lineRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Station 강남역 = new Station("강남역");
        Station 교대역 = new Station("교대역");
        Station 양재역 = new Station("양재역");
        Station 남부터미널역 = new Station("남부터미널역");

        Line 신분당선 = new Line("신분당선", "red lighten-1", 강남역, 양재역, Distance.from(10));
        Line 이호선 = new Line("2호선", "green lighten-1", 교대역, 강남역, Distance.from(10));
        Line 삼호선 = new Line("3호선", "orange darken-1", 교대역, 양재역, Distance.from(10));

        lineRepository.saveAll(Lists.newArrayList(신분당선, 이호선, 삼호선));

        memberRepository.save(new Member("probitanima11@gmail.com", "11", 10));
    }
}
