package nextstep.subway.config;

import nextstep.subway.line.application.FarePolicyHandler;
import nextstep.subway.line.domain.fare.policy.LineAdditionalFarePolicy;
import nextstep.subway.line.infrastructure.fare.policy.FarePolicyHandlerImpl;
import nextstep.subway.line.domain.fare.policy.BaseFarePolicy;
import nextstep.subway.line.infrastructure.fare.policy.RateDiscountPolicyCollection;
import nextstep.subway.line.infrastructure.fare.policy.DistancePolicyCollection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SubwayFarePolicyConfig {

    @Bean
    public FarePolicyHandler subwayFarePolicyHandler() {
        FarePolicyHandler subwayFarePolicyHandler = new FarePolicyHandlerImpl();
        subwayFarePolicyHandler.link(additionalFarePolicy());
        subwayFarePolicyHandler.link(distanceFarePolicy());
        subwayFarePolicyHandler.link(rateDiscountPolicy());

        return subwayFarePolicyHandler;
    }

    @Bean
    public BaseFarePolicy additionalFarePolicy() {
        return new LineAdditionalFarePolicy();
    }

    @Bean
    public BaseFarePolicy distanceFarePolicy() {
        return new DistancePolicyCollection();
    }

    @Bean
    public BaseFarePolicy rateDiscountPolicy() {
        return new RateDiscountPolicyCollection();
    }
}
