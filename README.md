# Комбинированное применение реактивного программирование и паттернов устойчивости в микросервисной архитектуре в условиях отказов и высокой нагрузки

Этот репозиторий содержит набор реализаций микросервисной системы, предназначенной для проведения нагрузочного тестирования и анализа эффективности различных паттернов отказоустойчивости в условиях сбоев и высокой нагрузки. Система моделирует типичный заказной поток (`Order → Customer → Product → Payment → Kafka → Notification`) с возможностью гибкой настройки архитектурных конфигураций.

Архитектурная схема системы представлена в файле `diagram.drawio`.

## Структура веток

Ветка `main` пустая и используется как основная (default), но все реализации размещены в отдельных ветках:


- [`native_baseline`](https://github.com/st1gmat/spbu_diploma_practice/tree/native_baseline) - минимальная **"нативная" версия системы**. Используется только Spring Boot и REST. Все вызовы синхронные, отказоустойчивость не реализована. Является отправной точкой для реализации других конфигураций и для сравнения.

- [`native_reactive`](https://github.com/st1gmat/spbu_diploma_practice/tree/native_reactive) - переход к **реактивной модели Order-сервиса**. Остальные сервисы остаются синхронными. Исследуется влияние реактивности в цепочке вызовов.

- [`native_sync_retry_timeout`](https://github.com/st1gmat/spbu_diploma_practice/tree/native_sync_retry_timeout) - добавлены паттерны **Retry** и **Timeout** на основе `native_baseline`. Всё остаётся синхронным

- [`native_reactive_retry`](https://github.com/st1gmat/spbu_diploma_practice/tree/native_reactive_retry) - реактивная модель (`native_reactive`) с добавлением **Retry** и **Timeout**

- [`native_reactive_cb_bh_retry_timeout`](https://github.com/st1gmat/spbu_diploma_practice/tree/native_reactive_cb_bh_retry_timeout) - включает **Circuit Breaker**, **Bulkhead**, **Retry**, **Timeout**. Построена на основе `native_reactive`.

- [`native_rate_limiting`](https://github.com/st1gmat/spbu_diploma_practice/tree/native_rate_limiting) - включает Rate Limiting при базовой реализации

- [`native_sync_rt_bh_cache`](https://github.com/st1gmat/spbu_diploma_practice/tree/native_sync_rt_bh_cache) - включает Throttling + Cache-Aside при базовой реализации

- [`native_reactive_throttling_cache`](https://github.com/st1gmat/spbu_diploma_practice/tree/native_reactive_throttling_cache) - включает Throttling + Cache-Aside при реактивной реализации

<details>
<summary>Прочее:</summary>
  
- [`basic_implementation`](https://github.com/st1gmat/spbu_diploma_practice/tree/basic_implementation) - реализация **базовой системы** на стеке **Spring Boot + Spring Cloud** (используются Feign, Eureka, Config Server и пр.)

- [`reactive_implementation`](https://github.com/st1gmat/spbu_diploma_practice/tree/reactive_implementation) - полностью реактивная реализация всей системы, основанная на концепции из `basic_implementation`, но с применением **Spring WebFlux** и реактивных драйверов. Является "реактивным аналогом" `basic_implementation`

- [`reactive-order-service`](https://github.com/st1gmat/spbu_diploma_practice/tree/reactive-order-service) - содержит только реактивную реализацию **Order-сервиса** относительно `basic_implementation`

</details>   


## Используемый стек

- **Spring Boot / Web / WebFlux**
- **PostgreSQL / MongoDB**
- **Kafka**
- **Resilience4j**
- **JMeter** — нагрузочное тестирование
- **Prometheus + Grafana** — мониторинг
- **Zipkin** — распределённый трейсинг
- **Docker / Docker Compose**

