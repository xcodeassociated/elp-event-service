package com.xcodeassociated.service

import groovy.util.logging.Slf4j
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@Slf4j
@ActiveProfiles("test")
class ServiceIntegrationSpec extends Specification {

    def setup() {
        log.debug("Spock Test Setup")
    }

    def "sample test equals"() {
        given:
            def a = 1
            def b = 1
        when:
            def sum = a + b
        then:
            2 == sum
    }

    def "sample test expect"() {
        def a = 1
        def b = 1

        expect:
            2 == a + b
    }
}
