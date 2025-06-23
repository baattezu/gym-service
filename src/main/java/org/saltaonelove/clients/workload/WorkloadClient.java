package org.saltaonelove.clients.workload;

import org.saltaonelove.dto.workload.WorkloadRequest;

public interface WorkloadClient {
    void updateTrainerWorkload(WorkloadRequest workloadRequest);
    void deleteTrainerWorkloadHistory(String username);

}
