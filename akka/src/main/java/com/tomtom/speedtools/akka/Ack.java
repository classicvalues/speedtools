/*
 * Copyright (C) 2015. TomTom International BV (http://tomtom.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tomtom.speedtools.akka;


import javax.annotation.Nullable;
import java.io.Serializable;

/**
 * Singleton message that can be used to implement synchronous void messages. Note that through deserialization,
 * multiple instances might exist. They are all equal though.
 */
public final class Ack implements Serializable {

    public static final Ack INSTANCE = new Ack();

    private Ack() {
        super();
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public boolean equals(@Nullable final Object obj) {
        return obj instanceof Ack;
    }
}
