Шаурма Гриль - готовый исправленный проект.

Если хочешь собрать через GitHub:
1. Создай пустой репозиторий.
2. Распакуй этот архив.
3. Загрузи В САМ КОРЕНЬ репозитория всё содержимое папки shaurma_grill_app_ready:
   .github
   app
   gradle
   gradlew
   gradlew.bat
   build.gradle.kts
   settings.gradle.kts
   gradle.properties
   и остальные файлы.
4. Не загружай сам zip.
5. После загрузки открой Actions.
6. Дождись workflow Build APK.
7. Скачай артефакт app-debug.

Если хочешь открыть в Android Studio:
- открой именно папку shaurma_grill_app_ready как проект.

Важно:
- gradlew и gradlew.bat здесь кастомные: они сами скачивают Gradle по distributionUrl.
- Android SDK на GitHub ставится через workflow автоматически.
