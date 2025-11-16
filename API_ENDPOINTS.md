# GuildHub API Endpoints

## Аутентификация
- `POST /api/auth/register` - Регистрация пользователя
- `POST /api/auth/login` - Вход в систему

## Пользователи
- `GET /api/users` - Получить всех пользователей
- `GET /api/users/{userId}` - Получить пользователя по ID
- `POST /api/users` - Создать пользователя
- `PUT /api/users/{userId}` - Обновить пользователя
- `DELETE /api/users/{userId}` - Удалить пользователя
- `POST /api/users/avatar` - Загрузить аватар пользователя

## Игроки (Player Cards)
- `GET /api/players` - Получить всех игроков
- `GET /api/players/{playerId}` - Получить игрока по ID
- `GET /api/players/team/{teamId}` - Получить игроков команды
- `POST /api/players` - Создать карточку игрока
- `PUT /api/players/{playerId}` - Обновить карточку игрока
- `DELETE /api/players/{playerId}` - Удалить карточку игрока

## Команды (Teams)
- `GET /api/teams` - Получить все команды
- `GET /api/teams/{teamId}` - Получить команду по ID
- `POST /api/teams` - Создать команду
- `PUT /api/teams/{teamId}` - Обновить команду
- `DELETE /api/teams/{teamId}` - Удалить команду

## Тренировки (Training Sessions)
- `GET /api/training-sessions` - Получить все тренировки
- `GET /api/training-sessions/{sessionId}` - Получить тренировку по ID
- `GET /api/training-sessions/team/{teamId}` - Получить тренировки команды
- `GET /api/training-sessions/date-range?start={start}&end={end}` - Получить тренировки по периоду
- `POST /api/training-sessions` - Создать тренировку
- `PUT /api/training-sessions/{sessionId}` - Обновить тренировку
- `DELETE /api/training-sessions/{sessionId}` - Удалить тренировку

## Результаты матчей (Match Results)
- `GET /api/match-results` - Получить все результаты матчей
- `GET /api/match-results/{matchResultId}` - Получить результат матча по ID
- `GET /api/match-results/team/{teamId}` - Получить результаты матчей команды
- `GET /api/match-results/result/{result}` - Получить результаты по типу (WIN/LOSS/DRAW)
- `POST /api/match-results` - Создать результат матча
- `PUT /api/match-results/{matchResultId}` - Обновить результат матча
- `DELETE /api/match-results/{matchResultId}` - Удалить результат матча

## Посещаемость (Attendance)
- `GET /api/attendances` - Получить всю посещаемость
- `GET /api/attendances/{attendanceId}` - Получить посещаемость по ID
- `GET /api/attendances/training-session/{trainingSessionId}` - Получить посещаемость тренировки
- `GET /api/attendances/player/{playerCardId}` - Получить посещаемость игрока
- `POST /api/attendances` - Создать запись о посещаемости
- `PUT /api/attendances/{attendanceId}` - Обновить запись о посещаемости
- `DELETE /api/attendances/{attendanceId}` - Удалить запись о посещаемости

## Тактические заметки (Tactical Notes)
- `GET /api/tactical-notes` - Получить все тактические заметки
- `GET /api/tactical-notes/{noteId}` - Получить заметку по ID
- `GET /api/tactical-notes/team/{teamId}` - Получить заметки команды
- `GET /api/tactical-notes/team/{teamId}/public` - Получить публичные заметки команды
- `GET /api/tactical-notes/map/{map}` - Получить заметки по карте
- `POST /api/tactical-notes` - Создать тактическую заметку
- `PUT /api/tactical-notes/{noteId}` - Обновить тактическую заметку
- `DELETE /api/tactical-notes/{noteId}` - Удалить тактическую заметку

## Видео (Videos)
- `GET /api/videos` - Получить все видео
- `GET /api/videos/{videoId}` - Получить видео по ID
- `GET /api/videos/team/{teamId}` - Получить видео команды
- `GET /api/videos/type/{type}` - Получить видео по типу (HIGHLIGHT/TUTORIAL/TACTICAL/TEAM_PRIVATE)
- `GET /api/videos/map/{map}` - Получить видео по карте
- `POST /api/videos` - Создать видео
- `PUT /api/videos/{videoId}` - Обновить видео
- `POST /api/videos/{videoId}/views` - Увеличить счетчик просмотров
- `POST /api/videos/{videoId}/likes` - Увеличить счетчик лайков
- `DELETE /api/videos/{videoId}` - Удалить видео

## Статистика команд (Team Statistics)
- `GET /api/team-statistics` - Получить всю статистику
- `GET /api/team-statistics/{statisticId}` - Получить статистику по ID
- `GET /api/team-statistics/team/{teamId}` - Получить статистику команды
- `GET /api/team-statistics/team/{teamId}/period?start={start}&end={end}` - Получить статистику за период
- `POST /api/team-statistics` - Создать статистику
- `PUT /api/team-statistics/{statisticId}` - Обновить статистику
- `DELETE /api/team-statistics/{statisticId}` - Удалить статистику

## Файлы
- `POST /api/files/upload/{bucket}` - Загрузить файл в bucket

## Примечания
- Все эндпоинты (кроме `/api/auth/**` и `/api/files/**`) требуют JWT токен в заголовке `Authorization: Bearer {token}`
- Даты в запросах должны быть в формате ISO 8601 (например: `2024-01-15T10:30:00`)
- Валидация применяется автоматически через `@Valid` аннотации


