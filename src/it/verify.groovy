import com.orbitz.consul.Consul

import static groovy.util.GroovyTestCase.assertEquals
import static junit.framework.TestCase.assertNull


def static assertEqualsValue(def expectedKey, def expectedValue, def consulClient) {
    def entry = consulClient.getValue(expectedKey)
    def value = entry.get()
    assertEquals(expectedValue, value.getValueAsString().orNull())
}

def static check(def consulServer) {
    def consulClient = Consul
            .builder()
            .withUrl('http://' + consulServer.getAddress() + ':' + consulServer.getHttpPort())
            .build()
            .keyValueClient()

    def prefix = "domain/config/"

    assertEqualsValue(prefix + 'first-key', 'first-value', consulClient)
    assertEqualsValue(prefix + 'second-key', null, consulClient)
    assertEqualsValue(prefix + 'third-key', '#hello world', consulClient)

    assertNull(consulClient.getValueAsString("#it is comment").orNull())
}


def consulServer = context.get('consul')

try {
    check(consulServer)
} catch (Exception e) {
    throw e
} finally {
    consulServer.close()
    println "consul has been destroyed"
}

