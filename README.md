# VitalisEnchants

[![Paper](https://img.shields.io/badge/Paper-1.21.11-2b2b2b?logo=paper&logoColor=white)](https://papermc.io/)
[![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk&logoColor=white)](https://adoptium.net/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
[![Tests](https://img.shields.io/badge/tests-JUnit-informational)](src/test/java)

16 кастомных зачарований для **Purpur/Paper 1.21.11**: оружие, броня, инструменты
и луки. Все числа — в конфиге, все эффекты — в коде, без NMS и внешних библиотек.

## Зачарования

### Оружие (меч/топор), до III уровня

| ID | Название | Что делает |
|---|---|---|
| `storm` | Гроза | Шанс ударить молнией по цели |
| `vampire` | Вампир | Восстанавливает здоровье за нанесённый урон |
| `scholar` | Учёный | Больше опыта с убитых мобов |
| `frost` | Мороз | Накладывает замедление |
| `venom` | Яд | Накладывает отравление |
| `berserk` | Ярость | Увеличивает урон |

### Броня

| ID | Название | Слот | Что делает |
|---|---|---|---|
| `owl` | Сова | шлем | Ночное зрение |
| `gills` | Жабры | шлем | Подводное дыхание |
| `vigor` | Стойкость | нагрудник | Регенерация |
| `spring` | Пружина | ботинки | Прыгучесть |
| `feather` | Пёрышко | ботинки | Убирает урон от падения |

### Инструменты и лук

| ID | Название | Предмет | Что делает |
|---|---|---|---|
| `drill` | Бур | кирка | Копает 3×3 |
| `haste` | Спешка | кирка | Ускорение копания |
| `autosmelt` | Плавка | кирка | Руда выпадает уже переплавленной |
| `lumberjack` | Дровосек | топор | Валит дерево целиком |
| `hunter` | Охотник | лук/арбалет | Больше урона от стрел |

## Команды

Только для операторов — так задумано: зачарования не продаются и не выпадают
из стола, их выдаёт администрация.

| Команда | Что делает |
|---|---|
| `/venchant give <player> <id> [level]` | Выдать зачарование на предмет в руке |
| `/venchant remove <player> <id>` | Снять зачарование |
| `/venchant list` | Показать все ID и названия |

Алиас: `/vench`.

## Права

| Право | По умолчанию |
|---|---|
| `vitalisenchants.admin` | оператор |

## Конфиг

`plugins/VitalisEnchants/config.yml` — по каждому зачарованию:

```yaml
enchants:
  storm:
    enabled: true
    max-level: 3
    chance-base: 0.10        # шанс срабатывания на I уровне
    chance-per-level: 0.10   # +10% за уровень
  frost:
    enabled: true
    max-level: 3
    chance-base: 0.25
    duration-base: 40        # тики (20 тиков = 1 сек)
    duration-per-level: 20
  vampire:
    enabled: true
    mult-base: 1.10          # множитель эффекта
    mult-per-level: 0.10
```

Пропущенные ключи берут значения из кода — конфиг можно не дописывать целиком.
`enabled: false` выключает зачарование, не ломая уже выданные предметы.

## Установка

```bash
./gradlew build       # Windows: gradlew.bat build
# build/libs/VitalisEnchants-1.1.0.jar -> plugins/
```

Нужен JDK 21. После копирования jar перезапустите сервер.

## Тесты

Математика шансов/множителей, таблица плавки, 3×3-бур и парсинг предметов
покрыты JUnit:

```bash
./gradlew test
```

## Лицензия

MIT — см. [LICENSE](LICENSE).
