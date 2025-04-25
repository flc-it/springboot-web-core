/*
 * Copyright 2002-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.flcit.springboot.web.core.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.concurrent.Executor;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import org.flcit.commons.core.functional.consumer.ConsumerException;

class SseEmitterServiceTest {

    private static final long TIMEOUT = 60000L;

    @SuppressWarnings("unchecked")
    @Test
    void test() throws Exception {
        final ConsumerException<SseEmitter> consumerMock = mock(ConsumerException.class);
        assertNull(SseEmitterService.send(consumerMock).getTimeout());
        assertEquals(TIMEOUT, SseEmitterService.send(TIMEOUT, consumerMock).getTimeout());
        final Executor executor = spy(Executor.class);
        SseEmitterService.send(executor, TIMEOUT, null);
        verify(executor, times(1)).execute(any());
        final ConsumerException<SseEmitter> consumer = spy(ConsumerException.class);
        SseEmitterService.send(new SyncTaskExecutor(), TIMEOUT, consumer);
        verify(consumer, times(1)).accept(any());

        try (MockedConstruction<SseEmitter> mocked = mockConstruction(SseEmitter.class)) {
            SseEmitterService.send(new SyncTaskExecutor(), TIMEOUT, emit -> { });
            verify(mocked.constructed().get(0), times(1)).complete();
        }

        try (MockedConstruction<SseEmitter> mocked = mockConstruction(SseEmitter.class)) {
            SseEmitterService.send(new SyncTaskExecutor(), TIMEOUT, emit -> { throw new IllegalStateException(); });
            verify(mocked.constructed().get(0), times(1)).completeWithError(any(IllegalStateException.class));
        }
    }

}