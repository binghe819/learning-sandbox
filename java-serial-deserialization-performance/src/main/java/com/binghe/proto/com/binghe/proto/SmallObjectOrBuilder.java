// Generated by the protocol buffer compiler.  DO NOT EDIT!
// NO CHECKED-IN PROTOBUF GENCODE
// source: src/main/java/com/binghe/proto/object.proto
// Protobuf Java Version: 4.28.2

package com.binghe.proto;

public interface SmallObjectOrBuilder extends
    // @@protoc_insertion_point(interface_extends:SmallObject)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int64 id = 1;</code>
   * @return The id.
   */
  long getId();

  /**
   * <code>string name = 2;</code>
   * @return The name.
   */
  java.lang.String getName();
  /**
   * <code>string name = 2;</code>
   * @return The bytes for name.
   */
  com.google.protobuf.ByteString
      getNameBytes();

  /**
   * <code>bool active = 3;</code>
   * @return The active.
   */
  boolean getActive();
}
