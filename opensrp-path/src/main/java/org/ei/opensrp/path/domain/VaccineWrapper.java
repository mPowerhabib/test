package org.ei.opensrp.path.domain;

import org.ei.opensrp.domain.Alert;
import org.ei.opensrp.path.db.VaccineRepo.Vaccine;
import org.joda.time.DateTime;

import java.util.Date;

/**
 * Created by keyman on 16/11/2016.
 */
public class VaccineWrapper {
    private String id;
    private Long dbKey;
    private Photo photo;
    private String name;
    private String gender;
    private String status;
    private Vaccine vaccine;
    private DateTime vaccineDate;
    private Alert alert;
    private String previousVaccineId;
    private boolean compact;

    private String color;
    private String formattedVaccineDate;
    private String existingAge;

    private String patientName;
    private String patientNumber;

    private DateTime updatedVaccineDate;
    private DateTime recordedDate;

    private boolean today;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getDbKey() {
        return dbKey;
    }

    public void setDbKey(Long dbKey) {
        this.dbKey = dbKey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setVaccine(Vaccine vaccine) {
        this.vaccine = vaccine;
    }

    public Vaccine getVaccine() {
        return vaccine;
    }

    public DateTime getVaccineDate() {
        return vaccineDate;
    }

    public void setVaccineDate(DateTime vaccineDate) {
        this.vaccineDate = vaccineDate;
    }

    public Alert getAlert() {
        return alert;
    }

    public void setAlert(Alert alert) {
        this.alert = alert;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getPreviousVaccineId() {
        return previousVaccineId;
    }

    public void setPreviousVaccine(String previousVaccineId) {
        this.previousVaccineId = previousVaccineId;
    }

    public boolean isCompact() {
        return compact;
    }

    public void setCompact(boolean compact) {
        this.compact = compact;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFormattedVaccineDate() {
        return formattedVaccineDate;
    }

    public void setFormattedVaccineDate(String formattedVaccineDate) {
        this.formattedVaccineDate = formattedVaccineDate;
    }

    public String getExistingAge() {
        return existingAge;
    }

    public void setExistingAge(String existingAge) {
        this.existingAge = existingAge;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientNumber() {
        return patientNumber;
    }

    public void setPatientNumber(String patientNumber) {
        this.patientNumber = patientNumber;
    }

    public DateTime getUpdatedVaccineDate() {
        return updatedVaccineDate;
    }

    public void setUpdatedVaccineDate(DateTime updatedVaccineDate, boolean today) {
        this.today = today;
        this.updatedVaccineDate = updatedVaccineDate;
        if (!isToday()) {
            this.recordedDate = new DateTime(new Date());
        } else {
            this.recordedDate = updatedVaccineDate;
        }
    }

    public boolean isToday() {
        return today;
    }

    public String getVaccineDateAsString() {
        return vaccineDate != null ? vaccineDate.toString("yyyy-MM-dd") : "";
    }

    public String getUpdatedVaccineDateAsString() {
        return updatedVaccineDate != null ? updatedVaccineDate.toString("yyyy-MM-dd") : "";
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public Photo getPhoto() {
        return photo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setRecordedDate(DateTime recordedDate) {
        this.recordedDate = recordedDate;
    }

    public DateTime getRecordedDate() {
        return recordedDate;
    }
}
