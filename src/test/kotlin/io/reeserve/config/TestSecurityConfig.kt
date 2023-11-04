package io.reeserve.config

import org.mockito.Mockito
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.web.SecurityFilterChain

@TestConfiguration
class TestSecurityConfig {

    @Bean
    fun jwtSecurityFilterChain(): SecurityFilterChain {
        return Mockito.mock(SecurityFilterChain::class.java)
    }
}