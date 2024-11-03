package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.dto.PisteDTO;
import tn.esprit.spring.entities.Color;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.repositories.IPisteRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
@AllArgsConstructor
@Service
public class PisteServicesImpl implements  IPisteServices{

    private IPisteRepository pisteRepository;

    @Override
    public List<Piste> retrieveAllPistes() {

        return pisteRepository.findAll();
    }

    @Override
    public Piste addPiste(PisteDTO pisteDTO) {
        // Convert DTO to entity
        Piste piste = new Piste();
        piste.setNamePiste(pisteDTO.getNamePiste());
        piste.setColor(Color.valueOf(pisteDTO.getColor().toUpperCase()));
        piste.setLength(pisteDTO.getLength());
        piste.setSlope(pisteDTO.getSlope());

        // Save entity to the repository
        return pisteRepository.save(piste);
    }

    @Override
    public void removePiste(Long numPiste) {
        pisteRepository.deleteById(numPiste);
    }

    @Override
    public Piste retrievePiste(Long numPiste) {
        return pisteRepository.findById(numPiste).orElseThrow(() -> new EntityNotFoundException("Piste not found with ID: " + numPiste));
    }
}
