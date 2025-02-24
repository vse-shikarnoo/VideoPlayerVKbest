Демонстрация работы приложения - [https://drive.google.com/file/d/1OXwp9W2RuEq4BmfHmt4yTeH-gbv8_sks/view?usp=drive_link](https://drive.google.com/file/d/1Gk8lrJSf--FcyxfTL3PV10-8334CQn_g/view?usp=sharing)

# 🎵 Видеоплеер на Kotlin (Jetpack Compose + Pexels API)
Приложение для поиска и воспроизведения видео с Pexels API, написанное на Kotlin с использованием
Jetpack Compose.

## 🚀 Функциональность
- 🔍 Поиск видео [в процессе нужен UI].
- 📊 Получение видео с Pexels.
- ▶️ Воспроизведение видео через `ExoPlayer`.
- ⏯️ Управление воспроизведением (пауза, продолжение, перемотка).
- 🎨 UI на Jetpack Compose.

## Техническое задание
### ✅ 1. Экран со списком видео:
* ✅ Список видео (более 5) через Pexels API
* ✅ Отображение thumbnail, названия и продолжительности
* ✅ Загрузка из удаленного API [Pexels API]
* ✅ Pull-to-refresh реализован
### ✅ 2. Экран просмотра видео:
* ✅ Открытие видео по клику
* ✅ Использование ExoPlayer
* ✅ Кнопки управления воспроизведением
* ✅ Поворот экрана для полноэкранного просмотра
### ✅ 3. Архитектура и код:
* ✅ MVVM
* ✅ Retrofit
* ✅ Dagger Hilt
* ✅ Android 8.0+
### ✅ 4. Дополнительные требования:
* ✅ Кеширование через Room
* ✅ Обработка ошибок сети
* ✅ Поддержка обеих ориентаций

## 🛠️ Технологии
- **Язык**: Kotlin
- **Архитектура**: MVVM + Jetpack Compose
- **Сетевые запросы**: Retrofit + OkHttp
- **DI**: Dagger Hilt
- **Асинхронность**: Coroutines + Flow
- **Воспроизведение видео**: ExoPlayer

## 📡 API
Используется [Pexels API + VPN](https://api.pexels.com/).
