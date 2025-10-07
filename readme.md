<a id="readme-top"></a>

# 💰 CheckPoint – Dimdim – DevOps – Web App Service – Finance App

## 🧑‍🤝‍🧑 Informações dos Contribuintes

| Nome | Matrícula | Turma |
| :------------: | :------------: | :------------: |
| Pedro Henrique Vasco Antonieti | 556253 | 2TDSPH |

<p align="right"><a href="#readme-top">Voltar ao topo</a></p>

---

## 🚩 Estrutura do Projeto

```bash
dimdim/
├── .idea/                          # Configurações do IntelliJ IDEA (não versionar)
├── .mvn/                           # Configurações do Maven Wrapper
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── br/
│   │   │       └── com/
│   │   │           └── fiap/
│   │   │               └── devops/
│   │   │                   └── dimdim/
│   │   │                       ├── controller/       # Controladores REST (Usuário, Categoria, Lançamento)
│   │   │                       ├── dto/              # Data Transfer Objects (DTOs de entrada/saída)
│   │   │                       ├── exception/        # Tratamento de exceções e mensagens de erro
│   │   │                       ├── model/            # Entidades JPA (@Entity) - Usuário, Categoria, Lançamento
│   │   │                       ├── repository/       # Interfaces que herdam JpaRepository
│   │   │                       ├── security/         # Configurações de segurança (Spring Security, se houver)
│   │   │                       ├── swagger/          # Configuração do Swagger (documentação da API)
│   │   │                       └── DimdimApplication.java  # Classe principal da aplicação Spring Boot
│   │   └── resources/
│   │       ├── application.properties   # Configurações principais da aplicação
│   │       └── import.sql               # Script SQL para carga inicial (dados de exemplo)
│   └── test/
│       ├── java/
│       │   └── br/
│       │       └── com/
│       │           └── fiap/
│       │               └── devops/
│       │                   └── dimdim/
│       │                       └── DimdimApplicationTests.java  # Classe de testes automatizados (JUnit)
│       └── resources/                # Recursos adicionais de teste (se necessário)
├── target/                           # Arquivos compilados e empacotados (gerado pelo Maven)
│   ├── classes/                      # Classes Java compiladas
│   └── generated-sources/            # Fontes geradas automaticamente pelo build
├── .env                              # Variáveis de ambiente (não versionado)
├── .env.example                      # Modelo de variáveis de ambiente para outros devs
├── .gitattributes                    # Configurações de formatação e final de linha no Git
├── .gitignore                        # Arquivos e pastas ignorados pelo Git
├── HELP.md                           # Arquivo de ajuda padrão do Spring Boot
├── mvnw                              # Script Maven Wrapper (Linux/macOS)
├── mvnw.cmd                          # Script Maven Wrapper (Windows)
├── pom.xml                           # Configuração do Maven (dependências, plugins e build)
└── README.md                         # Documentação principal do projeto (este arquivo)
```

---

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3**
- **Spring Data JPA**
- **Swagger OpenAPI**
- **H2 Database / SQL Server (Azure)**
- **Azure Web App Service**
- **Maven**
- **GitHub Actions (CI/CD)**

---

## ☁️ Deploy Automático na Azure

O deploy é feito via **GitHub Actions** configurado com as variáveis de ambiente e scripts **Azure CLI**, utilizando o banco de dados **SQL Server PaaS** e monitoramento com **Application Insights**.

---

## 🎥 Youtube

Apresentação do projeto no Youtube: https://youtu.be/yqMnHaFaeyM

<p align="right"><a href="#readme-top">Voltar ao topo</a></p>

---

## 💬 Endpoints Principais

| Método | Endpoint | Descrição |
|:-------:|:----------|:-----------|
| GET | `/api/usuarios/listar` | Lista todos os usuários cadastrados |
| POST | `/api/usuarios/inserir` | Cria um novo usuário |
| PUT | `/api/usuarios/atualizar/{id}` | Atualiza os dados de um usuário |
| DELETE | `/api/usuarios/deletar/{id}` | Remove um usuário |
| GET | `/api/categorias/listar` | Lista todas as categorias |
| POST | `/api/categorias/inserir` | Cria uma nova categoria vinculada a um usuário |
| GET | `/api/lancamentos/listar` | Lista todos os lançamentos |
| POST | `/api/lancamentos/inserir` | Cria um novo lançamento vinculado a uma categoria |

