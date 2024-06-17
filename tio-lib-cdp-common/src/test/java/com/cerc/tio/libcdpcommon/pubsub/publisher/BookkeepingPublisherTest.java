package com.cerc.tio.libcdpcommon.pubsub.publisher;

import com.cerc.tio.libcdpcommon.domain.bookkeeping.BKTransactionDomainType;
import com.cerc.tio.libcdpcommon.domain.bookkeeping.BKTransactionRequest;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookkeepingPublisherTest {

    private final BookkeepingPublisher underTest = new BookkeepingPublisher("project-id", "topic-id");

    @Mock
    private DefaultPublisher publisher;

    @BeforeEach
    void setUp() {
        underTest.setPublisher(publisher);
    }

    @Test
    void publishAsync_shouldCallDefaultPublisher() {
        // Given
        String trace = "trace";
        BKTransactionRequest transactionRequest = new BKTransactionRequest();
        BKTransactionDomainType domainType = BKTransactionDomainType.FINANCIAL_ASSET;

        String message = new JSONObject(transactionRequest).toString();
        Map<String, String> attributes = Map.of("domain", domainType.getDomain());

        // When
        underTest.publishAsync(trace, transactionRequest, domainType);

        // Then
        verify(publisher).publishAsync(trace, message, attributes);
    }

    @Test
    void publishSync_shouldCallDefaultPublisher() throws InterruptedException {
        // Given
        String trace = "trace";
        BKTransactionRequest transactionRequest = new BKTransactionRequest();
        BKTransactionDomainType domainType = BKTransactionDomainType.FINANCIAL_ASSET;

        String message = new JSONObject(transactionRequest).toString();
        Map<String, String> attributes = Map.of("domain", domainType.getDomain());

        // When
        underTest.publishSync(trace, transactionRequest, domainType);

        // Then
        verify(publisher).publishSync(trace, message, attributes);
    }
}