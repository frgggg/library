package com.cft.focusstart.library.quartz.finddebtors;

import com.cft.focusstart.library.model.Reader;
import com.cft.focusstart.library.service.ReaderService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class FindDebtorsJob  implements Job {
    @Autowired
    private ReaderService readerService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.debug(this.getClass().getSimpleName() + " - " + LocalDateTime.now());
        List<Reader> notDebtors = readerService.findNotDebtors();
        if(notDebtors != null) {
            if(notDebtors.size() > 0){
                for(Reader reader: notDebtors) {
                    readerService.refreshDebtorStatus(reader.getId());
                }
            }
        }
    }
}
