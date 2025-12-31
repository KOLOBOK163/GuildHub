<template>
  <div class="management-container">
    <div class="header-actions">
      <button @click="showCreateModal = true" class="btn-primary">+ Добавить тренировку</button>
    </div>

    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Дата и время</th>
            <th>Тип</th>
            <th>Команда ID</th>
            <th>Оппонент</th>
            <th>Действия</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="session in sessions" :key="session.id">
            <td>{{ session.id }}</td>
            <td>{{ formatDateTime(session.dateTime) }}</td>
            <td>{{ session.type }}</td>
            <td>{{ session.teamId }}</td>
            <td>{{ session.opponent || '-' }}</td>
            <td class="actions">
              <button @click="editSession(session)" class="btn-edit">Редактировать</button>
              <button @click="deleteSession(session.id)" class="btn-delete">Удалить</button>
            </td>
          </tr>
          <tr v-if="sessions.length === 0">
            <td colspan="6" class="empty-state">Нет данных</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Create/Edit Modal -->
    <div v-if="showCreateModal || editingSession" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <h2>{{ editingSession ? 'Редактировать тренировку' : 'Добавить тренировку' }}</h2>
        <form @submit.prevent="saveSession">
          <div class="form-group">
            <label>Дата и время *</label>
            <input v-model="formData.dateTime" type="datetime-local" required />
          </div>
          <div class="form-group">
            <label>Тип *</label>
            <select v-model="formData.type" required>
              <option value="PRACTICE">Practice</option>
              <option value="SCRIM">Scrim</option>
              <option value="MATCH">Match</option>
            </select>
          </div>
          <div class="form-group">
            <label>ID команды *</label>
            <input v-model.number="formData.teamId" type="number" required />
          </div>
          <div class="form-group">
            <label>Оппонент</label>
            <input v-model="formData.opponent" type="text" />
          </div>
          <div class="form-group">
            <label>Информация о сервере</label>
            <input v-model="formData.serverInfo" type="text" />
          </div>
          <div class="form-group">
            <label>Описание</label>
            <textarea v-model="formData.description" rows="3"></textarea>
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
  name: 'TrainingSessionsManagement',
  data() {
    return {
      sessions: [],
      showCreateModal: false,
      editingSession: null,
      formData: {
        dateTime: '',
        type: 'PRACTICE',
        teamId: null,
        opponent: '',
        serverInfo: '',
        description: ''
      }
    };
  },
  mounted() {
    this.loadSessions();
  },
  methods: {
    async loadSessions() {
      try {
        const token = localStorage.getItem('token');
        const response = await fetch('http://localhost:8080/api/training-sessions', {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        if (response.ok) {
          this.sessions = await response.json();
        }
      } catch (error) {
        console.error('Error loading sessions:', error);
      }
    },
    formatDateTime(dateTime) {
      if (!dateTime) return '-';
      return new Date(dateTime).toLocaleString('ru-RU');
    },
    editSession(session) {
      this.editingSession = session;
      const dt = session.dateTime ? new Date(session.dateTime).toISOString().slice(0, 16) : '';
      this.formData = {
        dateTime: dt,
        type: session.type || 'PRACTICE',
        teamId: session.teamId || null,
        opponent: session.opponent || '',
        serverInfo: session.serverInfo || '',
        description: session.description || ''
      };
    },
    async saveSession() {
      try {
        const token = localStorage.getItem('token');
        const url = this.editingSession
          ? `http://localhost:8080/api/training-sessions/${this.editingSession.id}`
          : 'http://localhost:8080/api/training-sessions';
        const method = this.editingSession ? 'PUT' : 'POST';

        const data = {
          ...this.formData,
          dateTime: new Date(this.formData.dateTime).toISOString()
        };

        const response = await fetch(url, {
          method,
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(data)
        });

        if (response.ok) {
          this.closeModal();
          this.loadSessions();
        } else {
          alert('Ошибка при сохранении');
        }
      } catch (error) {
        console.error('Error saving session:', error);
        alert('Ошибка при сохранении');
      }
    },
    async deleteSession(id) {
      if (!confirm('Вы уверены, что хотите удалить эту тренировку?')) return;

      try {
        const token = localStorage.getItem('token');
        const response = await fetch(`http://localhost:8080/api/training-sessions/${id}`, {
          method: 'DELETE',
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });

        if (response.ok) {
          this.loadSessions();
        } else {
          alert('Ошибка при удалении');
        }
      } catch (error) {
        console.error('Error deleting session:', error);
        alert('Ошибка при удалении');
      }
    },
    closeModal() {
      this.showCreateModal = false;
      this.editingSession = null;
      this.formData = {
        dateTime: '',
        type: 'PRACTICE',
        teamId: null,
        opponent: '',
        serverInfo: '',
        description: ''
      };
    }
  }
};
</script>

<style scoped>
/* Используем те же стили, что и в VideosManagement */
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

