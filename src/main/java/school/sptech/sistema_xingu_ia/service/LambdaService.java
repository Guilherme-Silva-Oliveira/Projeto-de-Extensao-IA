package school.sptech.sistema_xingu_ia.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvocationType;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class LambdaService {

    private final LambdaClient lambdaClient;
    private final ObjectMapper objectMapper;

    @Value("${aws.lambda.function-name}")
    private String functionName;

    @Async
    public void enviarParaS3(Object solicitacao) {
        try {
            String json = objectMapper.writeValueAsString(solicitacao);

            InvokeResponse response = lambdaClient.invoke(InvokeRequest.builder()
                    .functionName(functionName)
                    .invocationType(InvocationType.EVENT)
                    .payload(SdkBytes.fromUtf8String(json))
                    .build());

            log.info("Lambda {} acionada, status {}", functionName, response.statusCode());
        } catch (Exception e) {
            log.error("Falha ao acionar a Lambda", e);
        }
    }
}