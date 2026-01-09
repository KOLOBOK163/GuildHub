<template>
  <div class="management-container">
    <div class="header-actions">
      <button @click="showCreateModal = true" class="btn-primary">+ Добавить видео</button>
    </div>

    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Название</th>
            <th>Тип</th>
            <th>Карта</th>
            <th>Просмотры</th>
            <th>Лайки</th>
            <th>Действия</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="video in videos" :key="video.id">
            <td>{{ video.id }}</td>
            <td>{{ video.title }}</td>
            <td>{{ video.type }}</td>
            <td>{{ video.map || '-' }}</td>
            <td>{{ video.views || 0 }}</td>
            <td>{{ video.likes || 0 }}</td>
            <td class="actions">
              <button @click="editVideo(video)" class="btn-edit">Редактировать</button>
              <button @click="deleteVideo(video.id)" class="btn-delete">Удалить</button>
            </td>
          </tr>
          <tr v-if="videos.length === 0">
            <td colspan="7" class="empty-state">Нет данных</td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-if="showCreateModal || editingVideo" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <h2>{{ editingVideo ? 'Редактировать видео' : 'Добавить видео' }}</h2>
        <form @submit.prevent="saveVideo">
          <div class="form-group">
            <label>Название *</label>
            <input v-model="formData.title" type="text" required />
          </div>
          <div class="form-group">
            <label>Описание</label>
            <textarea v-model="formData.description" rows="3"></textarea>
          </div>
          <div class="form-group">
            <label>Тип *</label>
            <select v-model="formData.type" required>
              <option value="HIGHLIGHT">Highlight</option>
              <option value="TUTORIAL">Tutorial</option>
              <option value="TACTICAL">Tactical</option>
              <option value="TEAM_PRIVATE">Team Private</option>
            </select>
          </div>
          <div class="form-group">
            <label>YouTube URL</label>
            <input v-model="formData.youtubeUrl" type="url" />
          </div>
          <div class="form-group">
            <label>Карта</label>
            <input v-model="formData.map" type="text" />
          </div>
          <div class="form-group">
            <label>Позиция</label>
            <input v-model="formData.position" type="text" />
          </div>
          <div class="form-group">
            <label>ID команды</label>
            <input v-model.number="formData.teamId" type="number" />
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
  name: 'VideosManagement',
  data() {
    return {
      videos: [],
      showCreateModal: false,
      editingVideo: null,
      formData: {
        title: '',
        description: '',
        type: 'HIGHLIGHT',
        youtubeUrl: '',
        map: '',
        position: '',
        teamId: null
      }
    };
  },
  mounted() {
    this.loadVideos();
  },
  methods: {
    async loadVideos() {
      try {
        const token = localStorage.getItem('token');
        const response = await fetch('http://localhost:8080/api/videos', {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        if (response.ok) {
          this.videos = await response.json();
        }
      } catch (error) {
        console.error('Error loading videos:', error);
      }
    },
    editVideo(video) {
      this.editingVideo = video;
      this.formData = {
        title: video.title || '',
        description: video.description || '',
        type: video.type || 'HIGHLIGHT',
        youtubeUrl: video.youtubeUrl || '',
        map: video.map || '',
        position: video.position || '',
        teamId: video.teamId || null
      };
    },
    async saveVideo() {
      try {
        const token = localStorage.getItem('token');
        const url = this.editingVideo
          ? `http://localhost:8080/api/videos/${this.editingVideo.id}`
          : 'http://localhost:8080/api/videos';
        const method = this.editingVideo ? 'PUT' : 'POST';

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
          this.loadVideos();
        } else {
          alert('Ошибка при сохранении');
        }
      } catch (error) {
        console.error('Error saving video:', error);
        alert('Ошибка при сохранении');
      }
    },
    async deleteVideo(id) {
      if (!confirm('Вы уверены, что хотите удалить это видео?')) return;

      try {
        const token = localStorage.getItem('token');
        const response = await fetch(`http://localhost:8080/api/videos/${id}`, {
          method: 'DELETE',
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });

        if (response.ok) {
          this.loadVideos();
        } else {
          alert('Ошибка при удалении');
        }
      } catch (error) {
        console.error('Error deleting video:', error);
        alert('Ошибка при удалении');
      }
    },
    closeModal() {
      this.showCreateModal = false;
      this.editingVideo = null;
      this.formData = {
        title: '',
        description: '',
        type: 'HIGHLIGHT',
        youtubeUrl: '',
        map: '',
        position: '',
        teamId: null
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

