# GuildHub Database Schema

## Entity Relationships Diagram

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│      USERS      │    │   PLAYER_CARDS  │    │      TEAMS      │
├─────────────────┤    ├─────────────────┤    ├─────────────────┤
│ id (PK)         │    │ id (PK)         │    │ id (PK)         │
│ username        │    │ nickname        │    │ name            │
│ email           │    │ full_name       │    │ logo_url        │
│ password        │    │ steam_id        │    │ prizes          │
│ avatar_url      │    │ faceit_url      │    │ hltv_rating     │
│ created_at      │    │ prizes          │    └─────────────────┘
│ updated_at      │    │ age             │              │
│ player_card_id  │    │ country         │              │
│ role            │    │ rating          │              │
└─────────────────┘    │ kd_ratio        │              │
         │              │ maps_played     │              │
         │              │ team_id (FK)    │              │
         │              └─────────────────┘              │
         │                       │                        │
         └───────────────────────┼────────────────────────┘
                                 │
                    ┌────────────┼────────────┐
                    │            │            │
         ┌─────────────────┐    │    ┌─────────────────┐
         │   ATTENDANCE   │    │    │ TRAINING_SESSIONS│
         ├─────────────────┤    │    ├─────────────────┤
         │ id (PK)         │    │    │ id (PK)         │
         │ training_session│    │    │ date_time       │
         │ _id (FK)        │    │    │ description     │
         │ player_card_id  │    │    │ type            │
         │ (FK)            │    │    │ team_id (FK)    │
         │ attended        │    │    │ opponent        │
         │ reason          │    │    │ server_info     │
         │ performance     │    │    └─────────────────┘
         │ notes           │    │
         └─────────────────┘    │
                                │
                    ┌───────────┼───────────┐
                    │           │           │
         ┌─────────────────┐    │    ┌─────────────────┐
         │  MATCH_RESULTS  │    │    │   TACTICAL_NOTES│
         ├─────────────────┤    │    ├─────────────────┤
         │ id (PK)         │    │    │ id (PK)         │
         │ team_id (FK)    │    │    │ team_id (FK)    │
         │ opponent_team_id│    │    │ title           │
         │ opponent_name   │    │    │ content         │
         │ match_date      │    │    │ map             │
         │ map             │    │    │ position        │
         │ team_score      │    │    │ created_at      │
         │ opponent_score  │    │    │ updated_at      │
         │ tournament      │    │    │ author          │
         │ match_type      │    │    │ is_private      │
         │ result          │    │    └─────────────────┘
         │ description     │    │
         └─────────────────┘    │
                                │
                    ┌───────────┼───────────┐
                    │           │           │
         ┌─────────────────┐    │    ┌─────────────────┐
         │ TEAM_STATISTICS │    │    │     VIDEOS      │
         ├─────────────────┤    │    ├─────────────────┤
         │ id (PK)         │    │    │ id (PK)         │
         │ team_id (FK)    │    │    │ title           │
         │ period_start    │    │    │ description     │
         │ period_end      │    │    │ created_at      │
         │ matches_played  │    │    │ type            │
         │ matches_won     │    │    │ youtube_url     │
         │ matches_lost    │    │    │ map             │
         │ win_rate        │    │    │ position        │
         │ average_rating  │    │    │ team_id (FK)    │
         │ total_rounds    │    │    │ views           │
         │ rounds_won      │    │    │ likes           │
         │ rounds_lost     │    │    └─────────────────┘
         │ round_win_rate  │    │
         │ most_played_map │    │
         │ best_map        │    │
         │ worst_map       │    │
         │ average_kd      │    │
         │ total_kills     │    │
         │ total_deaths    │    │
         │ total_assists   │    │
         └─────────────────┘    │
                                │
                                └─────────────────────────────┘
```

## Key Relationships:

1. **USERS** → **PLAYER_CARDS** (One-to-One)
2. **PLAYER_CARDS** → **TEAMS** (Many-to-One)
3. **TEAMS** → **TRAINING_SESSIONS** (One-to-Many)
4. **TEAMS** → **MATCH_RESULTS** (One-to-Many)
5. **TEAMS** → **TACTICAL_NOTES** (One-to-Many)
6. **TEAMS** → **TEAM_STATISTICS** (One-to-Many)
7. **TEAMS** → **VIDEOS** (One-to-Many)
8. **TRAINING_SESSIONS** → **ATTENDANCE** (One-to-Many)
9. **PLAYER_CARDS** → **ATTENDANCE** (One-to-Many)

## Enums:

- **UserRole**: FAN, PLAYER, TEAM_LEADER, COACH, CONTENT_CREATOR
- **SessionType**: TRAINING, SCRIM, OFFICIAL_MATCH
- **VideoType**: HIGHLIGHT, TUTORIAL, TACTICAL, TEAM_PRIVATE
