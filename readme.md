# Sistema de Gerenciamento de Resíduos - Smart City

## 📝 Descrição
Sistema desenvolvido para gerenciar coletas de resíduos em uma cidade inteligente, permitindo o agendamento, acompanhamento e gestão de coletas de diferentes tipos de resíduos.

## 🚀 Tecnologias Utilizadas
- Java 17
- Spring Boot 3.2.0
- MySQL 8.0
- Docker & Docker Compose
- Spring Security + JWT
- Flyway para migrações
- Swagger/OpenAPI para documentação

## 🛠️ Pré-requisitos
- Docker
- Docker Compose
- Java 17 (para desenvolvimento local)
- Maven (opcional, pode usar o wrapper)

## 💻 Como Executar

1. Clone o repositório:
```bash
git clone [url-do-repositorio]
cd waste-management
```

2. Configure as variáveis de ambiente:
```bash
cp .env.example .env
# Edite o arquivo .env com suas configurações
```

3. Execute a aplicação:
```bash
docker-compose up --build
```

A aplicação estará disponível em `http://localhost:8080`

## 📚 Documentação da API
A documentação completa da API está disponível através do Swagger UI:
- URL: `http://localhost:8080/swagger-ui.html`

### Endpoints Principais

#### Autenticação
- POST `/api/auth/register` - Registro de novo usuário
- POST `/api/auth/login` - Login de usuário

#### Coletas
- GET `/api/v1/waste-collections` - Lista todas as coletas
- POST `/api/v1/waste-collections` - Cria nova coleta
- GET `/api/v1/waste-collections/{id}` - Busca coleta por ID
- PUT `/api/v1/waste-collections/{id}` - Atualiza coleta
- PATCH `/api/v1/waste-collections/{id}/status` - Atualiza status da coleta
- DELETE `/api/v1/waste-collections/{id}` - Remove coleta

## 🗄️ Estrutura do Banco de Dados

### Tabelas Principais
1. `users`
   - Armazena informações dos usuários
   - Campos: id, email, name, password, role

2. `waste_collections`
   - Registros de coletas
   - Campos: id, address, waste_type, scheduled_time, status, notes, etc.

### Migrações
O projeto utiliza Flyway para gerenciamento de migrações do banco de dados:
- `V1__Create_Initial_Tables.sql` - Estrutura inicial
- `V2__Add_User_Indexes.sql` - Índices para otimização
- `V3__Add_Collection_Details.sql` - Campos adicionais para coletas

## 🔐 Segurança
- Autenticação via JWT
- Endpoints protegidos com Spring Security
- Senhas criptografadas com BCrypt
- Validação de tokens em todas as requisições protegidas

## 🧪 Testando a API

### Usando Insomnia
1. Importe a coleção do OpenAPI:
   - Acesse `http://localhost:8080/v3/api-docs`
   - No Insomnia: Create > Import From File
   - Selecione o arquivo baixado

2. Configure o ambiente:
```json
{
  "baseUrl": "http://localhost:8080",
  "bearerToken": "seu-token-jwt"
}
```

### Exemplo de Uso
1. Registre um usuário
2. Faça login e copie o token
3. Configure o token no ambiente
4. Teste os endpoints protegidos

## 📦 Estrutura do Projeto
```
waste-management/
├── src/
│   └── main/
│       ├── java/com/smartcity/wastemanagement/
│       │   ├── config/
│       │   ├── controller/
│       │   ├── dto/
│       │   ├── model/
│       │   ├── repository/
│       │   ├── security/
│       │   └── service/
│       └── resources/
│           ├── db/migration/
│           └── application.yml
├── docker-compose.yml
├── Dockerfile
└── pom.xml
```