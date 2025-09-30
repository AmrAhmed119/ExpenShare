package com.expenshare;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.info.*;

@OpenAPIDefinition(
    info = @Info(
        title = "Expen-Share",
        version = "1.0.0",
        description = "API documentation for the Expen-Share application, a platform to manage expenses, track sharing, and integrate with Kafka messaging.",
        contact = @Contact(
            name = "Amr",
            url = "https://github.com/AmrAhmed119/expen-share"
        )
    )
)
public class ExpenShareApplication {

    public static void main(String[] args) {
        Micronaut.run(ExpenShareApplication.class, args);
    }
}