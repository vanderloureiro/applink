package com.vanderloureiro.applink_api.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class WebConfig : WebMvcConfigurer {
    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addViewController("/{spring:[^\\.]*}")
            .setViewName("forward:/index.html")
        registry.addViewController("/{spring:^(?!api).*}/**{spring:[^\\.]*}")
            .setViewName("forward:/index.html")
    }
}