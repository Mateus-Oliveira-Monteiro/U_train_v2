<h1>U-Train Interface</h1>

Project Status:  in progress 

Documentation: https://docs.google.com/document/d/1ydXdu1GTUevtnLdOYo8bkMAjRQyXn6GpZxvUo5MoHhw/edit?usp=sharing

## 📦 Retrofit no Projeto

**Retrofit** é uma biblioteca HTTP client type-safe para Android, utilizada aqui para facilitar o consumo da API REST da Wger.

### Retrofit:

- Define endpoints como interfaces Java (`@GET`, `@Query`, `@Path`)
- Assíncrono por padrão (sem bloquear a UI)
- Conversão automática de JSON para objetos Java (via GSON)
- Suporte a interceptadores e tratamento de erros

### 📄 Dependências no `build.gradle` (Módulo App):

```gradle
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
```

## 🌐 Endpoints da API Wger Utilizados

| Endpoint                  | Descrição                                                 |
| ------------------------- | --------------------------------------------------------- |
| `GET /exercise/`          | Lista paginada de exercícios                              |
| `GET /exerciseinfo/{id}/` | Detalhes completos do exercício com traduções             |
| `GET /muscle/`            | Lista de grupos musculares para mapeamento                |

---

## Estrutura de Integração

### Interface: `WgerApiService.java`

Define métodos de requisição HTTP (ex: `getExercises()`, `getExerciseById()`).

### Cliente Retrofit: `RetrofitClient.java`

Cria uma instância singleton configurada com:

* URL base da API: `https://wger.de/api/v2/`
* Conversor GSON

### Modelos de Dados (`model/`)

Contêm classes que representam a estrutura JSON:

* `Exercise.java`, `ExerciseResponse.java`
* `ExerciseInfo.java`, `Translation.java`
* `Muscle.java`, `MuscleResponse.java`

### Activities:

#### `MuscleGroupSelectionActivity.java`

* Exibe botões de grupos musculares.
* Envia o ID via `Intent` para a próxima Activity.

#### `ExerciseListActivity.java`

* Recebe o ID do músculo selecionado.
* Chama `getExercises()` e filtra localmente pelos músculos (`muscles`, `muscles_secondary`).
* Para cada exercício filtrado, chama `getExerciseById(id)` para obter o nome/descrição traduzidos.
* Popula a lista com objetos `Exercicio` em um `RecyclerView`.

---

## 🔁 Reutilização de Componentes Existentes

### `planilhas/Exercicio.java`

* Recebe dados da API mapeados para o modelo existente (ID, nome, descrição).

### `planilhas/ExercicioAdapter.java`

* Usado para exibir a lista no `RecyclerView`.
* Nenhuma modificação estrutural foi necessária, apenas os dados alimentados foram adaptados.

## 🔗 Referência

* [Wger API Documentation](https://wger.de/api/v2/)
