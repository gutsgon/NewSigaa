package br.edu.ifs.apinewsigaa.config.security;

import org.keycloak.adapters.authorization.integration.jakarta.ServletPolicyEnforcerFilter;
import org.keycloak.adapters.authorization.spi.ConfigurationResolver;
import org.keycloak.adapters.authorization.spi.HttpRequest;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig;
import org.keycloak.util.JsonSerialization;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;

/**
 * Classe de configuração para a segurança da aplicação.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // colocar uma configuração com keycloack para cada método
@KeycloakConfiguration
public class SecurityConfig{

    /**
     * Configuração do filtro de segurança.
     *
     * @param http Objeto HttpSecurity para configuração da segurança HTTP.
     * @return O filtro de segurança configurado.
     * @throws Exception Se ocorrer algum erro durante a configuração.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(t -> t.disable());
        http.addFilterAfter(createPolicyEnforcerFilter(), BearerTokenAuthenticationFilter.class);
        http.sessionManagement(t -> t.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    /**
     * Cria um filtro para aplicar as políticas de autorização.
     *
     * @return O filtro de aplicação de políticas de autorização.
     */
    private ServletPolicyEnforcerFilter createPolicyEnforcerFilter() {
        return new ServletPolicyEnforcerFilter(new ConfigurationResolver() {
            @Override
            public PolicyEnforcerConfig resolve(HttpRequest httpRequest) {
                try {
                    return JsonSerialization.readValue(
                            getClass().getResourceAsStream("/policy-enforcer.json"),
                            PolicyEnforcerConfig.class
                    );
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}