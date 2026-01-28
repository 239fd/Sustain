# Все линтеры одной командой

``
./mvnw clean compile spotless:check modernizer:modernizer spotbugs:check pmd:check pmd:cpd-check
``
# Тесты
``
./mvnw test
``

# Полная сборка
``
./mvnw clean package
``

# Исправить форматирование
``
./mvnw spotless:apply
``