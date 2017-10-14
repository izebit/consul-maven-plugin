import com.pszymczyk.consul.ConsulStarterBuilder

def static startConsul(int port) {
    return ConsulStarterBuilder
                    .consulStarter()
                    .withHttpPort(port)
                    .build()
                    .start()
}

int port = 10000
context.put('consul', startConsul(port))
println "consul has been started on port: $port"