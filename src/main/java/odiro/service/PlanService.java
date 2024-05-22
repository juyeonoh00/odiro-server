package travelplaner.odiro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import travelplaner.odiro.domain.Plan;
import travelplaner.odiro.mysql.PlanRepository;

@Service
@Transactional
public class PlanService {

    @Autowired
    private PlanRepository planRepository;

    public Plan initPlan(Plan plan) {
        planRepository.save(plan);
        return plan;
    }
}
