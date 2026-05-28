package com.neup.web.service;

import com.neup.web.dto.DietaDTO;
import com.neup.web.dto.RecetaDTO;
import com.neup.web.repository.PersonaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuardadosService {

    private final PersonaRepository personaRepository;
    private final RecetaService recetaService;
    private final DietaService dietaService;

    public GuardadosService(PersonaRepository personaRepository,
                            RecetaService recetaService,
                            DietaService dietaService) {
        this.personaRepository = personaRepository;
        this.recetaService     = recetaService;
        this.dietaService      = dietaService;
    }

    // ── Recetas ───────────────────────────────────────────────────────────────

    public boolean isRecetaGuardada(String personaId, String recetaId) {
        return personaRepository.getRecetasGuardadas(personaId).contains(recetaId);
    }

    public void guardarReceta(String personaId, String recetaId) {
        personaRepository.addRecetaGuardada(personaId, recetaId);
    }

    public void desguardarReceta(String personaId, String recetaId) {
        personaRepository.removeRecetaGuardada(personaId, recetaId);
    }

    public List<RecetaDTO.RecetaResponse> getRecetasGuardadas(String personaId) {
        return personaRepository.getRecetasGuardadas(personaId).stream()
                .map(id -> recetaService.obtenerPorId(id).orElse(null))
                .filter(r -> r != null)
                .toList();
    }

    // ── Dietas ────────────────────────────────────────────────────────────────

    public boolean isDietaGuardada(String personaId, String dietaId) {
        return personaRepository.getDietasGuardadas(personaId).contains(dietaId);
    }

    public void guardarDieta(String personaId, String dietaId) {
        personaRepository.addDietaGuardada(personaId, dietaId);
    }

    public void desguardarDieta(String personaId, String dietaId) {
        personaRepository.removeDietaGuardada(personaId, dietaId);
    }

    public List<DietaDTO.DietaResponse> getDietasGuardadas(String personaId) {
        return personaRepository.getDietasGuardadas(personaId).stream()
                .map(id -> dietaService.obtenerPorId(id).orElse(null))
                .filter(d -> d != null)
                .toList();
    }
}
