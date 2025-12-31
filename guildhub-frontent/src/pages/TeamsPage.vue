<template>
  <div class="teams-page">
    <div class="teams-list">
      <div v-for="team in source" :key="team.id" class="team-row">
        <div class="team-rank">#{{ team.rank }}</div>
        <div class="team-logo">
          <img :src="team.logo" :alt="team.name + ' logo'" />
        </div>
        <div class="team-info">
          <div class="team-name">{{ team.name }} ({{ team.points }} HLTV points)</div>
          <div class="team-players">
            {{ team.players.join(' • ') }}
          </div>
        </div>
        <div class="team-change" :class="{ 'positive': team.change > 0, 'negative': team.change < 0 }">
          {{ team.change !== 0 ? (team.change > 0 ? '+' : '') + team.change : '-' }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "TeamsPage",
  data() {
    return {
      source: [
        {
          id: 1,
          rank: 1,
          name: "Vitality",
          logo: "/team1.png",
          points: 954,
          players: ["apEX", "ropz", "ZywOo", "flameZ", "mezii"],
          change: 0
        },
        {
          id: 2,
          rank: 2,
          name: "FURIA",
          logo: "/team2.png",
          points: 808,
          players: ["Fallen", "yuurih", "YEKINDAR", "KSCERATO", "molody"],
          change: -1
        },
        {
          id: 3,
          rank: 3,
          name: "Falcons",
          logo: "/team3.png",
          points: 586,
          players: ["NiKo", "TeSeS", "m0NESY", "kyxsan", "kyousuke"],
          change: 0
        },
        {
          id: 4,
          rank: 4,
          name: "MOUZ",
          logo: "/team4.png",
          points: 478,
          players: ["Brollan", "torzsi", "Spinx", "JimpPhat", "xertioN"],
          change: +1
        },
        {
          id: 5,
          rank: 5,
          name: "FaZe",
          logo: "/team5.png",
          points: 380,
          players: ["karrigan", "frozen", "Twistzz", "broky", "jcobbb"],
          change: +1
        },
        {
          id: 6,
          rank: 6,
          name: "The MongolZ",
          logo: "/team6.png",
          points: 354,
          players: ["bLitz", "Techno", "mzinho", "910"],
          change: +1
        },
        {
          id: 7,
          rank: 7,
          name: "Natus Vincere",
          logo: "/team7.png",
          points: 346,
          players: ["Aleksib", "iM", "b1t", "wonderful", "makazze"],
          change: 0
        },
        {
          id: 8,
          rank: 8,
          name: "Spirit",
          logo: "/team8.png",
          points: 328,
          players: ["sh1ro", "magixx", "tNTR", "zont1x", "donk"],
          change: -3
        }
      ]
    };
  }
};
</script>

<style scoped>
.teams-page {
  padding: 2rem;
  background-color: #0f0f0f;
  color: white;
}

.teams-list {
  max-width: 1000px;
  margin: 0 auto;
}

.team-row {
  display: flex;
  align-items: center;
  padding: 1rem;
  background: rgba(30, 30, 30, 0.7);
  border-radius: 8px;
  margin-bottom: 0.5rem;
  transition: background 0.2s ease;
}

.team-row:hover {
  background: rgba(50, 50, 50, 0.7);
}

.team-rank {
  font-weight: 700;
  color: #66d9ff;
  min-width: 30px;
  text-align: center;
}

.team-logo img {
  width: 40px;
  height: 40px;
  object-fit: contain;
  margin-right: 1rem;
}

.team-info {
  flex: 1;
  min-width: 200px;
}

.team-name {
  font-size: 1.1rem;
  font-weight: 600;
  color: #fff;
  margin-bottom: 0.25rem;
}

.team-players {
  font-size: 0.9rem;
  color: #aaa;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.team-change {
  font-weight: 600;
  min-width: 30px;
  text-align: center;
  font-size: 0.95rem;
}

.team-change.positive {
  color: #4ecdc4;
}

.team-change.negative {
  color: #ff6b6b;
}
</style>