---

## ⚙️ Execução Local

```bash
# Compilar e rodar o projeto localmente
mvn spring-boot:run
```

A aplicação iniciará em: **http://localhost:8080**  
Swagger UI disponível em: **http://localhost:8080/swagger-ui.html**

---

## ☁️ Deploy na Azure

Este repositório contém os scripts e instruções necessários para realizar o deploy do projeto na Azure utilizando **Azure CLI** e **GitHub Actions**.

---

## ⚙️ Variáveis de Ambiente

Antes de rodar os comandos, configure as variáveis:

```sh
RESOURCE_GROUP_NAME="rg-devops-dimdim"
LOCATION="brazilsouth"
APP_SERVICE_PLAN="plan-dimdim"
WEBAPP_NAME="app-dimdim"
APP_INSIGHTS_NAME="ai-dimdim"
SQL_SERVER="sql-server-dimdim"
SQL_DB="db-dimdim"
SQL_USER="user-dimdim"
SQL_PASS="Fiap@2tdsvms"
GITHUB_REPO_NAME="P3terHenry/devops-dimdim"
BRANCH="main"
```

---

## 🚀 Etapas de Deploy

### 1️⃣ Providers e Extensões

```sh
az provider register --namespace Microsoft.Web
az provider register --namespace Microsoft.Insights
az provider register --namespace Microsoft.OperationalInsights
az extension add --name application-insights
```

### 2️⃣ Criar Grupo de Recursos

```sh
az group create \
    --name $RESOURCE_GROUP_NAME \
    --location "$LOCATION"
```

### 3️⃣ Application Insights

```sh
az monitor app-insights component create \
    --app $APP_INSIGHTS_NAME \
    --location $LOCATION \
    --resource-group $RESOURCE_GROUP_NAME \
    --application-type web
```

### 4️⃣ Plano App Service

```sh
az appservice plan create \
    --name $APP_SERVICE_PLAN \
    --resource-group $RESOURCE_GROUP_NAME \
    --location "$LOCATION" \
    --sku F1 \
    --is-linux
```

### 5️⃣ Criar Web App (Java 17)

```sh
az webapp create \
    --name $WEBAPP_NAME \
    --resource-group $RESOURCE_GROUP_NAME \
    --plan $APP_SERVICE_PLAN \
    --runtime "JAVA:17-java17"
```

### 6️⃣ Banco de Dados SQL Server

Criação do SQL Server:
```sh
az sql server create \
    --name $SQL_SERVER \
    --resource-group $RESOURCE_GROUP_NAME \
    --location $LOCATION \
    --admin-user $SQL_USER \
    --admin-password $SQL_PASS \
    --enable-public-network true
```

Criação do Database dentro do SQL Server:
```sh
az sql db create \
    --resource-group $RESOURCE_GROUP_NAME \
    --server $SQL_SERVER \
    --name $SQL_DB \
    --service-objective Basic \
    --backup-storage-redundancy Local \
    --zone-redundant false
```

Liberar acesso público para todos os IPs no SQL Server:
```sh
az sql server firewall-rule create \
    --resource-group $RESOURCE_GROUP_NAME \
    --server $SQL_SERVER \
    --name liberaGeral \
    --start-ip-address 0.0.0.0 \
    --end-ip-address 255.255.255.255
```

### 7️⃣ Habilitar autenticação básica (SCM)

```sh
az resource update \
    --resource-group $RESOURCE_GROUP_NAME \
    --namespace Microsoft.Web \
    --resource-type basicPublishingCredentialsPolicies \
    --name scm \
    --parent sites/$WEBAPP_NAME \
    --set properties.allow=true
```

### 8️⃣ Configurar Variáveis de Ambiente

Recuperar a string do Application Insights:

