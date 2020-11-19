package com.unibo.iss.manager.controller;

import com.unibo.iss.manager.beans.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class EntityController {

    private Map<String, Map<String, Optional<EntityAbstraction>>> entityMap = new HashMap<>();

    @GetMapping("/api/state/{owner}/{statename}")
    synchronized public String getStateByOwnerName(@PathVariable String owner, String statename) {
        return this.entityMap.get(owner).getOrDefault(statename, Optional.of(new Entity("NONE"))).orElseGet(() -> new Entity("NONE")).getContent();
    }

}