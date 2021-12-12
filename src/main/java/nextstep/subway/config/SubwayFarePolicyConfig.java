package nextstep.subway.config;

import nextstep.subway.line.application.FarePolicyHandler;
import nextstep.subway.line.domain.fare.policy.LineAdditionalFarePolicy;
import nextstep.subway.line.infrastructure.fare.policy.FarePolicyHandlerImpl;
import nextstep.subway.line.domain.fare.policy.BaseFarePolicy;
import nextstep.subway.line.infrastructure.fare.policy.AgeDiscountPolicy;
import nextstep.subway.line.infrastructure.fare.policy.DistancePolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SubwayFarePolicyConfig {

    @Bean
    public FarePolicyHandler subwayFarePolicyHandler() {
        FarePolicyHandler subwayFarePolicyHandler = new FarePolicyHandlerImpl();
        subwayFarePolicyHandler.link(additionalFarePolicy());
        subwayFarePolicyHandler.link(distanceFarePolicy());
        subwayFarePolicyHandler.link(ageDiscountPolicy());

        return subwayFarePolicyHandler;
    }

    @Bean
    public BaseFarePolicy additionalFarePolicy() {
        return new LineAdditionalFarePolicy();
    }

    @Bean
    public BaseFarePolicy distanceFarePolicy() {
        return new DistancePolicy();
    }

    @Bean
    public BaseFarePolicy ageDiscountPolicy() {
        return new AgeDiscountPolicy();
    }
}
