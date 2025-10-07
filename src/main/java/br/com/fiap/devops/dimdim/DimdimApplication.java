package br.com.fiap.devops.dimdim;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@EntityScan
@SpringBootApplication
public class DimdimApplication {

    public static void main(String[] args) {

        // Carrega .env apenas se existir, sem quebrar em produção
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing() // <--- evita exception se .env não existir
                .load();

        // Seta variáveis de ambiente apenas se ainda não estiverem definidas
        setIfAbsent("SPRING_DATASOURCE_URL", dotenv.get("SPRING_DATASOURCE_URL"));
        setIfAbsent("SPRING_DATASOURCE_USERNAME", dotenv.get("SPRING_DATASOURCE_USERNAME"));
        setIfAbsent("SPRING_DATASOURCE_PASSWORD", dotenv.get("SPRING_DATASOURCE_PASSWORD"));
        setIfAbsent("SPRING_DATASOURCE_DRIVER", dotenv.get("SPRING_DATASOURCE_DRIVER"));

//        System.setProperty("SPRING_DATASOURCE_URL", dotenv.get("SPRING_DATASOURCE_URL"));
//        System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv.get("SPRING_DATASOURCE_USERNAME"));
//        System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv.get("SPRING_DATASOURCE_PASSWORD"));
//        System.setProperty("SPRING_DATASOURCE_DRIVER", dotenv.get("SPRING_DATASOURCE_DRIVER"));


        SpringApplication.run(DimdimApplication.class, args);
    }

    private static void setIfAbsent(String key, String value) {
        if (System.getenv(key) == null && value != null) {
            System.setProperty(key, value);
        }
    }

}
