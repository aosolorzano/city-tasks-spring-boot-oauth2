package com.hiperium.city.tasks.api.common;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

public class StorageContainers extends BaseContainers {

    @DynamicPropertySource
    public static void dynamicPropertySource(DynamicPropertyRegistry registry) {
        baseContainers.join();
        initStorageContainers(registry);
    }
}
