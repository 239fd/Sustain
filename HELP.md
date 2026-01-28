# Sustain Microservices

## Разработчики
- Pavel Karliuk
- Evgenii Shostak
- Aleksandr Shubin

## Требования
- Java 25
- Maven 3.9+

## Команды

### Все линтеры одной командой
```bash
./mvnw clean compile spotless:check pmd:check pmd:cpd-check
```

> **Примечание:** Modernizer и SpotBugs временно отключены - не поддерживают Java 25

### Тесты
```bash
./mvnw test
```

### Полная сборка
```bash
./mvnw clean package
```

### Исправить форматирование
```bash
./mvnw spotless:apply
```
