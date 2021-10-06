package cloud4webdevs.mythaistar.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "cloud4webdevs.mythaistar.springboot",
        "cloud4webdevs.mythaistar.booking.adapter.in.springweb"})
@EnableJpaRepositories(basePackages = "cloud4webdevs.mythaistar.booking.adapter.out.jpa")
@EntityScan(basePackages = "cloud4webdevs.mythaistar.booking.adapter.out.jpa")
public class SpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);
    }

}
