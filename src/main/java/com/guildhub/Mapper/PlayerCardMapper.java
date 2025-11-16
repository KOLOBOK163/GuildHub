package com.guildhub.Mapper;

import com.guildhub.Dto.PlayerCardDto;
import com.guildhub.Entity.PlayerCard;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlayerCardMapper {

    public PlayerCard PlayerCardDtoToPlayerCard(PlayerCardDto playerCardDto);

    public PlayerCardDto PlayerCardToPlayerCardDto(PlayerCard playerCard);
}