```sh
CONNECTION_STRING=$(az monitor app-insights component show \
--app $APP_INSIGHTS_NAME \
--resource-group $RESOURCE_GROUP_NAME \
--query connectionString \
--output tsv)
```

Configurar App Settings no WebApp:

```sh
az webapp config appsettings set \
      --name "$WEBAPP_NAME" \
      --resource-group "$RESOURCE_GROUP_NAME" \
      --settings \
    APPLICATIONINSIGHTS_CONNECTION_STRING="$CONNECTION_STRING" \
    ApplicationInsightsAgent_EXTENSION_VERSION="~3" \
    XDT_MicrosoftApplicationInsights_Mode="Recommended" \
    XDT_MicrosoftApplicationInsights_PreemptSdk="1" \
    SPRING_DATASOURCE_URL="jdbc:sqlserver://sql-server-dimdim.database.windows.net:1433;databaseName=db-dimdim;encrypt=true;trustServerCertificate=false;loginTimeout=30" \
    SPRING_DATASOURCE_USERNAME="user-dimdim" \
    SPRING_DATASOURCE_PASSWORD="Fiap@2tdsvms" \
    SPRING_JPA_DATABASE_PLATFORM="org.hibernate.dialect.SQLServerDialect" \
    SPRING_JPA_HIBERNATE_DDL_AUTO="update" \
    SPRING_DATASOURCE_DRIVER="com.microsoft.sqlserver.jdbc.SQLServerDriver"
```

Reiniciar o WebApp:

```sh
az webapp restart \
  --name $WEBAPP_NAME \
  --resource-group $RESOURCE_GROUP_NAME
```

Conectar o Web App com o App Insights:

```sh
az monitor app-insights component connect-webapp \
    --app $APP_INSIGHTS_NAME \
    --web-app $WEBAPP_NAME \
    --resource-group $RESOURCE_GROUP_NAME
```

### 9️⃣ Configurar GitHub Actions

```sh
az webapp deployment github-actions add \
    --name $WEBAPP_NAME \
    --resource-group $RESOURCE_GROUP_NAME \
    --repo $GITHUB_REPO_NAME \
    --branch $BRANCH \
    --login-with-github
```

---

## 🔑 Configurar Secrets no GitHub

1. Vá em Settings → Secrets and variables → Actions
2. Clique em New repository secret
3. Adicione os secrets abaixo (um de cada vez):

```ini
SPRING_DATASOURCE_URL=jdbc:sqlserver://sql-server-find-mottu.database.windows.net:1433;databaseName=db-find-mottu;encrypt=true;trustServerCertificate=false;loginTimeout=30;
SPRING_DATASOURCE_USERNAME=user-find-mottu
SPRING_DATASOURCE_PASSWORD=Fiap@2tdsvms
SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.SQLServerDialect
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_DATASOURCE_DRIVER=com.microsoft.sqlserver.jdbc.SQLServerDriver
```

No arquivo **.github/workflows/deploy.yml**:

```yml
env:
  SPRING_DATASOURCE_URL: ${{ secrets.SPRING_DATASOURCE_URL }}
  SPRING_DATASOURCE_USERNAME: ${{ secrets.SPRING_DATASOURCE_USERNAME }}
  SPRING_DATASOURCE_PASSWORD: ${{ secrets.SPRING_DATASOURCE_PASSWORD }}
  SPRING_JPA_DATABASE_PLATFORM: ${{ secrets.SPRING_JPA_DATABASE_PLATFORM }}
  SPRING_JPA_HIBERNATE_DDL_AUTO: ${{ secrets.SPRING_JPA_HIBERNATE_DDL_AUTO }}
  SPRING_DATASOURCE_DRIVER: ${{ secrets.SPRING_DATASOURCE_DRIVER }}
```

---

✅ Pronto! Agora o deploy será feito automaticamente via GitHub Actions sempre que houver push na branch main.

<p align="right"><a href="#readme-top">Voltar ao topo</a></p>

## 📂 Autor

**Pedro Henrique Vasco Antonieti**  
📧 rm556253@fiap.com.br (E-mail Institucional)  
🔗 [LinkedIn](https://www.linkedin.com/in/vascoantonieti)

