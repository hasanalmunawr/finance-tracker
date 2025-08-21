package hasanalmunawr.dev.backend_spring.web.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class WebConfig implements WebMvcConfigurer {

    private final JwtFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
//    private final AuthenticationManager authenticationManager;

    private final static String[] WHITE_LIST_URL = {
            "/authentication/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults()) // Izinkan CORS default
                .csrf(AbstractHttpConfigurer::disable) // Nonaktifkan CSRF (karena kita pakai JWT, bukan session)
                .authorizeHttpRequests(req ->
                        req.requestMatchers( WHITE_LIST_URL).permitAll() // Izinkan endpoint di whitelist
                                .anyRequest().authenticated() // Sisanya harus login
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS)) // Tidak pakai session (JWT = stateless)
                .authenticationProvider(authenticationProvider) // Pakai custom auth provider
//                .authenticationManager(authenticationManager)
//                .httpBasic(Customizer.withDefaults()) // Izinkan Basic Auth (opsional)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // Tambah custom JWT filter sebelum login filter
                .build();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // izinkan semua endpoint
                .allowedOrigins("http://localhost:5173", "https://fintrack.hasanalmunawar.my.id") // ganti dengan port Vue-mu
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true); // jika kamu menggunakan cookie / session
    }
}
