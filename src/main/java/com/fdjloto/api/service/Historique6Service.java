package com.fdjloto.api.service;

import com.fdjloto.api.model.Historique6Result;
import com.fdjloto.api.repository.Historique6Repository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class Historique6Service {

    private final Historique6Repository historique6Repository;

    public Historique6Service(Historique6Repository historique6Repository) {
        this.historique6Repository = historique6Repository;
    }

    public List<Historique6Result> getLast6Results() {
        return historique6Repository.findTop6ByOrderByDateDeTirageDesc();
    }
}
