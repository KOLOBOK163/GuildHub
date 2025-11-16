package com.guildhub.Controller;

import com.guildhub.Dto.AttendanceDto;
import com.guildhub.Service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendances")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping
    public ResponseEntity<AttendanceDto> createAttendance(@Valid @RequestBody AttendanceDto dto) {
        return ResponseEntity.ok(attendanceService.createAttendance(dto));
    }

    @PutMapping("/{attendanceId}")
    public ResponseEntity<AttendanceDto> updateAttendance(
            @PathVariable Long attendanceId,
            @Valid @RequestBody AttendanceDto dto) {
        return ResponseEntity.ok(attendanceService.updateAttendance(attendanceId, dto));
    }

    @GetMapping("/{attendanceId}")
    public ResponseEntity<AttendanceDto> getAttendanceById(@PathVariable Long attendanceId) {
        return ResponseEntity.ok(attendanceService.getAttendanceById(attendanceId));
    }

    @GetMapping
    public ResponseEntity<List<AttendanceDto>> getAllAttendances() {
        return ResponseEntity.ok(attendanceService.getAllAttendances());
    }

    @GetMapping("/training-session/{trainingSessionId}")
    public ResponseEntity<List<AttendanceDto>> getAttendancesByTrainingSessionId(
            @PathVariable Long trainingSessionId) {
        return ResponseEntity.ok(attendanceService.getAttendancesByTrainingSessionId(trainingSessionId));
    }

    @GetMapping("/player/{playerCardId}")
    public ResponseEntity<List<AttendanceDto>> getAttendancesByPlayerCardId(
            @PathVariable Long playerCardId) {
        return ResponseEntity.ok(attendanceService.getAttendancesByPlayerCardId(playerCardId));
    }

    @DeleteMapping("/{attendanceId}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable Long attendanceId) {
        attendanceService.deleteAttendance(attendanceId);
        return ResponseEntity.noContent().build();
    }
}


