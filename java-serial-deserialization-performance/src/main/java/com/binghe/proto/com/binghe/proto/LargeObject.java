// Generated by the protocol buffer compiler.  DO NOT EDIT!
// NO CHECKED-IN PROTOBUF GENCODE
// source: src/main/java/com/binghe/proto/object.proto
// Protobuf Java Version: 4.28.2

package com.binghe.proto;

/**
 * Protobuf type {@code LargeObject}
 */
public final class LargeObject extends
    com.google.protobuf.GeneratedMessage implements
    // @@protoc_insertion_point(message_implements:LargeObject)
    LargeObjectOrBuilder {
private static final long serialVersionUID = 0L;
  static {
    com.google.protobuf.RuntimeVersion.validateProtobufGencodeVersion(
      com.google.protobuf.RuntimeVersion.RuntimeDomain.PUBLIC,
      /* major= */ 4,
      /* minor= */ 28,
      /* patch= */ 2,
      /* suffix= */ "",
      LargeObject.class.getName());
  }
  // Use LargeObject.newBuilder() to construct.
  private LargeObject(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
    super(builder);
  }
  private LargeObject() {
    name_ = "";
    smallObjects_ = java.util.Collections.emptyList();
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.binghe.proto.Object.internal_static_LargeObject_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.binghe.proto.Object.internal_static_LargeObject_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.binghe.proto.LargeObject.class, com.binghe.proto.LargeObject.Builder.class);
  }

  public static final int ID_FIELD_NUMBER = 1;
  private long id_ = 0L;
  /**
   * <code>int64 id = 1;</code>
   * @return The id.
   */
  @java.lang.Override
  public long getId() {
    return id_;
  }

  public static final int NAME_FIELD_NUMBER = 2;
  @SuppressWarnings("serial")
  private volatile java.lang.Object name_ = "";
  /**
   * <code>string name = 2;</code>
   * @return The name.
   */
  @java.lang.Override
  public java.lang.String getName() {
    java.lang.Object ref = name_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      name_ = s;
      return s;
    }
  }
  /**
   * <code>string name = 2;</code>
   * @return The bytes for name.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getNameBytes() {
    java.lang.Object ref = name_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      name_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int SMALLOBJECTS_FIELD_NUMBER = 3;
  @SuppressWarnings("serial")
  private java.util.List<com.binghe.proto.SmallObject> smallObjects_;
  /**
   * <code>repeated .SmallObject smallObjects = 3;</code>
   */
  @java.lang.Override
  public java.util.List<com.binghe.proto.SmallObject> getSmallObjectsList() {
    return smallObjects_;
  }
  /**
   * <code>repeated .SmallObject smallObjects = 3;</code>
   */
  @java.lang.Override
  public java.util.List<? extends com.binghe.proto.SmallObjectOrBuilder> 
      getSmallObjectsOrBuilderList() {
    return smallObjects_;
  }
  /**
   * <code>repeated .SmallObject smallObjects = 3;</code>
   */
  @java.lang.Override
  public int getSmallObjectsCount() {
    return smallObjects_.size();
  }
  /**
   * <code>repeated .SmallObject smallObjects = 3;</code>
   */
  @java.lang.Override
  public com.binghe.proto.SmallObject getSmallObjects(int index) {
    return smallObjects_.get(index);
  }
  /**
   * <code>repeated .SmallObject smallObjects = 3;</code>
   */
  @java.lang.Override
  public com.binghe.proto.SmallObjectOrBuilder getSmallObjectsOrBuilder(
      int index) {
    return smallObjects_.get(index);
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (id_ != 0L) {
      output.writeInt64(1, id_);
    }
    if (!com.google.protobuf.GeneratedMessage.isStringEmpty(name_)) {
      com.google.protobuf.GeneratedMessage.writeString(output, 2, name_);
    }
    for (int i = 0; i < smallObjects_.size(); i++) {
      output.writeMessage(3, smallObjects_.get(i));
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (id_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(1, id_);
    }
    if (!com.google.protobuf.GeneratedMessage.isStringEmpty(name_)) {
      size += com.google.protobuf.GeneratedMessage.computeStringSize(2, name_);
    }
    for (int i = 0; i < smallObjects_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(3, smallObjects_.get(i));
    }
    size += getUnknownFields().getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.binghe.proto.LargeObject)) {
      return super.equals(obj);
    }
    com.binghe.proto.LargeObject other = (com.binghe.proto.LargeObject) obj;

    if (getId()
        != other.getId()) return false;
    if (!getName()
        .equals(other.getName())) return false;
    if (!getSmallObjectsList()
        .equals(other.getSmallObjectsList())) return false;
    if (!getUnknownFields().equals(other.getUnknownFields())) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + ID_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getId());
    hash = (37 * hash) + NAME_FIELD_NUMBER;
    hash = (53 * hash) + getName().hashCode();
    if (getSmallObjectsCount() > 0) {
      hash = (37 * hash) + SMALLOBJECTS_FIELD_NUMBER;
      hash = (53 * hash) + getSmallObjectsList().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.binghe.proto.LargeObject parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.binghe.proto.LargeObject parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.binghe.proto.LargeObject parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.binghe.proto.LargeObject parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.binghe.proto.LargeObject parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.binghe.proto.LargeObject parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.binghe.proto.LargeObject parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input);
  }
  public static com.binghe.proto.LargeObject parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public static com.binghe.proto.LargeObject parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseDelimitedWithIOException(PARSER, input);
  }

  public static com.binghe.proto.LargeObject parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.binghe.proto.LargeObject parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input);
  }
  public static com.binghe.proto.LargeObject parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.binghe.proto.LargeObject prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessage.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code LargeObject}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessage.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:LargeObject)
      com.binghe.proto.LargeObjectOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.binghe.proto.Object.internal_static_LargeObject_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.binghe.proto.Object.internal_static_LargeObject_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.binghe.proto.LargeObject.class, com.binghe.proto.LargeObject.Builder.class);
    }

    // Construct using com.binghe.proto.LargeObject.newBuilder()
    private Builder() {

    }

    private Builder(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      super(parent);

    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      id_ = 0L;
      name_ = "";
      if (smallObjectsBuilder_ == null) {
        smallObjects_ = java.util.Collections.emptyList();
      } else {
        smallObjects_ = null;
        smallObjectsBuilder_.clear();
      }
      bitField0_ = (bitField0_ & ~0x00000004);
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.binghe.proto.Object.internal_static_LargeObject_descriptor;
    }

    @java.lang.Override
    public com.binghe.proto.LargeObject getDefaultInstanceForType() {
      return com.binghe.proto.LargeObject.getDefaultInstance();
    }

    @java.lang.Override
    public com.binghe.proto.LargeObject build() {
      com.binghe.proto.LargeObject result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.binghe.proto.LargeObject buildPartial() {
      com.binghe.proto.LargeObject result = new com.binghe.proto.LargeObject(this);
      buildPartialRepeatedFields(result);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartialRepeatedFields(com.binghe.proto.LargeObject result) {
      if (smallObjectsBuilder_ == null) {
        if (((bitField0_ & 0x00000004) != 0)) {
          smallObjects_ = java.util.Collections.unmodifiableList(smallObjects_);
          bitField0_ = (bitField0_ & ~0x00000004);
        }
        result.smallObjects_ = smallObjects_;
      } else {
        result.smallObjects_ = smallObjectsBuilder_.build();
      }
    }

    private void buildPartial0(com.binghe.proto.LargeObject result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.id_ = id_;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.name_ = name_;
      }
    }

    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.binghe.proto.LargeObject) {
        return mergeFrom((com.binghe.proto.LargeObject)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.binghe.proto.LargeObject other) {
      if (other == com.binghe.proto.LargeObject.getDefaultInstance()) return this;
      if (other.getId() != 0L) {
        setId(other.getId());
      }
      if (!other.getName().isEmpty()) {
        name_ = other.name_;
        bitField0_ |= 0x00000002;
        onChanged();
      }
      if (smallObjectsBuilder_ == null) {
        if (!other.smallObjects_.isEmpty()) {
          if (smallObjects_.isEmpty()) {
            smallObjects_ = other.smallObjects_;
            bitField0_ = (bitField0_ & ~0x00000004);
          } else {
            ensureSmallObjectsIsMutable();
            smallObjects_.addAll(other.smallObjects_);
          }
          onChanged();
        }
      } else {
        if (!other.smallObjects_.isEmpty()) {
          if (smallObjectsBuilder_.isEmpty()) {
            smallObjectsBuilder_.dispose();
            smallObjectsBuilder_ = null;
            smallObjects_ = other.smallObjects_;
            bitField0_ = (bitField0_ & ~0x00000004);
            smallObjectsBuilder_ = 
              com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                 getSmallObjectsFieldBuilder() : null;
          } else {
            smallObjectsBuilder_.addAllMessages(other.smallObjects_);
          }
        }
      }
      this.mergeUnknownFields(other.getUnknownFields());
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 8: {
              id_ = input.readInt64();
              bitField0_ |= 0x00000001;
              break;
            } // case 8
            case 18: {
              name_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000002;
              break;
            } // case 18
            case 26: {
              com.binghe.proto.SmallObject m =
                  input.readMessage(
                      com.binghe.proto.SmallObject.parser(),
                      extensionRegistry);
              if (smallObjectsBuilder_ == null) {
                ensureSmallObjectsIsMutable();
                smallObjects_.add(m);
              } else {
                smallObjectsBuilder_.addMessage(m);
              }
              break;
            } // case 26
            default: {
              if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                done = true; // was an endgroup tag
              }
              break;
            } // default:
          } // switch (tag)
        } // while (!done)
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.unwrapIOException();
      } finally {
        onChanged();
      } // finally
      return this;
    }
    private int bitField0_;

    private long id_ ;
    /**
     * <code>int64 id = 1;</code>
     * @return The id.
     */
    @java.lang.Override
    public long getId() {
      return id_;
    }
    /**
     * <code>int64 id = 1;</code>
     * @param value The id to set.
     * @return This builder for chaining.
     */
    public Builder setId(long value) {

      id_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>int64 id = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearId() {
      bitField0_ = (bitField0_ & ~0x00000001);
      id_ = 0L;
      onChanged();
      return this;
    }

    private java.lang.Object name_ = "";
    /**
     * <code>string name = 2;</code>
     * @return The name.
     */
    public java.lang.String getName() {
      java.lang.Object ref = name_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        name_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string name = 2;</code>
     * @return The bytes for name.
     */
    public com.google.protobuf.ByteString
        getNameBytes() {
      java.lang.Object ref = name_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        name_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string name = 2;</code>
     * @param value The name to set.
     * @return This builder for chaining.
     */
    public Builder setName(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      name_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>string name = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearName() {
      name_ = getDefaultInstance().getName();
      bitField0_ = (bitField0_ & ~0x00000002);
      onChanged();
      return this;
    }
    /**
     * <code>string name = 2;</code>
     * @param value The bytes for name to set.
     * @return This builder for chaining.
     */
    public Builder setNameBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      name_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }

    private java.util.List<com.binghe.proto.SmallObject> smallObjects_ =
      java.util.Collections.emptyList();
    private void ensureSmallObjectsIsMutable() {
      if (!((bitField0_ & 0x00000004) != 0)) {
        smallObjects_ = new java.util.ArrayList<com.binghe.proto.SmallObject>(smallObjects_);
        bitField0_ |= 0x00000004;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilder<
        com.binghe.proto.SmallObject, com.binghe.proto.SmallObject.Builder, com.binghe.proto.SmallObjectOrBuilder> smallObjectsBuilder_;

    /**
     * <code>repeated .SmallObject smallObjects = 3;</code>
     */
    public java.util.List<com.binghe.proto.SmallObject> getSmallObjectsList() {
      if (smallObjectsBuilder_ == null) {
        return java.util.Collections.unmodifiableList(smallObjects_);
      } else {
        return smallObjectsBuilder_.getMessageList();
      }
    }
    /**
     * <code>repeated .SmallObject smallObjects = 3;</code>
     */
    public int getSmallObjectsCount() {
      if (smallObjectsBuilder_ == null) {
        return smallObjects_.size();
      } else {
        return smallObjectsBuilder_.getCount();
      }
    }
    /**
     * <code>repeated .SmallObject smallObjects = 3;</code>
     */
    public com.binghe.proto.SmallObject getSmallObjects(int index) {
      if (smallObjectsBuilder_ == null) {
        return smallObjects_.get(index);
      } else {
        return smallObjectsBuilder_.getMessage(index);
      }
    }
    /**
     * <code>repeated .SmallObject smallObjects = 3;</code>
     */
    public Builder setSmallObjects(
        int index, com.binghe.proto.SmallObject value) {
      if (smallObjectsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureSmallObjectsIsMutable();
        smallObjects_.set(index, value);
        onChanged();
      } else {
        smallObjectsBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .SmallObject smallObjects = 3;</code>
     */
    public Builder setSmallObjects(
        int index, com.binghe.proto.SmallObject.Builder builderForValue) {
      if (smallObjectsBuilder_ == null) {
        ensureSmallObjectsIsMutable();
        smallObjects_.set(index, builderForValue.build());
        onChanged();
      } else {
        smallObjectsBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .SmallObject smallObjects = 3;</code>
     */
    public Builder addSmallObjects(com.binghe.proto.SmallObject value) {
      if (smallObjectsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureSmallObjectsIsMutable();
        smallObjects_.add(value);
        onChanged();
      } else {
        smallObjectsBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <code>repeated .SmallObject smallObjects = 3;</code>
     */
    public Builder addSmallObjects(
        int index, com.binghe.proto.SmallObject value) {
      if (smallObjectsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureSmallObjectsIsMutable();
        smallObjects_.add(index, value);
        onChanged();
      } else {
        smallObjectsBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .SmallObject smallObjects = 3;</code>
     */
    public Builder addSmallObjects(
        com.binghe.proto.SmallObject.Builder builderForValue) {
      if (smallObjectsBuilder_ == null) {
        ensureSmallObjectsIsMutable();
        smallObjects_.add(builderForValue.build());
        onChanged();
      } else {
        smallObjectsBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .SmallObject smallObjects = 3;</code>
     */
    public Builder addSmallObjects(
        int index, com.binghe.proto.SmallObject.Builder builderForValue) {
      if (smallObjectsBuilder_ == null) {
        ensureSmallObjectsIsMutable();
        smallObjects_.add(index, builderForValue.build());
        onChanged();
      } else {
        smallObjectsBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .SmallObject smallObjects = 3;</code>
     */
    public Builder addAllSmallObjects(
        java.lang.Iterable<? extends com.binghe.proto.SmallObject> values) {
      if (smallObjectsBuilder_ == null) {
        ensureSmallObjectsIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, smallObjects_);
        onChanged();
      } else {
        smallObjectsBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <code>repeated .SmallObject smallObjects = 3;</code>
     */
    public Builder clearSmallObjects() {
      if (smallObjectsBuilder_ == null) {
        smallObjects_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000004);
        onChanged();
      } else {
        smallObjectsBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>repeated .SmallObject smallObjects = 3;</code>
     */
    public Builder removeSmallObjects(int index) {
      if (smallObjectsBuilder_ == null) {
        ensureSmallObjectsIsMutable();
        smallObjects_.remove(index);
        onChanged();
      } else {
        smallObjectsBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <code>repeated .SmallObject smallObjects = 3;</code>
     */
    public com.binghe.proto.SmallObject.Builder getSmallObjectsBuilder(
        int index) {
      return getSmallObjectsFieldBuilder().getBuilder(index);
    }
    /**
     * <code>repeated .SmallObject smallObjects = 3;</code>
     */
    public com.binghe.proto.SmallObjectOrBuilder getSmallObjectsOrBuilder(
        int index) {
      if (smallObjectsBuilder_ == null) {
        return smallObjects_.get(index);  } else {
        return smallObjectsBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <code>repeated .SmallObject smallObjects = 3;</code>
     */
    public java.util.List<? extends com.binghe.proto.SmallObjectOrBuilder> 
         getSmallObjectsOrBuilderList() {
      if (smallObjectsBuilder_ != null) {
        return smallObjectsBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(smallObjects_);
      }
    }
    /**
     * <code>repeated .SmallObject smallObjects = 3;</code>
     */
    public com.binghe.proto.SmallObject.Builder addSmallObjectsBuilder() {
      return getSmallObjectsFieldBuilder().addBuilder(
          com.binghe.proto.SmallObject.getDefaultInstance());
    }
    /**
     * <code>repeated .SmallObject smallObjects = 3;</code>
     */
    public com.binghe.proto.SmallObject.Builder addSmallObjectsBuilder(
        int index) {
      return getSmallObjectsFieldBuilder().addBuilder(
          index, com.binghe.proto.SmallObject.getDefaultInstance());
    }
    /**
     * <code>repeated .SmallObject smallObjects = 3;</code>
     */
    public java.util.List<com.binghe.proto.SmallObject.Builder> 
         getSmallObjectsBuilderList() {
      return getSmallObjectsFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilder<
        com.binghe.proto.SmallObject, com.binghe.proto.SmallObject.Builder, com.binghe.proto.SmallObjectOrBuilder> 
        getSmallObjectsFieldBuilder() {
      if (smallObjectsBuilder_ == null) {
        smallObjectsBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
            com.binghe.proto.SmallObject, com.binghe.proto.SmallObject.Builder, com.binghe.proto.SmallObjectOrBuilder>(
                smallObjects_,
                ((bitField0_ & 0x00000004) != 0),
                getParentForChildren(),
                isClean());
        smallObjects_ = null;
      }
      return smallObjectsBuilder_;
    }

    // @@protoc_insertion_point(builder_scope:LargeObject)
  }

  // @@protoc_insertion_point(class_scope:LargeObject)
  private static final com.binghe.proto.LargeObject DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.binghe.proto.LargeObject();
  }

  public static com.binghe.proto.LargeObject getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<LargeObject>
      PARSER = new com.google.protobuf.AbstractParser<LargeObject>() {
    @java.lang.Override
    public LargeObject parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      Builder builder = newBuilder();
      try {
        builder.mergeFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(builder.buildPartial());
      } catch (com.google.protobuf.UninitializedMessageException e) {
        throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(e)
            .setUnfinishedMessage(builder.buildPartial());
      }
      return builder.buildPartial();
    }
  };

  public static com.google.protobuf.Parser<LargeObject> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<LargeObject> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.binghe.proto.LargeObject getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

