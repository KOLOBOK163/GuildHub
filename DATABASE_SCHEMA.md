1. [ ] # GuildHub Database Schema
2. [ ] 
3. [ ] ## Entity Relationships Diagram
4. [ ] 
5. [ ] ```
6. [ ] ┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
7. [ ] │      USERS      │    │   PLAYER_CARDS  │    │      TEAMS      │
8. [ ] ├─────────────────┤    ├─────────────────┤    ├─────────────────┤
9. [ ] │ id (PK)         │    │ id (PK)         │    │ id (PK)         │
10. [ ] │ username        │    │ nickname        │    │ name            │
11. [ ] │ email           │    │ full_name       │    │ logo_url        │
12. [ ] │ password        │    │ steam_id        │    │ prizes          │
13. [ ] │ avatar_url      │    │ faceit_url      │    │ hltv_rating     │
14. [ ] │ created_at      │    │ prizes          │    └─────────────────┘
15. [ ] │ updated_at      │    │ age             │              │
16. [ ] │ player_card_id  │    │ country         │              │
17. [ ] │ role            │    │ rating          │              │
18. [ ] └─────────────────┘    │ kd_ratio        │              │
19. [ ]          │              │ maps_played     │              │
20. [ ]          │              │ team_id (FK)    │              │
21. [ ]          │              └─────────────────┘              │
22. [ ]          │                       │                        │
23. [ ]          └───────────────────────┼────────────────────────┘
24. [ ]                                  │
25. [ ]                     ┌────────────┼────────────┐
26. [ ]                     │            │            │
27. [ ]          ┌─────────────────┐    │    ┌─────────────────┐
28. [ ]          │   ATTENDANCE   │    │    │ TRAINING_SESSIONS│
29. [ ]          ├─────────────────┤    │    ├─────────────────┤
30. [ ]          │ id (PK)         │    │    │ id (PK)         │
31. [ ]          │ training_session│    │    │ date_time       │
32. [ ]          │ _id (FK)        │    │    │ description     │
33. [ ]          │ player_card_id  │    │    │ type            │
34. [ ]          │ (FK)            │    │    │ team_id (FK)    │
35. [ ]          │ attended        │    │    │ opponent        │
36. [ ]          │ reason          │    │    │ server_info     │
37. [ ]          │ performance     │    │    └─────────────────┘
38. [ ]          │ notes           │    │
39. [ ]          └─────────────────┘    │
40. [ ]                                 │
41. [ ]                     ┌───────────┼───────────┐
42. [ ]                     │           │           │
43. [ ]          ┌─────────────────┐    │    ┌─────────────────┐
44. [ ]          │  MATCH_RESULTS  │    │    │   TACTICAL_NOTES│
45. [ ]          ├─────────────────┤    │    ├─────────────────┤
46. [ ]          │ id (PK)         │    │    │ id (PK)         │
47. [ ]          │ team_id (FK)    │    │    │ team_id (FK)    │
48. [ ]          │ opponent_team_id│    │    │ title           │
49. [ ]          │ opponent_name   │    │    │ content         │
50. [ ]          │ match_date      │    │    │ map             │
51. [ ]          │ map             │    │    │ position        │
52. [ ]          │ team_score      │    │    │ created_at      │
53. [ ]          │ opponent_score  │    │    │ updated_at      │
54. [ ]          │ tournament      │    │    │ author          │
55. [ ]          │ match_type      │    │    │ is_private      │
56. [ ]          │ result          │    │    └─────────────────┘
57. [ ]          │ description     │    │
58. [ ]          └─────────────────┘    │
59. [ ]                                 │
60. [ ]                     ┌───────────┼───────────┐
61. [ ]                     │           │           │
62. [ ]          ┌─────────────────┐    │    ┌─────────────────┐
63. [ ]          │ TEAM_STATISTICS │    │    │     VIDEOS      │
64. [ ]          ├─────────────────┤    │    ├─────────────────┤
65. [ ]          │ id (PK)         │    │    │ id (PK)         │
66. [ ]          │ team_id (FK)    │    │    │ title           │
67. [ ]          │ period_start    │    │    │ description     │
68. [ ]          │ period_end      │    │    │ created_at      │
69. [ ]          │ matches_played  │    │    │ type            │
70. [ ]          │ matches_won     │    │    │ youtube_url     │
71. [ ]          │ matches_lost    │    │    │ map             │
72. [ ]          │ win_rate        │    │    │ position        │
73. [ ]          │ average_rating  │    │    │ team_id (FK)    │
74. [ ]          │ total_rounds    │    │    │ views           │
75. [ ]          │ rounds_won      │    │    │ likes           │
76. [ ]          │ rounds_lost     │    │    └─────────────────┘
77. [ ]          │ round_win_rate  │    │
78. [ ]          │ most_played_map │    │
79. [ ]          │ best_map        │    │
80. [ ]          │ worst_map       │    │
81. [ ]          │ average_kd      │    │
82. [ ]          │ total_kills     │    │
83. [ ]          │ total_deaths    │    │
84. [ ]          │ total_assists   │    │
85. [ ]          └─────────────────┘    │
86. [ ]                                 │
87. [ ]                                 └─────────────────────────────┘
88. [ ] ```
89. [ ] 
90. [ ] ## Key Relationships:
91. [ ] 
92. [ ] 1. **USERS** → **PLAYER_CARDS** (One-to-One)
93. [ ] 2. **PLAYER_CARDS** → **TEAMS** (Many-to-One)
94. [ ] 3. **TEAMS** → **TRAINING_SESSIONS** (One-to-Many)
95. [ ] 4. **TEAMS** → **MATCH_RESULTS** (One-to-Many)
96. [ ] 5. **TEAMS** → **TACTICAL_NOTES** (One-to-Many)
97. [ ] 6. **TEAMS** → **TEAM_STATISTICS** (One-to-Many)
98. [ ] 7. **TEAMS** → **VIDEOS** (One-to-Many)
99. [ ] 8. **TRAINING_SESSIONS** → **ATTENDANCE** (One-to-Many)
100. [ ] 9. **PLAYER_CARDS** → **ATTENDANCE** (One-to-Many)
101. [ ] 
102. [ ] ## Enums:
103. [ ] 
104. [ ] - **UserRole**: FAN, PLAYER, TEAM_LEADER, COACH, CONTENT_CREATOR
105. [ ] - **SessionType**: TRAINING, SCRIM, OFFICIAL_MATCH
106. [ ] - **VideoType**: HIGHLIGHT, TUTORIAL, TACTICAL, TEAM_PRIVATE
107. [ ] 