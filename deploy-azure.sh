# ============================================
# 🔧 VARIÁVEIS PRINCIPAIS
# ============================================

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

# ============================================
# 🔌 REGISTROS E EXTENSÕES NECESSÁRIAS
# ============================================

az provider register --namespace Microsoft.Web
az provider register --namespace Microsoft.Insights
az provider register --namespace Microsoft.OperationalInsights
az extension add --name application-insights

# ============================================
# 📦 CRIAR GRUPO DE RECURSOS
# ============================================

az group create \
  --name $RESOURCE_GROUP_NAME \
  --location "$LOCATION"

# ============================================
# 📊 CRIAR APPLICATION INSIGHTS
# ============================================

az monitor app-insights component create \
  --app $APP_INSIGHTS_NAME \
  --location $LOCATION \
  --resource-group $RESOURCE_GROUP_NAME \
  --application-type web

# ============================================
# ☁️ CRIAR PLANO DE APP SERVICE (FREE)
# ============================================

az appservice plan create \
  --name $APP_SERVICE_PLAN \
  --resource-group $RESOURCE_GROUP_NAME \
  --location "$LOCATION" \
  --sku F1 \
  --is-linux

# ============================================
# 🚀 CRIAR WEB APP (JAVA 17)
# ============================================

az webapp create \
  --name $WEBAPP_NAME \
  --resource-group $RESOURCE_GROUP_NAME \
  --plan $APP_SERVICE_PLAN \
  --runtime "JAVA:17-java17"

# ============================================
# 🧾 CRIAR SQL SERVER E BANCO DE DADOS
# ============================================

az sql server create \
  --name $SQL_SERVER \
  --resource-group $RESOURCE_GROUP_NAME \
  --location $LOCATION \
  --admin-user $SQL_USER \
  --admin-password $SQL_PASS \
  --enable-public-network true

az sql db create \
  --resource-group $RESOURCE_GROUP_NAME \
  --server $SQL_SERVER \
  --name $SQL_DB \
  --service-objective Basic \
  --backup-storage-redundancy Local \
  --zone-redundant false

# ============================================
# 🔓 LIBERAR ACESSO PÚBLICO
# ============================================

az sql server firewall-rule create \
  --resource-group $RESOURCE_GROUP_NAME \
  --server $SQL_SERVER \
  --name liberaGeral \
  --start-ip-address 0.0.0.0 \
  --end-ip-address 255.255.255.255

# ============================================
# 🔑 HABILITAR AUTENTICAÇÃO BÁSICA DO SCM
# ============================================

az resource update \
  --resource-group $RESOURCE_GROUP_NAME \
  --namespace Microsoft.Web \
  --resource-type basicPublishingCredentialsPolicies \
  --name scm \
  --parent sites/$WEBAPP_NAME \
  --set properties.allow=true

# ============================================
# 📡 RECUPERAR CONNECTION STRING DO APP INSIGHTS
# ============================================

CONNECTION_STRING=$(az monitor app-insights component show \
  --app $APP_INSIGHTS_NAME \
  --resource-group $RESOURCE_GROUP_NAME \
  --query connectionString \
  --output tsv)

# ============================================
# ⚙️ CONFIGURAR VARIÁVEIS DE AMBIENTE DO APP
# ============================================

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

# ============================================
# 🔄 REINICIAR O WEB APP
# ============================================

az webapp restart \
  --name $WEBAPP_NAME \
  --resource-group $RESOURCE_GROUP_NAME

# ============================================
# 🔗 CONECTAR WEB APP AO APPLICATION INSIGHTS
# ============================================

az monitor app-insights component connect-webapp \
  --app $APP_INSIGHTS_NAME \
  --web-app $WEBAPP_NAME \
  --resource-group $RESOURCE_GROUP_NAME

# ============================================
# 🧩 CONFIGURAR GITHUB ACTIONS PARA DEPLOY AUTOMÁTICO
# ============================================

az webapp deployment github-actions add \
  --name $WEBAPP_NAME \
  --resource-group $RESOURCE_GROUP_NAME \
  --repo $GITHUB_REPO_NAME \
  --branch $BRANCH \
  --login-with-github

# 01) Vá em Settings → Secrets and variables → Actions
# 02) Clique em New repository secret
# 03) Adicione os 3 secrets (um de cada vez):

# SPRING_DATASOURCE_URL=jdbc:sqlserver://sql-server-dimdim.database.windows.net:1433;databaseName=db-dimdim;encrypt=true;trustServerCertificate=false;loginTimeout=30;
# SPRING_DATASOURCE_USERNAME=user-dimdim
# SPRING_DATASOURCE_PASSWORD=Fiap@2tdsvms
# SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.SQLServerDialect
# SPRING_JPA_HIBERNATE_DDL_AUTO=update
# SPRING_DATASOURCE_DRIVER=com.microsoft.sqlserver.jdbc.SQLServerDriver

# Depois no arquivo yml:

# env:
#        SPRING_DATASOURCE_URL: ${{ secrets.SPRING_DATASOURCE_URL }}
#        SPRING_DATASOURCE_USERNAME: ${{ secrets.SPRING_DATASOURCE_USERNAME }}
#        SPRING_DATASOURCE_PASSWORD: ${{ secrets.SPRING_DATASOURCE_PASSWORD }}
#        SPRING_JPA_DATABASE_PLATFORM: ${{ secrets.SPRING_JPA_DATABASE_PLATFORM }}
#        SPRING_JPA_HIBERNATE_DDL_AUTO: ${{ secrets.SPRING_JPA_HIBERNATE_DDL_AUTO }}
#        SPRING_DATASOURCE_DRIVER: ${{ secrets.SPRING_DATASOURCE_DRIVER }}