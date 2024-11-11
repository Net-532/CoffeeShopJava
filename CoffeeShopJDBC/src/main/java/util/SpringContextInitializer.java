package util;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringContextInitializer {
    private static AnnotationConfigApplicationContext context;

    static {
        context = new AnnotationConfigApplicationContext("service");
    }

    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }
}
