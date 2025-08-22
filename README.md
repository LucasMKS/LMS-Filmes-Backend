# LMS Films - Backend com MongoDB e Docker

Este projeto foi refatorado para usar MongoDB em containers Docker.

## 🚀 Estrutura do Projeto

```
lmsfilmes/
├── src/
├── docker-compose.yml      # Configuração Docker
├── Dockerfile             # Build da aplicação
├── init-mongo.js          # Script inicialização MongoDB
├── docker-manager.bat     # Utilitário Windows
├── .env                   # Variáveis ambiente
└── README.md
```

## 🛠️ Pré-requisitos

- Docker Desktop
- Java 17
- Maven 3.6+

## ⚡ Como executar

### Opção 1: Script automatizado (Windows)

```cmd
docker-manager.bat
```

### Opção 2: Comandos Docker diretos

#### Iniciar tudo:

```bash
docker-compose up -d
```

#### Apenas MongoDB:

```bash
docker-compose up -d mongodb mongo-express
```

#### Parar tudo:

```bash
docker-compose down
```

## 🌐 URLs de Acesso

- **API Spring Boot**: http://localhost:8080
- **MongoDB**: localhost:27017
- **Mongo Express** (Interface Web): http://localhost:8081

## 📊 Mongo Express

Interface web para gerenciar o MongoDB:

- **URL**: http://localhost:8081
- **Usuário**: admin
- **Senha**: admin123

## 🔧 Configurações

### Banco de Dados

- **Database**: lmsfilmes
- **Host**: mongodb (container) / localhost (local)
- **Porta**: 27017
- **Usuário**: admin
- **Senha**: admin123

### Coleções Criadas

- `users` - Usuários do sistema
- `movies` - Filmes avaliados
- `favorites` - Filmes favoritos
- `series` - Séries avaliadas
- `favoriteSeries` - Séries favoritas

## 🔍 Comandos Úteis

### Ver logs da aplicação:

```bash
docker-compose logs -f lms-app
```

### Acessar MongoDB CLI:

```bash
docker exec -it lms-mongodb mongosh -u admin -p admin123 --authenticationDatabase admin
```

### Rebuild da aplicação:

```bash
docker-compose down
docker-compose build --no-cache lms-app
docker-compose up -d
```

## 📝 Mudanças Principais

### 1. Dependências alteradas:

- ✅ `spring-boot-starter-data-mongodb`
- ❌ `spring-boot-starter-data-jpa`
- ❌ `mysql-connector-j`

### 2. Modelos refatorados:

- `@Entity` → `@Document`
- `@Table` → `@Document(collection = "...")`
- `@GeneratedValue` → `@Id` (String)
- `@Column` → campos diretos
- `@Indexed` para performance

### 3. Repositórios:

- `JpaRepository` → `MongoRepository`
- `Long` → `String` nos IDs

## 🚨 Troubleshooting

### Porta 27017 em uso:

```bash
docker-compose down
# Matar processos na porta
netstat -ano | findstr :27017
```

### Rebuild completo:

```bash
docker-compose down --volumes
docker-compose build --no-cache
docker-compose up -d
```

### Limpar dados MongoDB:

```bash
docker-compose down -v
docker-compose up -d
```

## 📚 Estrutura de Dados

### Exemplo de documento User:

```json
{
  "_id": "objectId",
  "name": "João Silva",
  "email": "joao@email.com",
  "nickname": "joao123",
  "password": "$2a$10$...",
  "role": "USER"
}
```

### Índices criados automaticamente:

- `users.email` (único)
- `users.nickname` (único)
- `movies.movieId`
- `favorites.nickname + movieId` (composto, único)
