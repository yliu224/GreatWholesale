package com.community.configuration;

import com.community.workflow.checkout.RecordHeatRangeActivity;
import org.broadleafcommerce.common.extensibility.context.merge.Merge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Arrays;
import java.util.List;

@Configuration
public class CheckoutWorkflowConfiguration {

    @Merge("blCheckoutWorkflowActivities")
    @Bean
    public List<?> customCheckoutActivities(RecordHeatRangeActivity recordHeatRangeActivity) {
        return Arrays.asList(recordHeatRangeActivity);
    }
}
