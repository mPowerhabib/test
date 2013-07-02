package org.ei.drishti.service;

import org.ei.drishti.domain.Child;
import org.ei.drishti.domain.Mother;
import org.ei.drishti.domain.ServiceProvided;
import org.ei.drishti.domain.form.FormSubmission;
import org.ei.drishti.dto.Action;
import org.ei.drishti.repository.AllTimelineEvents;
import org.ei.drishti.repository.ChildRepository;
import org.ei.drishti.repository.MotherRepository;

import java.util.List;

import static org.ei.drishti.AllConstants.SPACE;
import static org.ei.drishti.domain.TimelineEvent.forChildImmunization;
import static org.ei.drishti.domain.TimelineEvent.forChildPNCVisit;

public class ChildService {
    private ChildRepository childRepository;
    private MotherRepository motherRepository;
    private AllTimelineEvents allTimelines;
    private ServiceProvidedService serviceProvidedService;

    public ChildService(MotherRepository motherRepository, ChildRepository childRepository,
                        AllTimelineEvents allTimelineEvents, ServiceProvidedService serviceProvidedService) {
        this.childRepository = childRepository;
        this.motherRepository = motherRepository;
        this.allTimelines = allTimelineEvents;
        this.serviceProvidedService = serviceProvidedService;
    }

    public void register(FormSubmission submission) {
        Mother mother = motherRepository.findById(submission.entityId());
        List<Child> children = childRepository.findByMotherCaseId(mother.caseId());

        for (Child child : children) {
            childRepository.update(child.setIsClosed(false).setThayiCardNumber(mother.thayiCardNumber()).setDateOfBirth(mother.referenceDate()));
        }
    }

    public void pncRegistration(FormSubmission submission) {
        Mother mother = motherRepository.findAllCasesForEC(submission.entityId()).get(0);
        List<Child> children = childRepository.findByMotherCaseId(mother.caseId());

        for (Child child : children) {
            childRepository.update(child.setIsClosed(false).setThayiCardNumber(mother.thayiCardNumber()).setDateOfBirth(mother.referenceDate()));
        }
    }

    public void updateImmunizations(FormSubmission submission) {
        String immunizationsGiven = submission.getFieldValue("immunizationsGiven");
        String immunizationDate = submission.getFieldValue("immunizationDate");
        allTimelines.add(forChildImmunization(submission.entityId(), immunizationsGiven, immunizationDate));
        for (String immunization : immunizationsGiven.split(SPACE)) {
            serviceProvidedService.add(ServiceProvided.forChildImmunization(submission.entityId(), immunization, immunizationDate));
        }
    }

    public void pncVisit(Action action) {
        allTimelines.add(forChildPNCVisit(action.caseID(), action.get("visitNumber"), action.get("visitDate"), action.details()));
        childRepository.updateDetails(action.caseID(), action.details());
    }

    public void delete(Action action) {
        childRepository.close(action.caseID());
    }
}
