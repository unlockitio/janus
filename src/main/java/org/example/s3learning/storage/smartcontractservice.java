
package org.example.s3learning.storage;

import com.daml.ledger.javaapi.data.*;
import com.daml.ledger.rxjava.DamlLedgerClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class damlservice {

    @Value("${daml.ledger.host}")
    private String host;

    @Value("${daml.ledger.port}")
    private int port;

    private DamlLedgerClient client;

    public void connect() {
        client = DamlLedgerClient.newBuilder(host, port).build();
        client.connect();
    }

    public String getPublicKeyForRequestId(String requestId) {
        List<CallerKey.Contract> contracts = client.getActiveContractSetClient()
            .getActiveContracts(CallerKey.TEMPLATE_ID, Set.of("owner"), true)
              .blockingIterable()
         .iterator()
           .next()
            .getCreatedEvents()
           .stream()
            .map(CallerKey.Contract::fromCreatedEvent)
            .filter(c -> c.data.requestId.equals(requestId))
            .toList();

        if (contracts.isEmpty()) throw new RuntimeException("No key found for requestId: " + requestId);
        return contracts.get(0).data.publicKey;
    }
}
