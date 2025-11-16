package com.guildhub.Service;

import com.guildhub.Dto.PlayerCardDto;
import com.guildhub.Entity.PlayerCard;

import java.util.List;

public interface PlayerCardService {
    PlayerCardDto createPlayerCard(PlayerCardDto playerCardDto);
    PlayerCardDto updatePlayerCard(Long playerCardId, PlayerCardDto playerCardDto);
    PlayerCardDto getPlayerCardById(Long playerCardId);
    List<PlayerCardDto> getAllPlayerCards();
    List<PlayerCardDto> getPlayerCardsByTeamId(Long teamId);
    void deletePlayerCard(Long playerCardId);
}
