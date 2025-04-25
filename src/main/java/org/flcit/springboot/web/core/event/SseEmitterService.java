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

import java.util.concurrent.Executor;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import org.flcit.commons.core.executor.SingleTaskThreadExecutor;
import org.flcit.commons.core.functional.consumer.ConsumerException;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class SseEmitterService {

    private SseEmitterService() { }

    /**
     * @param consumer
     * @return
     */
    public static SseEmitter send(ConsumerException<SseEmitter> consumer) {
        return send(new SingleTaskThreadExecutor(), null, consumer);
    }

    /**
     * @param timeout
     * @param consumer
     * @return
     */
    public static SseEmitter send(Long timeout, ConsumerException<SseEmitter> consumer) {
        return send(new SingleTaskThreadExecutor(), timeout, consumer);
    }

    /**
     * @param executor
     * @param timeout
     * @param consumer
     * @return
     */
    public static SseEmitter send(Executor executor, Long timeout, ConsumerException<SseEmitter> consumer) {
        final SseEmitter emitter = new SseEmitter(timeout);
        executor.execute(() -> {
            try {
                consumer.accept(emitter);
                emitter.complete();
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });
        return emitter;
    }

}
