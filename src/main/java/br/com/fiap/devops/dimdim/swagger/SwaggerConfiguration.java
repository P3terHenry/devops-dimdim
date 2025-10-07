package br.com.fiap.devops.dimdim.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    OpenAPI configurarSwagger() {

        return new OpenAPI().info(new Info()
                .title("API FinanceApp - Sistema de Controle Financeiro DimDim")
                .description("Esta API permite o gerenciamento de usuários, categorias e lançamentos financeiros. "
                        + "O objetivo é possibilitar o controle de despesas e receitas pessoais, com persistência de dados "
                        + "em banco SQL Server na nuvem (Azure).")
                .summary("Endpoints para cadastro, listagem, atualização e exclusão de usuários, categorias e lançamentos financeiros.")
                .version("v1.0.0"));
    }
}
