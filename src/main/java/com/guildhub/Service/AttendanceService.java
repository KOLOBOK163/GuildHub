package com.guildhub.Service;

import com.guildhub.Dto.AttendanceDto;

import java.util.List;

public interface AttendanceService {
    AttendanceDto createAttendance(AttendanceDto dto);
    AttendanceDto updateAttendance(Long attendanceId, AttendanceDto dto);
    AttendanceDto getAttendanceById(Long attendanceId);
    List<AttendanceDto> getAllAttendances();
    List<AttendanceDto> getAttendancesByTrainingSessionId(Long trainingSessionId);
    List<AttendanceDto> getAttendancesByPlayerCardId(Long playerCardId);
    void deleteAttendance(Long attendanceId);
}


