package com.guildhub.Mapper;

import com.guildhub.Dto.AttendanceDto;
import com.guildhub.Entity.Attendance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {
    @Mapping(target = "trainingSession", ignore = true)
    @Mapping(target = "playerCard", ignore = true)
    Attendance attendanceDtoToAttendance(AttendanceDto dto);
    
    @Mapping(source = "trainingSession.id", target = "trainingSessionId")
    @Mapping(source = "playerCard.id", target = "playerCardId")
    AttendanceDto attendanceToAttendanceDto(Attendance attendance);
}


