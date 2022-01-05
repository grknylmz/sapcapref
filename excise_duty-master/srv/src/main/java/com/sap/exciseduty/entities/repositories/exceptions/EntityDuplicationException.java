package com.sap.exciseduty.entities.repositories.exceptions;

public class EntityDuplicationException extends Exception {

    private String entity;

    private static final long serialVersionUID = 5927312436054869574L;

    public EntityDuplicationException(String entity) {
        this.entity = entity;
    }

    public String getEntity() {
        return this.entity;
    }

}
