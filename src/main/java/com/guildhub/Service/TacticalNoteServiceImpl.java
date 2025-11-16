package com.guildhub.Service;

import com.guildhub.Dto.TacticalNoteDto;
import com.guildhub.Entity.TacticalNote;
import com.guildhub.Entity.Team;
import com.guildhub.Entity.User;
import com.guildhub.Mapper.TacticalNoteMapper;
import com.guildhub.Reposirtory.TacticalNoteRepository;
import com.guildhub.Reposirtory.TeamRepository;
import com.guildhub.Reposirtory.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TacticalNoteServiceImpl implements TacticalNoteService {

    private final TacticalNoteRepository tacticalNoteRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TacticalNoteMapper tacticalNoteMapper;

    public TacticalNoteServiceImpl(
            TacticalNoteRepository tacticalNoteRepository,
            TeamRepository teamRepository,
            UserRepository userRepository,
            TacticalNoteMapper tacticalNoteMapper) {
        this.tacticalNoteRepository = tacticalNoteRepository;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.tacticalNoteMapper = tacticalNoteMapper;
    }

    @Override
    @Transactional
    public TacticalNoteDto createTacticalNote(TacticalNoteDto dto) {
        Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + dto.getTeamId()));

        TacticalNote note = tacticalNoteMapper.tacticalNoteDtoToTacticalNote(dto);
        note.setTeam(team);
        note.setCreatedAt(LocalDateTime.now());
        note.setUpdatedAt(LocalDateTime.now());

        // Set author from current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User author = userRepository.findByUsername(username)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
            note.setAuthor(author);
        } else if (dto.getAuthorId() != null) {
            User author = userRepository.findById(dto.getAuthorId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + dto.getAuthorId()));
            note.setAuthor(author);
        }

        return tacticalNoteMapper.tacticalNoteToTacticalNoteDto(tacticalNoteRepository.save(note));
    }

    @Override
    @Transactional
    public TacticalNoteDto updateTacticalNote(Long noteId, TacticalNoteDto dto) {
        TacticalNote note = tacticalNoteRepository.findById(noteId)
                .orElseThrow(() -> new EntityNotFoundException("Tactical note not found with id: " + noteId));

        if (dto.getTeamId() != null && !note.getTeam().getId().equals(dto.getTeamId())) {
            Team team = teamRepository.findById(dto.getTeamId())
                    .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + dto.getTeamId()));
            note.setTeam(team);
        }

        note.setTitle(dto.getTitle());
        note.setContent(dto.getContent());
        note.setMap(dto.getMap());
        note.setPosition(dto.getPosition());
        note.setPrivate(dto.isPrivate());
        note.setUpdatedAt(LocalDateTime.now());

        return tacticalNoteMapper.tacticalNoteToTacticalNoteDto(tacticalNoteRepository.save(note));
    }

    @Override
    public TacticalNoteDto getTacticalNoteById(Long noteId) {
        TacticalNote note = tacticalNoteRepository.findById(noteId)
                .orElseThrow(() -> new EntityNotFoundException("Tactical note not found with id: " + noteId));
        return tacticalNoteMapper.tacticalNoteToTacticalNoteDto(note);
    }

    @Override
    public List<TacticalNoteDto> getAllTacticalNotes() {
        return tacticalNoteRepository.findAll().stream()
                .map(tacticalNoteMapper::tacticalNoteToTacticalNoteDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TacticalNoteDto> getTacticalNotesByTeamId(Long teamId) {
        return tacticalNoteRepository.findByTeamId(teamId).stream()
                .map(tacticalNoteMapper::tacticalNoteToTacticalNoteDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TacticalNoteDto> getPublicTacticalNotesByTeamId(Long teamId) {
        return tacticalNoteRepository.findByTeamIdAndIsPrivateFalse(teamId).stream()
                .map(tacticalNoteMapper::tacticalNoteToTacticalNoteDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TacticalNoteDto> getTacticalNotesByMap(String map) {
        return tacticalNoteRepository.findByMap(map).stream()
                .map(tacticalNoteMapper::tacticalNoteToTacticalNoteDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteTacticalNote(Long noteId) {
        if (!tacticalNoteRepository.existsById(noteId)) {
            throw new EntityNotFoundException("Tactical note not found with id: " + noteId);
        }
        tacticalNoteRepository.deleteById(noteId);
    }
}


