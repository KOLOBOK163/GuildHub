<template>
  <div class="management-container">
    <div class="header-actions">
      <button @click="showCreateModal = true" class="btn-primary">+ Добавить результат матча</button>
    </div>

    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Дата</th>
            <th>Команда</th>
            <th>Оппонент</th>
            <th>Счет</th>
            <th>Результат</th>
            <th>Действия</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="match in matches" :key="match.id">
            <td>{{ match.id }}</td>
            <td>{{ formatDateTime(match.matchDate) }}</td>
            <td>{{ match.teamId }}</td>
            <td>{{ match.opponentName || '-' }}</td>
            <td>{{ match.teamScore }} - {{ match.opponentScore }}</td>
            <td>
              <span :class="['result-badge', match.result?.toLowerCase()]">
                {{ match.result || '-' }}
              </span>
            </td>
            <td class="actions">
              <button @click="editMatch(match)" class="btn-edit">Редактировать</button>
              <button @click="deleteMatch(match.id)" class="btn-delete">Удалить</button>
            </td>
          </tr>
          <tr v-if="matches.length === 0">
            <td colspan="7" class="empty-state">Нет данных</td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-if="showCreateModal || editingMatch" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <h2>{{ editingMatch ? 'Редактировать результат матча' : 'Добавить результат матча' }}</h2>
        <form @submit.prevent="saveMatch">
          <div class="form-group">
            <label>ID команды *</label>
            <input v-model.number="formData.teamId" type="number" required />
          </div>
          <div class="form-group">
            <label>Дата матча *</label>
            <input v-model="formData.matchDate" type="datetime-local" required />
          </div>
          <div class="form-group">
            <label>Название оппонента</label>
            <input v-model="formData.opponentName" type="text" />
          </div>
          <div class="form-group">
            <label>Карта</label>
            <input v-model="formData.map" type="text" />
          </div>
          <div class="form-group">
            <label>Счет команды</label>
            <input v-model.number="formData.teamScore" type="number" min="0" />
          </div>
          <div class="form-group">
            <label>Счет оппонента</label>
            <input v-model.number="formData.opponentScore" type="number" min="0" />
          </div>
          <div class="form-group">
            <label>Результат</label>
            <select v-model="formData.result">
              <option value="WIN">WIN</option>
              <option value="LOSS">LOSS</option>
              <option value="DRAW">DRAW</option>
            </select>
          </div>
          <div class="form-group">
            <label>Турнир</label>
            <input v-model="formData.tournament" type="text" />
          </div>
          <div class="form-group">
            <label>Тип матча</label>
            <input v-model="formData.matchType" type="text" />
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
  name: 'MatchResultsManagement',
  data() {
    return {
      matches: [],
      showCreateModal: false,
      editingMatch: null,
      formData: {
        teamId: null,
        matchDate: '',
        opponentName: '',
        map: '',
        teamScore: 0,
        opponentScore: 0,
        result: 'WIN',
        tournament: '',
        matchType: '',
        description: ''
      }
    };
  },
  mounted() {
    this.loadMatches();
  },
  methods: {
    async loadMatches() {
      try {
        const token = localStorage.getItem('token');
        const response = await fetch('http://localhost:8080/api/match-results', {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        if (response.ok) {
          this.matches = await response.json();
        }
      } catch (error) {
        console.error('Error loading matches:', error);
      }
    },
    formatDateTime(dateTime) {
      if (!dateTime) return '-';
      return new Date(dateTime).toLocaleString('ru-RU');
    },
    editMatch(match) {
      this.editingMatch = match;
      const dt = match.matchDate ? new Date(match.matchDate).toISOString().slice(0, 16) : '';
      this.formData = {
        teamId: match.teamId || null,
        matchDate: dt,
        opponentName: match.opponentName || '',
        map: match.map || '',
        teamScore: match.teamScore || 0,
        opponentScore: match.opponentScore || 0,
        result: match.result || 'WIN',
        tournament: match.tournament || '',
        matchType: match.matchType || '',
        description: match.description || ''
      };
    },
    async saveMatch() {
      try {
        const token = localStorage.getItem('token');
        const url = this.editingMatch
          ? `http://localhost:8080/api/match-results/${this.editingMatch.id}`
          : 'http://localhost:8080/api/match-results';
        const method = this.editingMatch ? 'PUT' : 'POST';

        const data = {
          ...this.formData,
          matchDate: new Date(this.formData.matchDate).toISOString()
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
          this.loadMatches();
        } else {
          alert('Ошибка при сохранении');
        }
      } catch (error) {
        console.error('Error saving match:', error);
        alert('Ошибка при сохранении');
      }
    },
    async deleteMatch(id) {
      if (!confirm('Вы уверены, что хотите удалить этот результат матча?')) return;

      try {
        const token = localStorage.getItem('token');
        const response = await fetch(`http://localhost:8080/api/match-results/${id}`, {
          method: 'DELETE',
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });

        if (response.ok) {
          this.loadMatches();
        } else {
          alert('Ошибка при удалении');
        }
      } catch (error) {
        console.error('Error deleting match:', error);
        alert('Ошибка при удалении');
      }
    },
    closeModal() {
      this.showCreateModal = false;
      this.editingMatch = null;
      this.formData = {
        teamId: null,
        matchDate: '',
        opponentName: '',
        map: '',
        teamScore: 0,
        opponentScore: 0,
        result: 'WIN',
        tournament: '',
        matchType: '',
        description: ''
      };
    }
  }
};
</script>

<style scoped>
.result-badge {
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  font-size: 0.85rem;
  font-weight: 600;
}

.result-badge.win {
  background: rgba(76, 175, 80, 0.2);
  color: #4caf50;
}

.result-badge.loss {
  background: rgba(244, 67, 54, 0.2);
  color: #f44336;
}

.result-badge.draw {
  background: rgba(255, 193, 7, 0.2);
  color: #ffc107;
}

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

