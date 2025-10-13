package com.expenshare.service.expense;

import com.expenshare.model.enums.SplitType;
import io.micronaut.context.BeanContext;
import jakarta.inject.Singleton;

@Singleton
public class SplitValidatorFactory {
    private final BeanContext beanContext;

    public SplitValidatorFactory(BeanContext beanContext) {
        this.beanContext = beanContext;
    }

    public SplitValidator getValidator(SplitType splitType) {
        return switch (splitType) {
            case EQUAL -> beanContext.getBean(EqualSplitValidator.class);
            case EXACT -> beanContext.getBean(ExactSplitValidator.class);
            case PERCENT -> beanContext.getBean(PercentSplitValidator.class);
        };
    }
}
