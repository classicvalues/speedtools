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

package com.tomtom.speedtools.mongodb;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import javax.annotation.Nonnull;

import org.joda.time.DateTime;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.tomtom.speedtools.json.DateTimeSerializer;
import com.tomtom.speedtools.json.ImageSerializer;
import com.tomtom.speedtools.json.JsonObjectMapperFactory;

/**
 * MongoDB Json formatter class. This class provides access to an ObjectMapper for the MongoJsonMapper framework. Note
 * that the framework uses the Jackson 1.9.x framework, not the 2.x framework.
 * <pre>
 *
 * Example code:
 * -------------
 *
 *  // Open a Mongo DB.
 *  final com.mongodb.DB db;
 *  try {
 *      final Mongo mongo = new Mongo(host, port);
 *      db = mongo.getDB(dbName);
 *  }
 *  catch (UnknownHostException e) {
 *      ...
 *  }
 *
 *  // Open a collection.
 *  final DBCollection dbCollection = db.getCollection(collectionName);
 *
 *  // Use the MongoJsonMapper for this collection.
 *  final ObjectMapper mapper = MongoJsonMapper.getObjectMapper();
 *  final JacksonDBCollection&lt;MyPojo, String&gt; collection = JacksonDBCollection.wrap(
 *      dbCollection, MyPojo.class, String.class, mapper);
 *
 *  // Insert a POJO into the collection and read it back.
 *  MyPojo myPojo = new MyPojo(...);
 *  final WriteResult&lt;MyPojo, String&gt; result = collection.insert(myPojo);
 *  final MyPojo myPojo = collection.find().next();
 * </pre>
 */

public final class MongoDBJsonMapper extends ObjectMapper {

    // Define our default Jackson object mapper.
    @Nonnull
    private static final ObjectMapper MONGO_JSON_MAPPER;

    static {
        MONGO_JSON_MAPPER = JsonObjectMapperFactory.createJsonObjectMapper();

        // Add custom mappers.
        final SimpleModule module = new SimpleModule("DateTimeToLongJsonMapper", new Version(0, 1, 0, ""));
        module.addSerializer(DateTime.class, new DateTimeSerializer.ToLongSerializer());
        module.addDeserializer(DateTime.class, new DateTimeSerializer.FromLongDeserializer());

        module.addSerializer(Image.class, new ImageSerializer.ToBytesSerializer());
        module.addDeserializer(Image.class, new ImageSerializer.FromBytesDeserializer());

        module.addSerializer(BufferedImage.class, new ImageSerializer.ToBytesSerializer());
        module.addDeserializer(BufferedImage.class, new ImageSerializer.FromBytesDeserializerForBufferedImage());
        MONGO_JSON_MAPPER.registerModule(module);

        // Add some deserialization properties (to skip the added "_id" field).
        MONGO_JSON_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        MONGO_JSON_MAPPER.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);
        MONGO_JSON_MAPPER.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
        MONGO_JSON_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Add type information.
        MONGO_JSON_MAPPER.enableDefaultTyping(DefaultTyping.JAVA_LANG_OBJECT, JsonTypeInfo.As.PROPERTY);
    }

    /**
     * Private ctor. Prevent instantiation.
     */
    private MongoDBJsonMapper() {
        super();
        assert false;
    }

    @Nonnull
    public static ObjectMapper getObjectMapper() {
        return MONGO_JSON_MAPPER;
    }
}
