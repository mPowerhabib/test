package org.ei.opensrp.domain;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by keyman on 3/1/17.
 */
public class Vaccine {
    private static final String ZEIR_ID = "ZEIR_ID";
    Long id;
    String baseEntityId;
    String programClientId;
    String name;
    Integer calculation;
    Date date;
    String anmId;
    String locationId;
    String syncStatus;
    Long updatedAt;
    String eventId;

    public Vaccine() {
    }

    public Vaccine(Long id, String baseEntityId, String name, Integer calculation, Date date, String anmId, String locationId, String syncStatus, Long updatedAt,String eventId) {
        this.id = id;
        this.baseEntityId = baseEntityId;
        this.programClientId = null;
        this.name = name;
        this.calculation = calculation;
        this.date = date;
        this.anmId = anmId;
        this.locationId = locationId;
        this.syncStatus = syncStatus;
        this.updatedAt = updatedAt;
        this.eventId=eventId;
    }

    public Vaccine(Long id, String baseEntityId, String programClientId, String name, Integer calculation, Date date, String anmId, String locationId, String syncStatus, Long updatedAt,String eventId) {
        this.id = id;
        this.baseEntityId = baseEntityId;
        this.programClientId = programClientId;
        this.name = name;
        this.calculation = calculation;
        this.date = date;
        this.anmId = anmId;
        this.locationId = locationId;
        this.syncStatus = syncStatus;
        this.updatedAt = updatedAt;
        this.eventId=eventId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBaseEntityId() {
        return baseEntityId;
    }

    public void setBaseEntityId(String baseEntityId) {
        this.baseEntityId = baseEntityId;
    }

    public String getProgramClientId() {
        return programClientId;
    }

    public void setProgramClientId(String programClientId) {
        this.programClientId = programClientId;
    }

    public HashMap<String, String> getIdentifiers() {
        HashMap<String, String> identifiers = null;
        if (programClientId != null) {
            identifiers = new HashMap<>();
            identifiers.put(ZEIR_ID, programClientId);
        }
        return identifiers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCalculation() {
        return calculation;
    }

    public void setCalculation(Integer calculation) {
        this.calculation = calculation;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAnmId() {
        return anmId;
    }

    public void setAnmId(String anmId) {
        this.anmId = anmId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
