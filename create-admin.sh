#!/bin/bash

# Скрипт для создания первого админа через API
# Использование: ./create-admin.sh [username] [password] [email]

USERNAME=${1:-admin}
PASSWORD=${2:-admin123}
EMAIL=${3:-admin@guildhub.com}

echo "Создание админа..."
echo "Username: $USERNAME"
echo "Email: $EMAIL"
echo ""

# Проверяем, запущен ли сервер
if ! curl -s http://localhost:8080/api/auth/login > /dev/null 2>&1; then
    echo "ОШИБКА: Сервер не запущен на http://localhost:8080"
    echo "Запустите приложение и попробуйте снова."
    exit 1
fi

# Создаем админа через API
RESPONSE=$(curl -s -X POST "http://localhost:8080/api/admin-init/create-first-admin" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=$USERNAME&password=$PASSWORD&email=$EMAIL")

echo "$RESPONSE"
echo ""

# Проверяем успешность
if echo "$RESPONSE" | grep -q "успешно создан"; then
    echo "✅ Админ успешно создан!"
    echo ""
    echo "Теперь вы можете войти с учетными данными:"
    echo "  Username: $USERNAME"
    echo "  Password: $PASSWORD"
    echo ""
    echo "⚠️  ВАЖНО: После входа смените пароль!"
    echo "⚠️  ВАЖНО: Отключите endpoint /api/admin-init/** в SecurityConfig для production!"
else
    echo "❌ Ошибка при создании админа"
    exit 1
fi

