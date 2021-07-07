package com.v1.cvealert.service;

import com.v1.cvealert.domainobject.ReportsDO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public interface IReportsService {
    ReportsDO createReports(ReportsDO reports);
    List<ReportsDO> getAllReports();
    List<ReportsDO> getReportsByUserID(Long userID);
}
