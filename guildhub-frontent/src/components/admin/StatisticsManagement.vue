<template>
  <div class="management-container">
    <div class="header-actions">
      <button @click="showCreateModal = true" class="btn-primary">+ Добавить статистику</button>
    </div>

    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Команда ID</th>
            <th>Период</th>
            <th>Матчей</th>
            <th>Побед</th>
            <th>Поражений</th>
            <th>Win Rate</th>
            <th>Действия</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="stat in statistics" :key="stat.id">
            <td>{{ stat.id }}</td>
            <td>{{ stat.teamId }}</td>
            <td>{{ formatPeriod(stat.periodStart, stat.periodEnd) }}</td>
            <td>{{ stat.matchesPlayed || 0 }}</td>
            <td>{{ stat.matchesWon || 0 }}</td>
            <td>{{ stat.matchesLost || 0 }}</td>
            <td>{{ stat.winRate ? (stat.winRate * 100).toFixed(1) + '%' : '-' }}</td>
            <td class="actions">
              <button @click="editStat(stat)" class="btn-edit">Редактировать</button>
              <button @click="deleteStat(stat.id)" class="btn-delete">Удалить</button>
            </td>
          </tr>
          <tr v-if="statistics.length === 0">
            <td colspan="8" class="empty-state">Нет данных</td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-if="showCreateModal || editingStat" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <h2>{{ editingStat ? 'Редактировать статистику' : 'Добавить статистику' }}</h2>
        <form @submit.prevent="saveStat">
          <div class="form-group">
            <label>ID команды *</label>
            <input v-model.number="formData.teamId" type="number" required />
          </div>
          <div class="form-group">
            <label>Начало периода *</label>
            <input v-model="formData.periodStart" type="date" required />
          </div>
          <div class="form-group">
            <label>Конец периода *</label>
            <input v-model="formData.periodEnd" type="date" required />
          </div>
          <div class="form-group">
            <label>Сыграно матчей</label>
            <input v-model.number="formData.matchesPlayed" type="number" min="0" />
          </div>
          <div class="form-group">
            <label>Побед</label>
            <input v-model.number="formData.matchesWon" type="number" min="0" />
          </div>
          <div class="form-group">
            <label>Поражений</label>
            <input v-model.number="formData.matchesLost" type="number" min="0" />
          </div>
          <div class="form-group">
            <label>Средний рейтинг</label>
            <input v-model.number="formData.averageRating" type="number" step="0.01" />
          </div>
          <div class="form-group">
            <label>Самый играемый карта</label>
            <input v-model="formData.mostPlayedMap" type="text" />
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
  name: 'StatisticsManagement',
  data() {
    return {
      statistics: [],
      showCreateModal: false,
      editingStat: null,
      formData: {
        teamId: null,
        periodStart: '',
        periodEnd: '',
        matchesPlayed: 0,
        matchesWon: 0,
        matchesLost: 0,
        averageRating: null,
        mostPlayedMap: ''
      }
    };
  },
  mounted() {
    this.loadStatistics();
  },
  methods: {
    async loadStatistics() {
      try {
        const token = localStorage.getItem('token');
        const response = await fetch('http://localhost:8080/api/team-statistics', {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        if (response.ok) {
          this.statistics = await response.json();
        }
      } catch (error) {
        console.error('Error loading statistics:', error);
      }
    },
    formatPeriod(start, end) {
      if (!start || !end) return '-';
      const startDate = new Date(start).toLocaleDateString('ru-RU');
      const endDate = new Date(end).toLocaleDateString('ru-RU');
      return `${startDate} - ${endDate}`;
    },
    editStat(stat) {
      this.editingStat = stat;
      const start = stat.periodStart ? new Date(stat.periodStart).toISOString().split('T')[0] : '';
      const end = stat.periodEnd ? new Date(stat.periodEnd).toISOString().split('T')[0] : '';
      this.formData = {
        teamId: stat.teamId || null,
        periodStart: start,
        periodEnd: end,
        matchesPlayed: stat.matchesPlayed || 0,
        matchesWon: stat.matchesWon || 0,
        matchesLost: stat.matchesLost || 0,
        averageRating: stat.averageRating || null,
        mostPlayedMap: stat.mostPlayedMap || ''
      };
    },
    async saveStat() {
      try {
        const token = localStorage.getItem('token');
        const url = this.editingStat
          ? `http://localhost:8080/api/team-statistics/${this.editingStat.id}`
          : 'http://localhost:8080/api/team-statistics';
        const method = this.editingStat ? 'PUT' : 'POST';

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
          this.loadStatistics();
        } else {
          alert('Ошибка при сохранении');
        }
      } catch (error) {
        console.error('Error saving statistic:', error);
        alert('Ошибка при сохранении');
      }
    },
    async deleteStat(id) {
      if (!confirm('Вы уверены, что хотите удалить эту статистику?')) return;

      try {
        const token = localStorage.getItem('token');
        const response = await fetch(`http://localhost:8080/api/team-statistics/${id}`, {
          method: 'DELETE',
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });

        if (response.ok) {
          this.loadStatistics();
        } else {
          alert('Ошибка при удалении');
        }
      } catch (error) {
        console.error('Error deleting statistic:', error);
        alert('Ошибка при удалении');
      }
    },
    closeModal() {
      this.showCreateModal = false;
      this.editingStat = null;
      this.formData = {
        teamId: null,
        periodStart: '',
        periodEnd: '',
        matchesPlayed: 0,
        matchesWon: 0,
        matchesLost: 0,
        averageRating: null,
        mostPlayedMap: ''
      };
    }
  }
};
</script>

<style scoped>
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

