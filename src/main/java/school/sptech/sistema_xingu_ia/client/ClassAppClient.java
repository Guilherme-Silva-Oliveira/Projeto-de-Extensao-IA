package school.sptech.sistema_xingu_ia.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import school.sptech.sistema_xingu_ia.config.ClassAppConfig;
import school.sptech.sistema_xingu_ia.dto.classapp.LabelsRequest;
import school.sptech.sistema_xingu_ia.dto.classapp.TagsRequest;

@FeignClient(
        name= "classAppClient",
        url= "https://api.classapp.com.br/v1",
        configuration = ClassAppConfig.class
)
public interface ClassAppClient {
    @GetMapping("/tags")
    TagsRequest getTags();

    @GetMapping("/groups")
    String getGroups();

    @GetMapping("/student")
    String getStudents();

    @GetMapping("/staff")
    String getStaffs();

    @GetMapping("/labels")
    LabelsRequest getLabels();
}
