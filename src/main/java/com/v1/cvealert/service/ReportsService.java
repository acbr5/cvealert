package com.v1.cvealert.service;

import com.v1.cvealert.domainobject.ReportsDO;
import com.v1.cvealert.repository.IReportsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ReportsService implements IReportsService{

    @Autowired
    private IReportsRepository reportsRepository;

    @Override
    public ReportsDO createReports(ReportsDO reports) {
        return reportsRepository.save(reports);
    }

    @Override
    public List<ReportsDO> getAllReports() {
        return reportsRepository.findAll();
    }

    @Override
    public List<ReportsDO> getReportsByUserID(Long userID) {
        return reportsRepository.findByUserID(userID);
    }
}
