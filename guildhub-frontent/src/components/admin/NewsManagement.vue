<template>
  <div class="management-container">
    <div class="header-actions">
      <button @click="showCreateModal = true" class="btn-primary">+ Добавить новость</button>
    </div>

    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Заголовок</th>
            <th>Автор</th>
            <th>Новая?</th>
            <th>Создано</th>
            <th>Действия</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="news in newsList" :key="news.id">
            <td>{{ news.id }}</td>
            <td>{{ news.title }}</td>
            <td>{{ news.author }}</td>
            <td>{{ news.isNew ? 'Да' : 'Нет' }}</td>
            <td>{{ formatDate(news.createdAt) }}</td>
            <td class="actions">
              <button @click="editNews(news)" class="btn-edit">Редактировать</button>
              <button @click="deleteNews(news.id)" class="btn-delete">Удалить</button>
            </td>
          </tr>
          <tr v-if="newsList.length === 0">
            <td colspan="6" class="empty-state">Нет новостей</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Create/Edit Modal -->
    <div v-if="showCreateModal || editingNews" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <h2>{{ editingNews ? 'Редактировать новость' : 'Добавить новость' }}</h2>
        <form @submit.prevent="saveNews">
          <div class="form-group">
            <label>Заголовок *</label>
            <input v-model="formData.title" type="text" required />
          </div>
          <div class="form-group">
            <label>Автор *</label>
            <input v-model="formData.author" type="text" required />
          </div>
          <div class="form-group">
            <label>Содержание</label>
            <textarea v-model="formData.content" rows="5"></textarea>
          </div>
          <div class="form-group">
            <label>URL изображения</label>
            <input v-model="formData.imageUrl" type="text" placeholder="https://example.com/image.jpg" />
          </div>
          <div class="form-group">
            <label>
              <input type="checkbox" v-model="formData.isNew" />
              Отметить как новую
            </label>
          </div>
          <div class="form-actions">
            <button type="button" @click="closeModal" class="btn-cancel">Отмена</button>
            <button type="submit" class="btn-save">Сохранить</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'NewsManagement',
  data() {
    return {
      newsList: [],
      showCreateModal: false,
      editingNews: null,
      formData: {
        title: '',
        content: '',
        author: '',
        imageUrl: '',
        isNew: true
      }
    };
  },
  mounted() {
    this.loadNews();
  },
  methods: {
    async loadNews() {
      try {
        const token = localStorage.getItem('token');
        const response = await fetch('http://localhost:8080/api/news', {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        if (response.ok) {
          const data = await response.json();
          // Если API возвращает GetAllNewsResponseDto (объект с полем news)
          this.newsList = data.news || data;
        }
      } catch (error) {
        console.error('Error loading news:', error);
        this.newsList = [];
      }
    },
    editNews(news) {
      this.editingNews = news;
      this.formData = {
        title: news.title || '',
        content: news.content || '',
        author: news.author || '',
        imageUrl: news.imageUrl || '',
        isNew: news.isNew !== undefined ? news.isNew : true
      };
    },
    async saveNews() {
      try {
        const token = localStorage.getItem('token');
        const url = this.editingNews
          ? `http://localhost:8080/api/news/${this.editingNews.id}`
          : 'http://localhost:8080/api/news';
        const method = this.editingNews ? 'PUT' : 'POST';

        const response = await fetch(url, {
          method,
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(this.formData)
        });

        if (response.ok) {
          this.closeModal();
          this.loadNews();
        } else {
          const errorText = await response.text();
          alert(`Ошибка: ${response.status} — ${errorText}`);
        }
      } catch (error) {
        console.error('Error saving news:', error);
        alert('Ошибка при сохранении новости');
      }
    },
    async deleteNews(id) {
      if (!confirm('Вы уверены, что хотите удалить эту новость?')) return;

      try {
        const token = localStorage.getItem('token');
        const response = await fetch(`http://localhost:8080/api/news/${id}`, {
          method: 'DELETE',
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });

        if (response.ok) {
          this.loadNews();
        } else {
          alert('Ошибка при удалении новости');
        }
      } catch (error) {
        console.error('Error deleting news:', error);
        alert('Ошибка при удалении новости');
      }
    },
    closeModal() {
      this.showCreateModal = false;
      this.editingNews = null;
      this.formData = {
        title: '',
        content: '',
        author: '',
        imageUrl: '',
        isNew: true
      };
    },
    formatDate(isoString) {
      if (!isoString) return '-';
      const date = new Date(isoString);
      return date.toLocaleDateString('ru-RU', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      });
    }
  }
};
</script>

<style scoped>
/* Стили полностью совпадают с TacticalNotesManagement */
.management-container {
  width: 100%;
}

.header-actions {
  margin-bottom: 1.5rem;
}

.btn-primary {
  padding: 0.75rem 1.5rem;
  background: linear-gradient(90deg, #6e8efb, #8a76f0);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(110, 142, 251, 0.4);
}

.table-container {
  background: rgba(30, 30, 30, 0.6);
  border-radius: 12px;
  overflow: hidden;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table thead {
  background: rgba(102, 217, 255, 0.1);
}

.data-table th {
  padding: 1rem;
  text-align: left;
  font-weight: 600;
  color: #66d9ff;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.data-table td {
  padding: 1rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

.data-table tbody tr:hover {
  background: rgba(102, 217, 255, 0.05);
}

.empty-state {
  text-align: center;
  color: #aaa;
  padding: 2rem;
}

.actions {
  display: flex;
  gap: 0.5rem;
}

.btn-edit,
.btn-delete {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 6px;
  font-size: 0.9rem;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-edit {
  background: rgba(102, 217, 255, 0.2);
  color: #66d9ff;
}

.btn-edit:hover {
  background: rgba(102, 217, 255, 0.3);
}

.btn-delete {
  background: rgba(255, 107, 107, 0.2);
  color: #ff6b6b;
}

.btn-delete:hover {
  background: rgba(255, 107, 107, 0.3);
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: #1a1a1a;
  border-radius: 12px;
  padding: 2rem;
  width: 90%;
  max-width: 600px;
  max-height: 90vh;
  overflow-y: auto;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.modal-content h2 {
  margin-bottom: 1.5rem;
  color: #66d9ff;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  color: #ccc;
  font-weight: 500;
}

.form-group input[type="checkbox"] {
  width: auto;
  margin-right: 0.5rem;
}

.form-group input,
.form-group textarea,
.form-group select {
  width: 100%;
  padding: 0.75rem;
  background: rgba(30, 30, 30, 0.8);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 6px;
  color: white;
  font-size: 1rem;
  font-family: 'Roboto', sans-serif;
}

.form-group input:focus,
.form-group textarea:focus,
.form-group select:focus {
  outline: none;
  border-color: #66d9ff;
  box-shadow: 0 0 0 3px rgba(102, 217, 255, 0.2);
}

.form-actions {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
  margin-top: 2rem;
}

.btn-cancel,
.btn-save {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 6px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-cancel {
  background: rgba(255, 255, 255, 0.1);
  color: #ccc;
}

.btn-cancel:hover {
  background: rgba(255, 255, 255, 0.2);
}

.btn-save {
  background: linear-gradient(90deg, #6e8efb, #8a76f0);
  color: white;
}

.btn-save:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(110, 142, 251, 0.4);
}
</style>