package tn.esprit.spring.services;

import tn.esprit.spring.dto.PisteDTO;
import tn.esprit.spring.entities.Piste;

import java.util.List;

public interface IPisteServices {

    List<Piste> retrieveAllPistes();

    Piste addPiste(PisteDTO pisteDTO);

    void removePiste (Long numPiste);

    Piste retrievePiste (Long numPiste);
}
