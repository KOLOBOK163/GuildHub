package com.guildhub.Service;

import com.guildhub.Dto.PlayerCardDto;
import com.guildhub.Entity.PlayerCard;
import com.guildhub.Mapper.PlayerCardMapper;
import com.guildhub.Reposirtory.PlayerCardRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerCardServiceImpl implements PlayerCardService {

    private final PlayerCardRepository playerCardRepository;
    private final PlayerCardMapper playerCardMapper;

    public PlayerCardServiceImpl(PlayerCardRepository playerCardRepository, PlayerCardMapper playerCardMapper) {
        this.playerCardRepository = playerCardRepository;
        this.playerCardMapper = playerCardMapper;
    }

    @Override
    @Transactional
    public PlayerCardDto createPlayerCard(PlayerCardDto playerCardDto) {
        if(playerCardRepository.existsByNickname(playerCardDto.getNickname())) {
            throw new EntityExistsException("PlayerCard with nickname " + playerCardDto.getNickname() + " already exists");
        }

        PlayerCard playerCard = playerCardMapper.PlayerCardDtoToPlayerCard(playerCardDto);
        playerCard.setPrizes(playerCardDto.getPrizes());
        playerCard.setAge(playerCardDto.getAge());

        return playerCardMapper.PlayerCardToPlayerCardDto(playerCardRepository.save(playerCard));
    }

    @Override
    @Transactional
    public PlayerCardDto updatePlayerCard(Long playerCardId, PlayerCardDto playerCardDto) {
        PlayerCard playerCard = playerCardRepository.findById(playerCardId)
                .orElseThrow(() -> new EntityNotFoundException("PlayerCard not found with id: " + playerCardId));

        if (!playerCard.getNickname().equals(playerCardDto.getNickname()) &&
                playerCardRepository.existsByNickname(playerCardDto.getNickname())) {
            throw new EntityExistsException("Nickname already taken");
        }

        playerCard.setNickname(playerCardDto.getNickname());
        playerCard.setFullName(playerCardDto.getFullName());
        playerCard.setSteamId(playerCardDto.getSteamId());
        playerCard.setFaceitUrl(playerCardDto.getFaceitUrl());
        playerCard.setPrizes(playerCardDto.getPrizes());
        playerCard.setAge(playerCardDto.getAge());
        playerCard.setCountry(playerCardDto.getCountry());

        return playerCardMapper.PlayerCardToPlayerCardDto(playerCardRepository.save(playerCard));
    }

    @Override
    public PlayerCardDto getPlayerCardById(Long playerCardId) {
        PlayerCard playerCard = playerCardRepository.findById(playerCardId)
                .orElseThrow(() -> new EntityNotFoundException("PlayerCard not found with id: " + playerCardId));
        return playerCardMapper.PlayerCardToPlayerCardDto(playerCard);
    }

    @Override
    public List<PlayerCardDto> getAllPlayerCards() {
        return playerCardRepository.findAll().stream()
                .map(playerCardMapper::PlayerCardToPlayerCardDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlayerCardDto> getPlayerCardsByTeamId(Long teamId) {
        return playerCardRepository.findByTeamId(teamId).stream()
                .map(playerCardMapper::PlayerCardToPlayerCardDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletePlayerCard(Long playerCardId) {
        if (!playerCardRepository.existsById(playerCardId)) {
            throw new EntityNotFoundException("PlayerCard not found with id: " + playerCardId);
        }
        playerCardRepository.deleteById(playerCardId);
    }
}
