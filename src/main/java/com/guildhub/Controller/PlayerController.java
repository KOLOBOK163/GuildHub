package com.guildhub.Controller;

import com.guildhub.Dto.PlayerCardDto;
import com.guildhub.Service.PlayerCardService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerCardService playerCardService;

    public PlayerController(PlayerCardService playerCardService) {
        this.playerCardService = playerCardService;
    }

    @PostMapping
    public ResponseEntity<PlayerCardDto> createPlayerCard(@Valid @RequestBody PlayerCardDto playerCardDto) {
        return ResponseEntity.ok(playerCardService.createPlayerCard(playerCardDto));
    }

    @PutMapping("/{playerId}")
    public ResponseEntity<PlayerCardDto> updatePlayerCard(
            @PathVariable Long playerId,
            @Valid @RequestBody PlayerCardDto playerCardDto) {
        return ResponseEntity.ok(playerCardService.updatePlayerCard(playerId, playerCardDto));
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<PlayerCardDto> getPlayerCardById(@PathVariable Long playerId) {
        return ResponseEntity.ok(playerCardService.getPlayerCardById(playerId));
    }

    @GetMapping
    public ResponseEntity<List<PlayerCardDto>> getAllPlayerCards() {
        return ResponseEntity.ok(playerCardService.getAllPlayerCards());
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<PlayerCardDto>> getPlayerCardsByTeamId(@PathVariable Long teamId) {
        return ResponseEntity.ok(playerCardService.getPlayerCardsByTeamId(teamId));
    }

    @DeleteMapping("/{playerId}")
    public ResponseEntity<Void> deletePlayerCard(@PathVariable Long playerId) {
        playerCardService.deletePlayerCard(playerId);
        return ResponseEntity.noContent().build();
    }
}
