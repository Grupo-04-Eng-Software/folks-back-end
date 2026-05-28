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
3.  O banco de dados PostgreSQL estará disponível em: `localhost:5432`

---

## 🔐 Autenticação e Autorização

A API utiliza **JWT (JSON Web Token)** com suporte a **Refresh Token**.

*   **Access Token:** Enviado via Header `Authorization: Bearer <token>`. Expiração curta.
*   **Refresh Token:** Utilizado para renovar a sessão sem novo login via rota `/api/v1/auth/refresh`.
*   **Roles:**
    *   `ROLE_USER`: Usuário padrão.
    *   `ROLE_ADMIN`: Permissões totais (excluir espaços e projetos).

---

## 📡 Notificações em Tempo Real (WebSocket)

As notificações são entregues via protocolo **STOMP** sobre WebSocket.

*   **Endpoint de Conexão:** `ws://localhost:8080/ws-folks`
*   **Tópico de Notificações do Usuário:** `/user/{email}/queue/notifications`
*   **Eventos:** Atribuição de tarefas, convites para espaços/projetos e novos comentários.

---

## 📑 Referência da API (v1)

### 🔑 Autenticação (Auth)
| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `POST` | `/api/v1/auth/register` | Cadastro de novo usuário |
| `POST` | `/api/v1/auth/login` | Login inicial e retorno de tokens |
| `POST` | `/api/v1/auth/refresh` | Renovação de Access Token via Refresh Token |

### 📂 Espaços (Spaces)
| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `GET` | `/api/v1/spaces` | Lista espaços do usuário logado |
| `POST` | `/api/v1/spaces` | Cria novo espaço |
| `DELETE` | `/api/v1/spaces/{id}` | Exclui espaço (ADMIN) |

### 🏗️ Projetos (Projects)
| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `GET` | `/api/v1/projects/space/{spaceId}` | Lista projetos de um espaço |
| `POST` | `/api/v1/projects` | Cria projeto dentro de um espaço |
| `DELETE` | `/api/v1/projects/{id}` | Exclui projeto (ADMIN) |

### 📋 Kanban (Status & Tasks)
| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `GET` | `/api/v1/status/project/{projectId}` | Lista colunas (Status) do projeto |
| `POST` | `/api/v1/status` | Cria nova coluna no Kanban |
| `GET` | `/api/v1/tasks/status/{statusId}` | Lista tarefas de uma coluna |
| `POST` | `/api/v1/tasks` | Cria nova tarefa |
| `PATCH` | `/api/v1/tasks/{id}/move` | Move tarefa entre colunas ou muda posição |

### 💬 Colaboração (Activities & Tags)
| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `GET` | `/api/v1/activities/task/{taskId}` | Histórico de comentários da tarefa |
| `POST` | `/api/v1/activities` | Adiciona comentário a uma tarefa |
| `GET` | `/api/v1/tags/space/{spaceId}` | Lista etiquetas disponíveis no espaço |
| `POST` | `/api/v1/tags` | Cria nova etiqueta |
| `POST` | `/api/v1/tags/task/{taskId}/associate/{tagId}` | Vincula etiqueta à tarefa |

### 🏢 RH (Companies & Candidates)
| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `GET` | `/api/v1/companies` | Lista todas as empresas clientes |
| `GET` | `/api/v1/companies/{id}/candidates` | Candidatos vinculados à empresa |
| `GET` | `/api/v1/candidates` | Lista/Pesquisa global de candidatos |
| `POST` | `/api/v1/candidates/{id}/associate-company/{cid}` | Vincula candidato à empresa |

---

## 🛠️ Tecnologias Utilizadas
*   **Java 21** & **Spring Boot 3.2.5**
*   **Spring Security** + **Auth0 JWT**
*   **PostgreSQL 16**
*   **Hibernate/JPA**
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
