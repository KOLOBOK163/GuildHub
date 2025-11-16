package com.guildhub.Mapper;

import com.guildhub.Dto.VideoDto;
import com.guildhub.Entity.Video;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VideoMapper {
    @Mapping(target = "team", ignore = true)
    Video videoDtoToVideo(VideoDto dto);
    
    @Mapping(source = "team.id", target = "teamId")
    VideoDto videoToVideoDto(Video video);
}


