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
| `DELETE` | `/{id}` | Exclui um espaço (Requer ROLE_ADMIN) |

### 🏗️ Projetos (`/api/v1/projects`)
| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `GET` | `/space/{spaceId}` | Lista projetos vinculados a um espaço |
| `POST` | `/` | Cria um projeto dentro de um espaço |
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
| `GET` | `/` | Pesquisa global de tarefas (Filtros e Paginação) |
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
| `DELETE` | `/{id}` | Exclui empresa (Soft Delete) |

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
