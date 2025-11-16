package com.guildhub.Dto;

import com.guildhub.Validation.FaceitUrl;
import com.guildhub.Validation.SteamId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerCardDto {

    private String nickname;

    private String fullName;

    @SteamId
    private String steamId;

    @FaceitUrl
    private String faceitUrl;

    private int prizes;

    private int age;

    private String country;
}
