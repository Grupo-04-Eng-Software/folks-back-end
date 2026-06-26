# FolksFlow Backend - Sistema Kanban & RH

Este é o backend do **FolksFlow**, um sistema de gerenciamento de tarefas (Kanban) voltado para processos de RH. A API foi desenvolvida seguindo os princípios SOLID, arquitetura REST versionada (v1) e suporte a notificações em tempo real.

## 🚀 Como Executar

O projeto está totalmente containerizado com Docker.

### Pré-requisitos
*   [Docker](https://www.docker.com/get-started)
*   [Docker Compose](https://docs.docker.com/compose/install/)

### Passo a Passo
1.  Na raiz do projeto, execute o comando:
    ```bash
    docker-compose up --build
    ```
2.  A API estará disponível em: `http://localhost:8080`
3.  A documentação visual (Swagger) estará em: `http://localhost:8080/swagger-ui.html`
4.  O banco de dados PostgreSQL estará disponível em: `localhost:5432`

---

## 🔐 Autenticação e Autorização

A API utiliza **JWT (JSON Web Token)** com suporte a **Refresh Token**.

*   **Access Token:** Enviado via Header `Authorization: Bearer <token>`. Expiração curta.
*   **Refresh Token:** Utilizado para renovar a sessão sem novo login via rota `/api/v1/auth/refresh`.
*   **Roles:**
    *   `ROLE_USER`: Usuário padrão.
    *   `ROLE_ADMIN`: Permissões totais (excluir espaços e projetos).

---

## 📑 Referência Completa da API (v1)

### 🔑 Autenticação (`/api/v1/auth`)
| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `GET` | `/me` | Obtém o perfil do usuário logado |
| `POST` | `/login` | Autenticação inicial e retorno de tokens |
| `POST` | `/register` | Cadastro de novo usuário |
| `POST` | `/refresh` | Renovação de Access Token via Refresh Token |

### 📂 Espaços (`/api/v1/spaces`)
| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `GET` | `/` | Lista espaços do usuário logado |
| `POST` | `/` | Cria um novo espaço |
| `PUT` | `/{id}` | Edita um espaço existente |
| `DELETE` | `/{id}` | Exclui um espaço (Requer ROLE_ADMIN) |

### 🏗️ Projetos (`/api/v1/projects`)
| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `GET` | `/space/{spaceId}` | Lista projetos vinculados a um espaço |
| `POST` | `/` | Cria um projeto dentro de um espaço |
| `PUT` | `/{id}` | Edita um projeto existente |
| `DELETE` | `/{id}` | Exclui um projeto (Requer ROLE_ADMIN) |

### 📊 Kanban & Status (`/api/v1/status`)
| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `GET` | `/project/{projectId}` | Lista colunas (Status) de um projeto |
| `POST` | `/` | Cria uma nova coluna no Kanban |
| `DELETE` | `/{id}` | Exclui uma coluna |

### 📋 Tarefas (`/api/v1/tasks`)
| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `GET` | `/` | Pesquisa global de tarefas (Filtros: status, prioridade, tag, título, **usuário**) |
| `GET` | `/status/{statusId}` | Lista tarefas de uma coluna específica |
| `GET` | `/overdue` | Lista tarefas com prazo vencido |
| `POST` | `/` | Cria uma nova tarefa |
| `PATCH` | `/{id}/move` | Move tarefa entre colunas ou muda posição |
| `POST` | `/{id}/assign/{userId}` | Atribui um usuário à tarefa |
| `DELETE` | `/{id}/unassign/{userId}` | Remove a atribuição de um usuário |
| `DELETE` | `/{id}` | Exclui uma tarefa (Soft Delete) |
| `POST` | `/{id}/checklist` | Adiciona item ao checklist da tarefa |
| `PATCH` | `/checklist/{itemId}/toggle` | Marca/Desmarca item do checklist |
| `POST` | `/{id}/time/start` | Inicia cronômetro de tempo gasto |
| `POST` | `/{id}/time/stop` | Para cronômetro e registra duração |
| `GET` | `/{id}/time/total` | Obtém soma total de tempo gasto na tarefa |

### 🏢 RH: Candidatos (`/api/v1/candidates`)
| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `GET` | `/` | Pesquisa/Lista global de candidatos (Paginado) |
| `POST` | `/` | Cadastra um novo candidato |
| `PUT` | `/{id}` | Edita um candidato existente |
| `POST` | `/{id}/associate-company/{cid}` | Vincula candidato a uma empresa |
| `POST` | `/{id}/resume` | Upload de currículo (Multipart/Form-Data) |
| `GET` | `/{id}/resume/download` | Download do arquivo de currículo |
| `DELETE` | `/{id}` | Exclui candidato (Soft Delete) |

### 🏭 RH: Empresas (`/api/v1/companies`)
| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `GET` | `/` | Lista todas as empresas clientes |
| `GET` | `/{id}` | Obtém detalhes de uma empresa específica |
| `GET` | `/{id}/candidates` | Lista candidatos vinculados à empresa |
| `POST` | `/` | Cadastra uma nova empresa |
| `PUT` | `/{id}` | Edita uma empresa existente |
| `DELETE` | `/{id}` | Exclui empresa (Soft Delete) |

### 👥 Usuários (`/api/v1/users`)
| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `GET` | `/` | Lista todos os usuários (Requer autenticação) |
| `GET` | `/{id}` | Obtém detalhes de um usuário específico |
| `PUT` | `/{id}` | Edita dados do usuário (nome, email, senha, cargo, foto, endereço) |

### 💬 Comentários & Atividades (`/api/v1/activities`)
| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `GET` | `/task/{taskId}` | Lista timeline de atividades/comentários |
| `POST` | `/` | Adiciona um comentário a uma tarefa |

### 🏷️ Etiquetas (`/api/v1/tags`)
| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `GET` | `/space/{spaceId}` | Lista etiquetas disponíveis no espaço |
| `POST` | `/` | Cria uma nova etiqueta |
| `POST` | `/task/{taskId}/associate/{tagId}` | Vincula etiqueta a uma tarefa |

### 🔔 Notificações (`/api/v1/notifications`)
| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `GET` | `/` | Lista notificações do usuário logado |
| `GET` | `/unread/count` | Obtém total de notificações não lidas |
| `PATCH` | `/{id}/read` | Marca notificação como lida |

### ⚙️ Configurações e Auxiliares
| Entidade | Método | Rota | Descrição |
| :--- | :--- | :--- | :--- |
| **Prioridades** | `GET` | `/api/v1/priorities` | Lista níveis de prioridade |
| **Prioridades** | `POST` | `/api/v1/priorities` | Cria novo nível de prioridade |
| **Endereços** | `POST` | `/api/v1/addresses` | Cadastro manual de endereço |

---

### 🚀 Produtividade e Auditoria
| Funcionalidade | Descrição |
| :--- | :--- |
| **Soft Delete** | Entidades principais (`Task`, `Project`, `Candidate`, `Company`, `Space`, `User`) usam exclusão lógica via campo `is_active`. |
| **AuditLog** | Toda criação, edição ou remoção em Services é registrada automaticamente no banco via AOP (Aspect-Oriented Programming). |
| **Filtros Avançados** | Busca dinâmica poderosa usando Spring Data Specifications para tarefas e candidatos. |

---

## 🛠️ Tecnologias Utilizadas
*   **Java 21** & **Spring Boot 3.2.5**
*   **Spring Security** + **Auth0 JWT** (Access & Refresh Token)
*   **Spring AOP** (Auditoria de Ações do Usuário)
*   **PostgreSQL 16**
*   **Hibernate 6** (Soft Delete e Auditoria)
*   **Spring WebSocket (STOMP)**
*   **JUnit 5** & **MockMvc**
*   **Docker** & **Docker Compose**

---

## 🧪 Testes
Para rodar os testes automatizados:
```bash
./mvnw clean test
```

---

## 📮 Postman
Importe o arquivo `folksflow_postman_collection.json` localizado na raiz para testar todas as rotas rapidamente.

---

## 🐳 DevOps & Infraestrutura

Esta seção documenta a governança de versionamento e a virtualização da aplicação (atende ao Projeto de Governança e Infraestrutura).

### 🌿 Governança de Código (Git Flow)
O repositório segue o **Git Flow**:
* `master` — branch estável de produção (recebe apenas releases).
* `develop` — branch de integração das features.
* `feature/*` — desenvolvimento de novas funcionalidades, finalizadas via **Pull Request** para `develop`.
* **Tags** de versão (ex: `v1.0.0`) marcam releases oficiais e disparam o pipeline de publicação (CD).

### 📦 Virtualização do Banco de Dados
O serviço `db` no [`docker-compose.yml`](docker-compose.yml) sobe um **PostgreSQL 16** isolado:
* Variáveis de ambiente: `POSTGRES_DB`, `POSTGRES_USER`, `POSTGRES_PASSWORD` (configuráveis via `.env`).
* **Volume** `postgres_data` para **persistência** dos dados entre reinícios do contêiner.
* Exposto em `localhost:5433` (mapeado para a `5432` interna, evitando conflito com Postgres local).

Subir apenas o banco:
```bash
docker compose up -d db
```

### 🏗️ Virtualização da Aplicação (Multi-stage Build)
O [`Dockerfile`](Dockerfile) usa **multi-stage build**: um estágio compila o `.jar` com Maven/JDK 21 e o estágio final usa apenas o **JRE Alpine**, gerando uma imagem enxuta.

### 🔗 Orquestração Completa
O `docker-compose.yml` sobe **aplicação + banco** juntos, interligados pela rede interna `folksflow-net` (**Service Discovery**: o app acessa o banco pelo hostname `db`).
```bash
docker compose up --build
```

### ❤️ Healthcheck (Observabilidade e Resiliência)
* Endpoint de saúde: **`GET /health`** (público) — retorna `200 UP` quando a aplicação e o banco estão prontos, ou `503 DOWN` caso o banco esteja indisponível.
* O serviço `app` possui a diretiva `healthcheck` no Compose, garantindo que o contêiner só seja considerado saudável (e receba tráfego) quando estiver 100% pronto.

### 🤖 Pipelines (GitHub Actions)
| Workflow | Arquivo | Gatilho | Função |
| :--- | :--- | :--- | :--- |
| **CI** | [`.github/workflows/ci.yml`](.github/workflows/ci.yml) | Push/PR em `develop`/`master` | Build + testes (`mvnw verify`) |
| **Security (Trivy)** | [`.github/workflows/security.yml`](.github/workflows/security.yml) | Abertura de Pull Request | Varredura de vulnerabilidades e segredos expostos (DevSecOps) |
| **CD** | [`.github/workflows/cd.yml`](.github/workflows/cd.yml) | Push de tag `v*.*.*` | Build e publicação da imagem no **Docker Hub** |

#### 🔑 Secrets necessários (Settings → Secrets and variables → Actions)
Para o CD publicar no Docker Hub, configure no GitHub:
| Secret | Descrição |
| :--- | :--- |
| `DOCKERHUB_USERNAME` | Seu usuário do Docker Hub |
| `DOCKERHUB_TOKEN` | Access Token gerado em hub.docker.com → Account Settings → Security |

A imagem é publicada como `DOCKERHUB_USERNAME/folks-back-end` com as tags da versão (ex: `1.0.0`, `1.0`) e `latest`.

### 📋 Variáveis de ambiente (`.env` opcional)
| Variável | Padrão | Descrição |
| :--- | :--- | :--- |
| `DB_NAME` | `folksflow` | Nome do banco |
| `DB_USER` | `postgres` | Usuário do banco |
| `DB_PASSWORD` | `root` | Senha do banco |
| `JWT_SECRET` | `senha-braba-demais-no-docker` | Segredo de assinatura do JWT |
| `DOCKERHUB_USERNAME` | `folksflow` | Prefixo da imagem buildada localmente |
