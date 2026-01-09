<template>
  <div class="management-container">
    <div class="header-actions">
      <button @click="showCreateModal = true" class="btn-primary">+ Создать пользователя</button>
    </div>

    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Username</th>
            <th>Email</th>
            <th>Роли</th>
            <th>Действия</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users" :key="user.id">
            <td>{{ user.id }}</td>
            <td>{{ user.username }}</td>
            <td>{{ user.email }}</td>
            <td>
              <span v-if="user.roles && user.roles.length > 0">
                <span v-for="role in user.roles" :key="role" class="role-badge">{{ role }}</span>
              </span>
              <span v-else class="no-roles">-</span>
            </td>
            <td class="actions">
              <button @click="editUser(user)" class="btn-edit">Редактировать</button>
              <button @click="deleteUser(user.id)" class="btn-delete">Удалить</button>
            </td>
          </tr>
          <tr v-if="users.length === 0">
            <td colspan="5" class="empty-state">Нет данных</td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-if="showCreateModal || editingUser" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <h2>{{ editingUser ? 'Редактировать пользователя' : 'Создать пользователя' }}</h2>
        <form @submit.prevent="saveUser">
          <div class="form-group">
            <label>Username *</label>
            <input v-model="formData.username" type="text" required />
          </div>
          <div class="form-group">
            <label>Email *</label>
            <input v-model="formData.email" type="email" required />
          </div>
          <div v-if="!editingUser" class="form-group">
            <label>Password *</label>
            <input v-model="formData.password" type="password" required />
          </div>
          <div class="form-group">
            <label>Роли</label>
            <div class="roles-checkboxes">
              <label v-for="role in availableRoles" :key="role" class="checkbox-label">
                <input
                  type="checkbox"
                  :value="role"
                  v-model="formData.roles"
                />
                <span>{{ role }}</span>
              </label>
            </div>
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
  name: 'UsersManagement',
  data() {
    return {
      users: [],
      showCreateModal: false,
      editingUser: null,
      availableRoles: ['FAN', 'PLAYER', 'TEAM_LEADER', 'COACH', 'CONTENT_CREATOR', 'ADMIN'],
      formData: {
        username: '',
        email: '',
        password: '',
        roles: []
      }
    };
  },
  mounted() {
    this.loadUsers();
  },
  methods: {
    async loadUsers() {
      try {
        const token = localStorage.getItem('token');
        const response = await fetch('http://localhost:8080/api/users', {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        if (response.ok) {
          this.users = await response.json();
        }
      } catch (error) {
        console.error('Error loading users:', error);
      }
    },
    async editUser(user) {
      try {
        const token = localStorage.getItem('token');
        let userId = user.id;
        if (!userId) {
          const allUsersResponse = await fetch('http://localhost:8080/api/users', {
            headers: {
              'Authorization': `Bearer ${token}`
            }
          });
          if (allUsersResponse.ok) {
            const allUsers = await allUsersResponse.json();
            const foundUser = allUsers.find(u => u.username === user.username);
            if (foundUser && foundUser.id) {
              userId = foundUser.id;
            }
          }
        }
        
        if (userId) {
          const response = await fetch(`http://localhost:8080/api/users/${userId}`, {
            headers: {
              'Authorization': `Bearer ${token}`
            }
          });
          if (response.ok) {
            const fullUser = await response.json();
            this.editingUser = fullUser;
            this.formData = {
              username: fullUser.username || '',
              email: fullUser.email || '',
              password: '',
              roles: fullUser.roles || []
            };
            return;
          }
        }
      } catch (error) {
        console.error('Error loading user details:', error);
      }

      this.editingUser = user;
      this.formData = {
        username: user.username || '',
        email: user.email || '',
        password: '',
        roles: user.roles || []
      };
    },
    async saveUser() {
      try {
        const token = localStorage.getItem('token');
        
        if (this.editingUser) {
          const response = await fetch(`http://localhost:8080/api/users/${this.editingUser.id}`, {
            method: 'PUT',
            headers: {
              'Authorization': `Bearer ${token}`,
              'Content-Type': 'application/json'
            },
            body: JSON.stringify({
              username: this.formData.username,
              email: this.formData.email,
              avatarUrl: this.editingUser.avatarUrl
            })
          });

          if (response.ok) {
            this.closeModal();
            this.loadUsers();
          } else {
            alert('Ошибка при сохранении');
          }
        } else {
          const response = await fetch('http://localhost:8080/api/users/admin', {
            method: 'POST',
            headers: {
              'Authorization': `Bearer ${token}`,
              'Content-Type': 'application/json'
            },
            body: JSON.stringify({
              username: this.formData.username,
              email: this.formData.email,
              password: this.formData.password,
              roles: this.formData.roles.length > 0 ? this.formData.roles : ['FAN']
            })
          });

          if (response.ok) {
            this.closeModal();
            this.loadUsers();
          } else {
            const error = await response.json();
            alert(error.message || 'Ошибка при создании пользователя');
          }
        }
      } catch (error) {
        console.error('Error saving user:', error);
        alert('Ошибка при сохранении');
      }
    },
    async deleteUser(id) {
      if (!confirm('Вы уверены, что хотите удалить этого пользователя?')) return;

      try {
        const token = localStorage.getItem('token');
        const response = await fetch(`http://localhost:8080/api/users/${id}`, {
          method: 'DELETE',
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });

        if (response.ok) {
          this.loadUsers();
        } else {
          alert('Ошибка при удалении');
        }
      } catch (error) {
        console.error('Error deleting user:', error);
        alert('Ошибка при удалении');
      }
    },
    closeModal() {
      this.showCreateModal = false;
      this.editingUser = null;
      this.formData = {
        username: '',
        email: '',
        password: '',
        roles: []
      };
    }
  }
};
</script>

<style scoped>
.role-badge {
  display: inline-block;
  padding: 0.25rem 0.5rem;
  margin: 0.25rem;
  background: rgba(102, 217, 255, 0.2);
  color: #66d9ff;
  border-radius: 4px;
  font-size: 0.85rem;
}

.roles-checkboxes {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
}

.checkbox-label {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #ccc;
  cursor: pointer;
}

.checkbox-label input[type="checkbox"] {
  width: auto;
  cursor: pointer;
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

