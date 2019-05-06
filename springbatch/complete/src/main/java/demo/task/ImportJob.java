package demo.task;

import com.google.common.base.Stopwatch;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class ImportJob {
    @Autowired
    @Qualifier("importPeopleJob")
    Job job;
    @Autowired
    JobLauncher jobLauncher;
    @Scheduled(cron = " 0 25 17 * * *" )
    public void importJob(){
        Stopwatch sw = Stopwatch.createStarted();
        sw.reset().start();
        JobParameter jp = new JobParameter(System.currentTimeMillis());
        Map<String,JobParameter> params = new HashMap<>();
        params.put("userJob",jp);
        JobParameters jps = new JobParameters(params);
        try {
            jobLauncher.run(job,jps);
        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {
            e.printStackTrace();
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
        }finally {
            System.out.println("###########################:"+sw.elapsed(TimeUnit.SECONDS));
        }
    }
}
