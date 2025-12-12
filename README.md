# Лабораторная работа №2  
## Реактивное программирование и веб-клиенты  
### Вариант 13 — Управление заказами (корзина)

---

## Описание работы

Лабораторная работа состоит из двух независимых Spring Boot сервисов:

- **Service A (client-service)** — клиент, который отправляет запросы к серверу через `WebClient`
- **Service B (server-service)** — сервер, который обрабатывает список идентификаторов товаров и возвращает реактивный результат (`Mono`)



---

## Запуск

### 1. Сервис B (сервер)

```bash
cd server-service
mvn spring-boot:run
```

Сервер стартует на порту **8081**  
(`server.port=8081` в `application.properties`).

### 2. Сервис A (клиент)

```bash
cd client-service
mvn spring-boot:run
```

Клиент стартует на порту **8080**  
(`server.port=8080` в `application.properties`).

---

## API

### Клиентский сервис (A)
Отправляет запрос на сервер:

`POST /basket` → принимает JSON массив itemIds:

#### Пример:

```
POST http://localhost:8080/basket
Content-Type: application/json
[1, 2, 5]
```

### Серверный сервис (B)
Принимает запрос:

`POST /basket` → возвращает реактивный `Mono<Basket>`

#### Пример ответа:
```json
{
  "items": [
    {"id":1,"name":"Keyboard","price":120.0},
    {"id":2,"name":"Mouse","price":50.0},
    {"id":5,"name":"Laptop","price":1500.0}
  ],
  "totalPrice": 1.002,
  "discountApplied": true
}
```

---

## Неоптимальный код

По заданию необходимо реализовать неэффективную логику, чтобы позже профилировать её в ЛР3.

### 1. Лишние сортировки

```java
collected.sort(Comparator.comparing(Item::getName));
        collected.sort(Comparator.comparing(Item::getPrice).reversed());
```

Почему плохо: сортировка — O(n log n). Делать её несколько раз подряд бессмысленно: достаточно одной сортировки с компаратором, объединяющим критерии. Много проходов → лишняя CPU-работа.

### 2. N+1: каждый item по одному

```java
for (Integer id : ids) {
            Item item = itemRepository.findByIdSlow(id);

            // Ненужная сериализация/десериализация
            try {
                String json = mapper.writeValueAsString(item);
                item = mapper.readValue(json, Item.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            collected.add(item);
        }
```

Почему плохо: при N itemIds мы делаем N отдельных вызовов вместо одного запроса за все нужные элементы. Задержки суммируются: если N=20 и задержка 50ms → ~1s простоя.

### 3. Ненужная сериализация/десериализация

```java
try {
                String json = mapper.writeValueAsString(item);
                item = mapper.readValue(json, Item.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
```

Почему плохо: лишняя CPU-работа, аллокация строк/буферов, парсинг JSON. Ничего не меняет в данных, только тратит ресурсы.

### 4. Вложенные циклы для расчета скидок

```java
double total = 0;
        for (Item i1 : collected) {
            for (Item i2 : collected) {
                total += (i1.getPrice() + i2.getPrice()) * 0.0001;
            }
        }
```

Проблема: квадратичная сложность делает код невероятно тяжёлым при росте N (N=1000 → 1M итераций).

---

## Требования для запуска

Maven 3.9.11

Java 17

## Пример использования

После запуска обоих сервисов можно выполнить:


```bash
curl -X POST http://localhost:8080/basket \
     -H "Content-Type: application/json" \
     -d "[1,2,5]"
```

