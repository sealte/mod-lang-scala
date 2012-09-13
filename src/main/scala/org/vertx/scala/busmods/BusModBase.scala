/*
 * Copyright 2011-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.vertx.scala.busmods

import org.vertx.scala.deploy.Verticle
import org.vertx.scala.core.EventBus
import org.vertx.java.core.json.JsonObject
import org.vertx.java.core.logging.Logger
import org.vertx.java.core.eventbus.Message
import org.vertx.java.core.json.JsonArray

trait BusModBase extends Verticle {

  var eb: EventBus = null

  var config: JsonObject = null

  var logger: Logger = null

  @throws(classOf[Exception])
  def start(): Unit = {
    eb = vertx.eventBus
    config = container.config()
    logger = container.logger()
  }

  def sendOK(message: Message[JsonObject]):Unit = {
    sendOK(message, null)
  }

  def sendStatus(status: String, message: Message[JsonObject]):Unit = {
    sendStatus(status, message, null)
  }

  def sendStatus(status: String, message: Message[JsonObject], json: JsonObject):Unit = {
    var reply: JsonObject = json
    if (json == null) {
      reply = new JsonObject()
    }
    reply.putString("status", status)
    message.reply(reply)
  }

  def sendOK(message: Message[JsonObject], json: JsonObject):Unit = {
    sendStatus("ok", message, json)
  }

  def sendError(message: Message[JsonObject], error: String):Unit = {
    sendError(message, error, null)
  }

  def sendError(message: Message[JsonObject], error: String, e: Exception):Unit = {
    logger.error(error, e)
    var json = new JsonObject().putString("status", "error").putString("message", error)
    message.reply(json)
  }

  def getMandatoryString(fieldName: String, message: Message[JsonObject]):String = {
    var obj = message.body.getString(fieldName)
    if (obj == null) {
      sendError(message, fieldName + " must be specified")
    }
    obj
  }

  def getMandatoryObject(fieldName: String, message: Message[JsonObject]):JsonObject = {
    var obj = message.body.getObject(fieldName)
    if (obj == null) {
      sendError(message, fieldName + " must be specified")
    }
    obj
  }

  def getOptionalBooleanConfig(fieldName: String, defaultValue: Boolean):Boolean = {
    var b = config.getBoolean(fieldName)
    if (b == null) {
      defaultValue
    }
    else {
      b
    }
  }

  def getOptionalStringConfig(fieldName: String, defaultValue: String ):String = {
    var b = config.getString(fieldName)
    if (b == null) {
      defaultValue
    }
    else {
      b
    }
  }

  def getOptionalIntConfig(fieldName: String, defaultValue: Int):Int = {
    var b = config.getInteger(fieldName)
    if (b == null) {
      defaultValue
    }
    else {
      b
    }
  }

  def getOptionalLongConfig(fieldName: String, defaultValue: Long):Long = {
    var l = config.getLong(fieldName)
    if (l == null) {
      defaultValue
    }
    else {
      l
    }
  }

  def getOptionalObjectConfig(fieldName: String, defaultValue: JsonObject):JsonObject = {
    var o = config.getObject(fieldName)
    if (o == null) {
      defaultValue
    }
    else {
      o
    }
  }

  def getOptionalArrayConfig(fieldName: String, defaultValue: JsonArray):JsonArray = {
    var a = config.getArray(fieldName)
    if (a == null) {
      defaultValue
    }
    else {
      a
    }
  }

  def getMandatoryBooleanConfig(fieldName: String):Boolean = {
    var b = config.getBoolean(fieldName)
    if (b == null) {
      throw new IllegalArgumentException(fieldName + " must be specified in config for busmod")
    }
    return b
  }

  def getMandatoryStringConfig(fieldName: String):String = {
    var s = config.getString(fieldName)
    if (s == null) {
      throw new IllegalArgumentException(fieldName + " must be specified in config for busmod")
    }
    return s
  }

  def getMandatoryIntConfig(fieldName: String):Int = {
    var i = config.getInteger(fieldName)
    if (i == null) {
      throw new IllegalArgumentException(fieldName + " must be specified in config for busmod")
    }
    return i
  }

  def getMandatoryLongConfig(fieldName: String):Long = {
    var l = config.getLong(fieldName)
    if (l == null) {
      throw new IllegalArgumentException(fieldName + " must be specified in config for busmod")
    }
    return l
  }

}